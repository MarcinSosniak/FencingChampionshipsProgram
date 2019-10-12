package model.FightDrawing;

import model.*;
import model.KillerDrawing.KillerRandomizerStrategy;

import java.util.ArrayList;
import java.util.List;

public abstract class FightDrawStrategy {

    protected KillerRandomizerStrategy killerStrat;
    public abstract List<CompetitionGroup> drawFightsForRound(Round round, int groupSize, List<Participant> participants);
    // avoiding same battles
}