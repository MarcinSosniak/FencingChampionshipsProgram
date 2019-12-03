package model.FightDrawing;

import model.CompetitionGroup;
import model.Fight;
import model.Participant;
import model.Round;

import java.util.ArrayList;
import java.util.List;

public class FinalStrategy extends FightDrawStrategy {

    @Override
    public List<CompetitionGroup> drawFightsForRound(Round round, int groupSize, List<Participant> participants) {
        Fight f1 = new Fight(round,participants.get(0),participants.get(1));
        Fight f2 = new Fight(round,participants.get(2),participants.get(3));
        List<Fight> fights = new ArrayList<>();
        List<Fight> fights2 = new ArrayList<>();
        fights.add(f1);
        fights2.add(f2);
        CompetitionGroup cg1 = new CompetitionGroup(fights);
        CompetitionGroup cg2 = new CompetitionGroup(fights2);
        List<CompetitionGroup> competitionGroups = new ArrayList<>();
        competitionGroups.add(cg1);
        competitionGroups.add(cg2);
        return competitionGroups;
    }
}
