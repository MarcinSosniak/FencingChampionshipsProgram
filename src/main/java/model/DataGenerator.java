package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.enums.JudgeState;
import model.enums.WeaponType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataGenerator {

    static public ObservableList<Participant> generateParticipants(int xD){
        ObservableList<Participant> participants =  FXCollections.observableArrayList();
        Participant p = new Participant("Judge"+xD,"Surname","Location","lacationGroup", JudgeState.MAIN_JUDGE,new Date());
        participants.add(p);
        participants.add(new Participant("Name1"+xD,"Surname1","Location1","lacationGroup1", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name2"+xD,"Surname2","Location2","lacationGroup2", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name3"+xD,"Surname3","Location3","lacationGroup3", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name4"+xD,"Surname4","Location4","lacationGroup4", JudgeState.NON_JUDGE,new Date()));
        return participants;
    }


    static public ObservableList<Participant> generateWeaponParticipants(WeaponType wt,int xD){
        ObservableList<Participant> participants =  FXCollections.observableArrayList();
        participants.add(new Participant("Judge"+xD,"Surname","Location","lacationGroup", JudgeState.MAIN_JUDGE,new Date()));
        participants.add(new Participant("Name1"+xD,"Surname1","Location1","lacationGroup1", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name2"+xD,"Surname2","Location2","lacationGroup2", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name3"+xD,"Surname3","Location3","lacationGroup3", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name4"+xD,"Surname4","Location4","lacationGroup4", JudgeState.NON_JUDGE,new Date()));

        for(Participant x : participants){
            x.setfWeaponParticipant(wt,true);
        }

        return participants;
    }


    /** TODO: make it more complicated */
    static public Competition generateSampleCompetition(){
        ObservableList rapierParticipants = DataGenerator.generateWeaponParticipants(WeaponType.RAPIER,1);
        ObservableList sabreParticipants = DataGenerator.generateWeaponParticipants(WeaponType.SABRE,2);
        ObservableList smallSwordParticipants = DataGenerator.generateWeaponParticipants(WeaponType.SMALL_SWORD,3);

        ObservableList wholeParticipants = FXCollections.observableArrayList();
        wholeParticipants.addAll(rapierParticipants);
        wholeParticipants.addAll(sabreParticipants);
        wholeParticipants.addAll(smallSwordParticipants);

        WeaponCompetition wcSabre = new WeaponCompetition(WeaponType.SABRE,sabreParticipants);
        WeaponCompetition wcRapier = new WeaponCompetition(WeaponType.RAPIER,rapierParticipants);
        WeaponCompetition wcSmallSwaord = new WeaponCompetition(WeaponType.SMALL_SWORD,smallSwordParticipants);

        ObservableList<WeaponCompetition> lwc = FXCollections.observableArrayList();
        lwc.addAll(wcRapier,wcSabre,wcSmallSwaord);

        return new Competition(lwc,wholeParticipants,new DummyKillerRandomizationStartegy());
    }



}
