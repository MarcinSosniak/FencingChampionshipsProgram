package model;

import java.util.List;

public abstract class FightDrawStrategy {

    abstract List<Fight> drawFightsForRound(Round round);
    // avoiding same battles
}