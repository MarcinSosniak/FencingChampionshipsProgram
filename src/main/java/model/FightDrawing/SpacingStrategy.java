package model.FightDrawing;

import model.*;
import model.KillerDrawing.KillerRandomizerStrategy;
import util.RationalNumber;

import java.util.*;
import java.util.stream.Collectors;

public class SpacingStrategy extends FightDrawStrategy {

    private KillerRandomizerStrategy strat;

    SpacingStrategy(KillerRandomizerStrategy strat)
    {
        this.strat =strat;
    }

    @Override
    public List<CompetitionGroup> drawFightsForRound(Round round, int groupSize, List<Participant> participants) {
        if(participants.size()%groupSize==0)
        {
            return generate_no_killers(round, groupSize,  participants);
        }
        int groups = participants.size()/groupSize;
        int killer_group_non_killers = participants.size()% groupSize;
        if (killer_group_non_killers > 0) groups++;
        List<List<Participant>> groupsParticpantsList= new ArrayList<>();
        for (int i=0;i<groups;i++)
        {
            groupsParticpantsList.add(new ArrayList<>());
        }
        List<Participant> participantsCopy = new ArrayList<>(participants);
        Collections.shuffle(participantsCopy);
        List<Participant> sorted_particpants = participantsCopy.stream().sorted(new Comparator<Participant>() {
            @Override
            public int compare(Participant o1, Participant o2) {
                return RationalNumber.compare(o1.getOldSeasonPointsForWeaponProperty(round.getMyWeaponCompetition().getWeaponType()).get(),
                        o2.getOldSeasonPointsForWeaponProperty(round.getMyWeaponCompetition().getWeaponType()).get());
            }
        }).collect(Collectors.toList());

        List<Participant> usedParticpants = new ArrayList<>();
        int idx = 0;
        boolean fUp=true; // we got from 0 to grouops -1 and from groups -1 to 0
        for (int i=0;i<groups*killer_group_non_killers;i++)
        {
            Participant p = sorted_particpants.get(i);
            groupsParticpantsList.get(idx).add(p);
            usedParticpants.add(p);
            if(fUp)
            {
                idx++;
                if(idx >=groups)
                {
                    fUp=false;
                    idx--;
                }
            }
            else
            {
                idx--;
                if(idx < 0)
                {
                    fUp=true;
                    idx++;
                }
            }
        }

        sorted_particpants.removeAll(usedParticpants);
        Random rand = new Random();
//        int killerGroupId = rand.nextInt(groups);
        int killerGroupId = 1;
        IdxGiver idxG;
        if (killer_group_non_killers%2==0)
            idxG=new IdxGiver(true,groups,killerGroupId);
        else
            idxG=new IdxGiver(false,groups,killerGroupId);

        for( Participant p : sorted_particpants)
        {
            idx = idxG.getIdx();
            groupsParticpantsList.get(idx).add(p);
        }

        List<CompetitionGroup> outGroups = new LinkedList<>();
        for(int i=0;i<groups;i++)
        {
            if(i==killerGroupId)
            {
                continue;
            }
            outGroups.add(getGroupFromParticipants(groupsParticpantsList.get(i),round));
        }
        List<Fight> killerGroupFights =new ArrayList<>();
        for (int i=0;i<killer_group_non_killers;i++)
        {
            for(int k=i+1;k<killer_group_non_killers;k++)
            {
                killerGroupFights.add(new Fight(round,groupsParticpantsList.get(killerGroupId).get(i),groupsParticpantsList.get(killerGroupId).get(k)));
            }
        }
        List<Participant> notInKillerGroup = new ArrayList<>(participants);
        notInKillerGroup.removeAll(groupsParticpantsList.get(killerGroupId));
        List<List<Participant>> partnersForFight= strat.drawKiller(notInKillerGroup, killer_group_non_killers,groupSize-killer_group_non_killers);
        for(int i=0;i<killer_group_non_killers;i++)
        {
            Participant p = groupsParticpantsList.get(killerGroupId).get(i);
            for(Participant p1 : partnersForFight.get(i))
            {
                killerGroupFights.add(new Fight(round,p,p1));
            }
        }

        CompetitionGroup cg = new CompetitionGroup(killerGroupFights);
        outGroups.add(cg);
        cg.setGroupID("Killer Group");

        return outGroups;
    }

    private List<CompetitionGroup> generate_no_killers(Round round, int groupSize, List<Participant> participants)
    {
        int groups = participants.size()/groupSize;
        int killer_group_non_killers = participants.size()% groupSize;
        if (killer_group_non_killers > 0) groups++;
        List<List<Participant>> groupsParticpantsList= new ArrayList<>();
        for (int i=0;i<groups;i++)
        {
            groupsParticpantsList.add(new ArrayList<>());
        }
        List<Participant> participantsCopy = new ArrayList<>(participants);
        Collections.shuffle(participantsCopy);
        List<Participant> sorted_particpants = participantsCopy.stream().sorted(new Comparator<Participant>() {
            @Override
            public int compare(Participant o1, Participant o2) {
                return RationalNumber.compare(o1.getOldSeasonPointsForWeaponProperty(round.getMyWeaponCompetition().getWeaponType()).get(),
                        o2.getOldSeasonPointsForWeaponProperty(round.getMyWeaponCompetition().getWeaponType()).get());
            }
        }).collect(Collectors.toList());
        int idx=0;
        boolean fUp=true; // we got from 0 to (grouops -1) and from (groups -1) to 0
        for (Participant p : sorted_particpants)
        {
            groupsParticpantsList.get(idx).add(p);
            if(fUp)
            {
                idx++;
                if(idx >=groups)
                {
                    fUp=false;
                    idx--;
                }
            }
            else
            {
                idx--;
                if(idx < 0)
                {
                    fUp=true;
                    idx++;
                }
            }
        }

        List<CompetitionGroup> outGroups = new ArrayList<>();
        for(int i=0;i<groups;i++)
        {
            outGroups.add(getGroupFromParticipants(groupsParticpantsList.get(i),round));
        }
        return outGroups;
    }

    private  CompetitionGroup getGroupFromParticipants(List<Participant> parts, Round round)
    {
        List<Fight> fights = new ArrayList<>();
        for (int i = 0 ;i < parts.size();i++)
        {
            for(int k=i+1;k< parts.size();k++)
            {
                fights.add(new Fight(round,parts.get(i),parts.get(k)));
            }
        }
        return new CompetitionGroup(fights);
    }


    public class IdxGiver
    {
        private int idx;
        private int overIdx;
        private int denied;
        private int zeroIdx=0;
        private boolean fUp;
        public IdxGiver(boolean fStart, int overIdx,int denied)
        {
            this.denied=denied;
            this.overIdx=overIdx-1;
            if(fStart)
            {
                idx = zeroIdx;
                fUp=true;
            }
            else
            {
                idx = this.overIdx-1;
                fUp=false;
            }
        }

        public int getIdx()
        {
            int outIdx = idx;

            if(fUp)
            {
                if(idx== this.overIdx-1)
                    fUp=false;
                else
                {
                    idx ++;
                }
            }
            else
            {
                if(idx==zeroIdx)
                {
                    fUp=true;
                }
                else
                {
                    idx --;
                }
            }

            if (outIdx>=denied)
                outIdx++;
            return outIdx;
        }
    }
}
