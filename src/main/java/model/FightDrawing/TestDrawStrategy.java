package model.FightDrawing;

import model.CompetitionGroup;
import model.Fight;
import model.KillerDrawing.KillerRandomizerStrategy;
import model.Participant;

import java.util.ArrayList;
import java.util.List;

public class TestDrawStrategy extends FightDrawStrategy {
    @Override
    public ArrayList<CompetitionGroup> drawFightsForRound(int groupSize, List<Participant> participants) {
        if(participants.size() % groupSize != 0)
            throw new IllegalStateException("ola boga killerzy to za skompilkowane dla tego");
        ArrayList<CompetitionGroup> out=new ArrayList<>();
        for(int i=0;i<participants.size();i+= groupSize)
        {
            ArrayList<Participant> groupParticipants=new ArrayList<>(participants.subList(i*groupSize,(i+1)*groupSize));
            ArrayList<Fight> fightsInGroup= new ArrayList<>();
            for(int k =0; k<groupParticipants.size();k++)
            {
                for(int m=k+1;m<groupParticipants.size();m++)
                {
                    fightsInGroup.add(new Fight(groupParticipants.get(k),groupParticipants.get(m)));
                }
            }
            out.add(new CompetitionGroup(fightsInGroup));
        }

        return out;
    }

    TestDrawStrategy(KillerRandomizerStrategy killStrat)
    {
        this.killerStrat=killStrat;
    }
}
