package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.enums.WeaponType;
import java.util.List;

public class WeaponCompetition {

    private WeaponType weaponType;
    private ObservableList<Participant> participants;
    private ObservableList<Round> rounds;
    private ObservableList<CompetitionGroup> competitionGroups;

    public WeaponCompetition(WeaponType weaponType, ObservableList<Participant> participants){
        this.weaponType = weaponType;
        this.participants = participants;
        this.rounds = FXCollections.observableArrayList();
        this.competitionGroups = FXCollections.observableArrayList();
    }

    public void addParticipantToWeaponCompetition (Participant participant){ participants.add(participant); }

    public void removeParticipantFromWeaponCompetition (Participant participant){ participants.remove(participant); }

    public void addRound(Round round){ rounds.add(round); }

    public String groupForParticipant(Participant p){
        return "a";
    }

    public WeaponType getWeaponType(){
        return this.weaponType;
    }

    public List<Participant> getParticipants(){
        return participants;
    }
}