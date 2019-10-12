package model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FightDrawing.FightDrawStrategy;
import model.FightDrawing.FightDrawStrategyPicker;
import model.KillerDrawing.KillerRandomizerStrategyPicker;

import java.util.ArrayList;
import java.util.List;

public class Round {

    private int roundNumber;
    private int groupSize;
    // last cut-off
    private FightDrawStrategy fightDrawStrategy;
    private ObservableList<CompetitionGroup> groups=null;
    private ObservableList<Participant> participants;

    public ObservableList<Participant> getParticipants() {
        return participants;
    }


    public ObservableList<CompetitionGroup> getGroups() {
        return groups;
    }

    public Round(int roundNumber, int groupSize,ArrayList<Participant> participants, FightDrawStrategyPicker fightDrawStrategyPicker){
        this.roundNumber = roundNumber;
        this.groupSize = groupSize;
        this.fightDrawStrategy = fightDrawStrategyPicker.pick(KillerRandomizerStrategyPicker.KillerRandomizerStrategy());
        this.participants= FXCollections.observableArrayList(participants);
        sortGroups();
    }

    public int getGroupSize() { return groupSize; }

    public int getRoundNumber() { return roundNumber; }

    private void sortGroups()
    {
        groups = FXCollections.observableArrayList(fightDrawStrategy.drawFightsForRound(groupSize,participants));
    }
    //count points
}