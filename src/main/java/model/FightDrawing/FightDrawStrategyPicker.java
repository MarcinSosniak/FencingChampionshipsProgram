package model.FightDrawing;

import model.CompetitionGroup;
import model.Fight;
import model.KillerDrawing.KillerRandomizerStrategy;
import model.Participant;
import model.Round;

import java.util.List;

public class FightDrawStrategyPicker {

    public FightDrawStrategy pick(KillerRandomizerStrategy strat)
    {
        return new RandomDrawStrategy(strat);
    }
}
