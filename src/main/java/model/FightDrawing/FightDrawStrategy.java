package model.FightDrawing;

import model.*;
import model.KillerDrawing.KillerRandomizerStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class FightDrawStrategy implements Serializable {

    protected KillerRandomizerStrategy killerStrat;
    public abstract List<CompetitionGroup> drawFightsForRound(Round round, int groupSize, List<Participant> participants);
    // avoiding same battles
}