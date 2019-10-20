package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.KillerDrawing.RandomKillerRandomizationStrategy;
import model.enums.JudgeState;
import model.enums.WeaponType;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DataGenerator {

    static public ObservableList<Participant> generateParticipants(int xD) throws ParseException {
        ObservableList<Participant> participants =  FXCollections.observableArrayList();
        Participant p = new Participant("Judge"+xD,"Surname","Location","lacationGroup", "MAIN_JUDGE", "00-00-0000");
        participants.add(p);
        participants.add(new Participant("Name1"+xD,"Surname1","Location1","lacationGroup1", "NON_JUDGE", "00-00-0000"));
        participants.add(new Participant("Name2"+xD,"Surname2","Location2","lacationGroup2", "NON_JUDGE", "00-00-0000"));
        participants.add(new Participant("Name3"+xD,"Surname3","Location3","lacationGroup3","NON_JUDGE", "00-00-0000"));
        participants.add(new Participant("Name4"+xD,"Surname4","Location4","lacationGroup4", "NON_JUDGE", "00-00-0000"));
        return participants;
    }


    static public ObservableList<Participant> generateWeaponParticipants(WeaponType wt,int xD) throws ParseException {
        ObservableList<Participant> participants =  FXCollections.observableArrayList();
        participants.add(new Participant("Judge"+xD,"Surname","Location","lacationGroup","MAIN_JUDGE", "00-00-0000"));
        participants.add(new Participant("Name1"+xD,"Surname1","Location1","lacationGroup1", "MAIN_JUDGE", "00-00-0000"));
        participants.add(new Participant("Name2"+xD,"Surname2","Location2","lacationGroup2", "MAIN_JUDGE", "00-00-0000"));
        participants.add(new Participant("Name3"+xD,"Surname3","Location3","lacationGroup3", "MAIN_JUDGE", "00-00-0000"));
        participants.add(new Participant("Name4"+xD,"Surname4","Location4","lacationGroup4", "MAIN_JUDGE", "00-00-0000"));

        for(Participant x : participants){
            x.setfWeaponParticipant(wt,true);
        }

        return participants;
    }


    /** TODO: make it more complicated */
    static public Competition generateSampleCompetition() throws ParseException {
        ObservableList rapierParticipants = DataGenerator.generateWeaponParticipants(WeaponType.RAPIER,1);
        ObservableList sabreParticipants = DataGenerator.generateWeaponParticipants(WeaponType.SABRE,2);
        ObservableList smallSwordParticipants = DataGenerator.generateWeaponParticipants(WeaponType.SMALL_SWORD,3);

        Competition comp = Competition.init(new util.Pair<ObservableList<Participant>,WeaponType>(rapierParticipants,WeaponType.RAPIER),
                                new util.Pair<ObservableList<Participant>,WeaponType>(sabreParticipants,WeaponType.SABRE),
                                new util.Pair<ObservableList<Participant>,WeaponType>(smallSwordParticipants,WeaponType.SMALL_SWORD),
                                new RandomKillerRandomizationStrategy());
        for(WeaponCompetition wc :  comp.getWeaponCompetitions())
        {
            wc.startFirstRound();
        }
        return comp;
    }




}
