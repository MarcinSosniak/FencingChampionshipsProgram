package model.FightDrawing;

import model.CompetitionGroup;
import model.Participant;
import model.Round;

import java.util.ArrayList;
import java.util.List;

public class FirstRoundStrategy extends FightDrawStrategy {
    @Override
    public List<CompetitionGroup> drawFightsForRound(Round round, int groupSize, List<Participant> participants) {
        return new ArrayList<>();
    }
}
