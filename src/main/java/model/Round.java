package model;
import util.RationalNumber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.FightDrawing.FightDrawStrategy;
import model.FightDrawing.FightDrawStrategyPicker;
import model.KillerDrawing.KillerRandomizerStrategyPicker;

import java.util.ArrayList;
import java.util.Map;

public class Round {

    private int roundNumber;
    private int groupSize;
    // last cut-off
    private FightDrawStrategy fightDrawStrategy;
    private ObservableList<CompetitionGroup> groups=null;
    private ObservableList<Participant> participants;
    private ObservableMap<Participant, RationalNumber> roundScore = FXCollections.observableHashMap();
    private Map<Participant,Integer> participantFightNumber;
    private int participantExcpectedFightNumber; // size of group -1

    public WeaponCompetition getMyWeaponCompetition() {
        return myWeaponCompetition;
    }

    public Round setMyWeaponCompetition(WeaponCompetition myWeaponCompetition) {
        this.myWeaponCompetition = myWeaponCompetition;
        return this;
    }

    private  WeaponCompetition myWeaponCompetition= null;

    public ObservableList<Participant> getParticipants() {
        return participants;
    }


    public ObservableList<CompetitionGroup> getGroups() {
        return groups;
    }

    public Round(int roundNumber, int groupSize,ArrayList<Participant> participants, FightDrawStrategyPicker fightDrawStrategyPicker){
        this.roundNumber = roundNumber;
        this.groupSize = groupSize;
        this.participantExcpectedFightNumber=groupSize-1;
        this.fightDrawStrategy = fightDrawStrategyPicker.pick(KillerRandomizerStrategyPicker.KillerRandomizerStrategy());
        this.participants= FXCollections.observableArrayList(participants);
        for(Participant p : participants)
        {
            participantFightNumber.put(p,0);
            roundScore.put(p,new RationalNumber(0));
        }
    }

    public CommandStack getCStack() {return getMyWeaponCompetition().getcStack();}

    public int getGroupSize() { return groupSize; }

    public int getRoundNumber() { return roundNumber; }

    public Round drawGroups()
    {
        groups = FXCollections.observableArrayList(fightDrawStrategy.drawFightsForRound(this,groupSize,participants));
        return this;
    }

    public void addExcpectedFightToParticipant(Participant p)
    {
        Integer current =participantFightNumber.get(p);
        participantFightNumber.put(p,current+1);
    }

    public void addPointsFromFight(Participant p, int points)
    {
        RationalNumber pScoreMultiplier = new RationalNumber(participantExcpectedFightNumber,participantFightNumber.get(p));
        roundScore.put(p,roundScore.get(p).multiply(pScoreMultiplier.multiply(points)));

    }
    //count points
}