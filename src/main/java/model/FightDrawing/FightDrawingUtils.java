package model.FightDrawing;

import model.CompetitionGroup;
import model.Fight;
import model.KillerDrawing.KillerRandomizerStrategy;
import model.Participant;
import model.Round;

import java.util.ArrayList;
import java.util.List;

public abstract class FightDrawingUtils {

    public static List<CompetitionGroup> drawFightsForSortedParticipants(
            Round round,
            int groupSize,
            List<Participant> participants,
            KillerRandomizerStrategy killS
    ) {
        int groupCount;
        List<CompetitionGroup> out=new ArrayList<>();
        int overPariticpants=participants.size()%groupSize;
        groupCount=participants.size()/groupSize;

        int participantsId;
        for( participantsId=0;participantsId<groupCount;participantsId++)
        {
            List<Participant> subList= participants.subList(participantsId*groupSize,(participantsId+1)*groupSize);
            List<Fight> fights = new ArrayList<>();
            for(int k=0;k<subList.size();k++)
                for(int m=k+1;m<subList.size();m++)
                {
                    fights.add(new Fight(round,subList.get(k),subList.get(m)));
                }
            out.add(new CompetitionGroup(fights));
        }
        if(overPariticpants!=0)
        {
            List<Participant> nonKillers=participants.subList(participants.size()-overPariticpants,participants.size());
            List<Participant> eligbleKillers= participants.subList(0,participants.size()-overPariticpants);
            List<List<Participant>> killersForParticipant = killS.drawKiller(eligbleKillers,
                    participants.size()%groupSize,
                    groupSize-(participants.size()%groupSize));
            List<Fight> fights = new ArrayList<>();
            for(int k=0;k<nonKillers.size();k++)
                for(int m=k+1;m<nonKillers.size();m++)
                {
                    fights.add(new Fight(round,nonKillers.get(k),nonKillers.get(m)));
                }
            int currentNonKillerId=0;
            /**
             * make tests. like if killersForPartipant return good amount of killers
             */
            for( List<Participant> killersForSingle : killersForParticipant) {
                for (Participant killer : killersForSingle) {
                    fights.add(new Fight(round,nonKillers.get(currentNonKillerId),killer));
//                    round.addExcpectedFightToParticipant(killer);
                }
                currentNonKillerId++;
            }
            CompetitionGroup cg = new CompetitionGroup(fights);
            cg.setGroupID("Killer Group");
            out.add(cg);
        }
        return out;
    }

    public static List<List<Participant>> toFullGroups(List<Participant> participants, int groupSize) {
        int fullGroupCount = (int) (participants.size() / groupSize);
        List<List<Participant>> groups = new ArrayList<List<Participant>>();
        int participantIdx = 0;
        for (int i = 0; i < fullGroupCount; ++i) {
            List<Participant> group = new ArrayList<>();
            for (int k = 0; k < groupSize; ++k) {
                group.add(participants.get(participantIdx));
                ++participantIdx;
            }
            groups.add(group);
        }
        return groups;
    }

    public static List<List<Participant>> toAllGroups(List<Participant> participants, int groupSize) {
        List<List<Participant>> groups = new ArrayList<List<Participant>>();
        int participantIdx = 0;
        for (int i = 0; participantIdx < participants.size(); ++i) {
            List<Participant> group = new ArrayList<>();
            for (int k = 0; k < groupSize && participantIdx< participants.size(); ++k) {
                group.add(participants.get(participantIdx));
                ++participantIdx;
            }
            groups.add(group);
        }
        return groups;
    }
}
