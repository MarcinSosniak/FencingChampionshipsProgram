package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.enums.JudgeState;

import java.util.Date;

public class DataGenerator {

    public ObservableList<Participant> generateParticipants(){
        ObservableList<Participant> participants =  FXCollections.observableArrayList();
        Participant p = new Participant("Judge","Surname","Location","lacationGroup", JudgeState.MAIN_JUDGE,new Date());
        participants.add(p);
        participants.add(new Participant("Name1","Surname1","Location1","lacationGroup1", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name2","Surname2","Location2","lacationGroup2", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name3","Surname3","Location3","lacationGroup3", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name4","Surname4","Location4","lacationGroup4", JudgeState.NON_JUDGE,new Date()));



        return participants;
    }




}
