package model.FightDrawing;

import model.CompetitionGroup;
import model.Fight;
import model.KillerDrawing.KillerRandomizerStrategy;
import model.Participant;
import model.Round;

import java.util.ArrayList;
import java.util.List;

public abstract class FightDrawStrategy {

    protected KillerRandomizerStrategy killerStrat;
    public abstract List<CompetitionGroup> drawFightsForRound(int groupSize, List<Participant> participants);
    // avoiding same battles
}