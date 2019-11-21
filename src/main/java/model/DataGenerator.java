package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.KillerDrawing.RandomKillerRandomizationStrategy;
import model.enums.JudgeState;
import model.enums.WeaponType;

import java.util.Date;
import java.util.LinkedList;
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
        participants.add(new Participant("Judge2"+xD,"Surname","Location","lacationGroup", JudgeState.MAIN_JUDGE,new Date()));
        participants.add(new Participant("Name12"+xD,"Surname1","Location1","lacationGroup1", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name22"+xD,"Surname2","Location2","lacationGroup2", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name32"+xD,"Surname3","Location3","lacationGroup3", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name42"+xD,"Surname4","Location4","lacationGroup4", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Judge3"+xD,"Surname","Location","lacationGroup", JudgeState.MAIN_JUDGE,new Date()));
        participants.add(new Participant("Name13"+xD,"Surname1","Location1","lacationGroup1", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name23"+xD,"Surname2","Location2","lacationGroup2", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name33"+xD,"Surname3","Location3","lacationGroup3", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name43"+xD,"Surname4","Location4","lacationGroup4", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Judge5"+xD,"Surname","Location","lacationGroup", JudgeState.MAIN_JUDGE,new Date()));
        participants.add(new Participant("Name15"+xD,"Surname1","Location1","lacationGroup1", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name25"+xD,"Surname2","Location2","lacationGroup2", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name35"+xD,"Surname3","Location3","lacationGroup3", JudgeState.NON_JUDGE,new Date()));
        participants.add(new Participant("Name45"+xD,"Surname4","Location4","lacationGroup4", JudgeState.NON_JUDGE,new Date()));

        for(Participant x : participants){
            x.setfWeaponParticipant(wt,true);
        }

        return participants;
    }


    /** TODO: make it more complicated */
    static public Competition generateSampleCompetition(){
        ObservableList rapierParticipants = DataGenerator.generateWeaponParticipants(WeaponType.RAPIER,11);
        rapierParticipants.addAll(DataGenerator.generateWeaponParticipants(WeaponType.RAPIER,12));

        ObservableList sabreParticipants = DataGenerator.generateWeaponParticipants(WeaponType.SABRE,21);
        sabreParticipants.addAll(DataGenerator.generateWeaponParticipants(WeaponType.SABRE,22));

        ObservableList smallSwordParticipants = DataGenerator.generateWeaponParticipants(WeaponType.SMALL_SWORD,31);
        smallSwordParticipants.addAll(DataGenerator.generateWeaponParticipants(WeaponType.SMALL_SWORD,32));

        Competition comp = Competition.init(new util.Pair<ObservableList<Participant>,WeaponType>(rapierParticipants,WeaponType.RAPIER),
                                new util.Pair<ObservableList<Participant>,WeaponType>(sabreParticipants,WeaponType.SABRE),
                                new util.Pair<ObservableList<Participant>,WeaponType>(smallSwordParticipants,WeaponType.SMALL_SWORD),
                                new RandomKillerRandomizationStrategy(), "");
        for(WeaponCompetition wc :  comp.getWeaponCompetitions())
        {
            wc.startFirstRound();
        }
        System.out.println("hue");
        for ( CompetitionGroup g: comp.getWeaponCompetitions().get(0).getLastRound().getGroups())
        {
            System.out.println("Size of group: " + g.getFightsList().size());
        }
        return comp;
    }




}
