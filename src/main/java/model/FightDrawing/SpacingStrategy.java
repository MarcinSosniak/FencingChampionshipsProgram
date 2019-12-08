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

        int idx = 0;
        int lastidx=idx;
        boolean fUp=true; // we got from 0 to grouops -1 and from groups -1 to 0
        for (Participant p : sorted_particpants)
        {
            groupsParticpantsList.get(idx).add(p);
            lastidx=idx;
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
                if(idx <= 0)
                {
                    fUp=true;
                    idx++;
                }
            }
        }

        List<CompetitionGroup> outGroups = new ArrayList<>();
        for(int i=0;i<groups;i++)
        {
            if(i!=lastidx)
            {

            }
        }






        return null;
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
}
