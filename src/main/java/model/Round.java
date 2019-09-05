package model;
import java.util.ArrayList;
import java.util.List;

public class Round {

    private int roundNumber;
    private int groupSize;
    // last cut-off
    private FightDrawStrategy fightDrawStrategy;
    private List<Fight> fights;

    public Round(int roundNumber, int groupSize, FightDrawStrategy fightDrawStrategy){
        this.roundNumber = roundNumber;
        this.groupSize = groupSize;
        this.fightDrawStrategy = fightDrawStrategy;
        this.fights = new ArrayList<>();
    }

    public int getGroupSize() { return groupSize; }

    public int getRoundNumber() { return roundNumber; }

    public void addFightToRound(Fight fight) {fights.add(fight); }

    public void drawFightsForRound(){
        List<Fight> fights = fightDrawStrategy.drawFightsForRound(this);
        for (Fight fight: fights) addFightToRound(fight);
    }

    //count points
}