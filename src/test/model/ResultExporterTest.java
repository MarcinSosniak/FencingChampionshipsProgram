package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.KillerDrawing.RandomKillerRandomizationStrategy;
import model.config.ConfigReader;
import model.enums.JudgeState;
import model.enums.WeaponType;
import model.resultsOperations.ResultExporter;
import org.junit.Before;
import org.junit.Test;
import util.RationalNumber;

import java.util.Date;

public class ResultExporterTest {

    Competition competition;
    private final String BASE_TEST_FILE= "cfg/default.cfg";

    @Before
    public void setUp() throws Exception {
        Participant p1 = new Participant("Marcin", "Kowalski", "KRK", "A", JudgeState.NON_JUDGE,new Date(),0,0,0);
        Participant p2 = new Participant("Paulina", "Nowak", "KRK", "B", JudgeState.NON_JUDGE,new Date(),0,0,0);
        Participant p3 = new Participant("Name3","Surname3","Location1","lacationGroup1", JudgeState.NON_JUDGE,new Date(),0,0,0);
        Participant p4 = new Participant("Name4","Surname4","Location2","lacationGroup2", JudgeState.NON_JUDGE,new Date(),0,0,0);
        Participant p5 = new Participant("Kot","Kiti","Location2","lacationGroup2", JudgeState.NON_JUDGE,new Date(),0,0,0);
        Participant p6 = new Participant("Mak","XD","Location2","lacationGroup2", JudgeState.NON_JUDGE,new Date(),0,0,0);
        Participant p7 = new Participant("yy","yy","Location2","lacationGroup2", JudgeState.NON_JUDGE,new Date(),0,0,0);
        Participant p8 = new Participant("aa","aa","Location2","lacationGroup2", JudgeState.NON_JUDGE,new Date(),0,0,0);


        ObservableList smallSwordParticipants = FXCollections.observableArrayList();
        smallSwordParticipants.add(p3); smallSwordParticipants.add(p4); smallSwordParticipants.add(p6);  smallSwordParticipants.add(p1); smallSwordParticipants.add(p2);
        smallSwordParticipants.add(p5);

        ObservableList rapierParticipants = FXCollections.observableArrayList();
        rapierParticipants.add(p1); rapierParticipants.add(p2);

        ConfigReader.init(BASE_TEST_FILE, BASE_TEST_FILE);

        Competition.init(
                new util.Pair<ObservableList<Participant>, WeaponType>(rapierParticipants, WeaponType.RAPIER),
                new util.Pair<ObservableList<Participant>,WeaponType>(FXCollections.observableArrayList(),WeaponType.SABRE),
                new util.Pair<ObservableList<Participant>,WeaponType>(smallSwordParticipants,WeaponType.SMALL_SWORD),
                new RandomKillerRandomizationStrategy(), "");
        Competition.getInstance().getParticipants().add(p7);
        Competition.getInstance().getParticipants().add(p8);
        Competition.getInstance().setCompetitionName("SOME_NAME");

        p1.getParticipantResult().getSmallSwordResults().setPlace(1);
        p1.getParticipantResult().getSmallSwordResults().setPoints(new RationalNumber(100));
        p1.getParticipantResult().getRapierResults().setPlace(1);
        p1.getParticipantResult().getRapierResults().setPoints(new RationalNumber(100));

        p2.getParticipantResult().getSmallSwordResults().setPlace(2);
        p2.getParticipantResult().getSmallSwordResults().setPoints(new RationalNumber(99));
        p2.getParticipantResult().getRapierResults().setPlace(2);
        p2.getParticipantResult().getRapierResults().setPoints(new RationalNumber(99));

        p3.getParticipantResult().getSmallSwordResults().setPlace(3);
        p3.getParticipantResult().getSmallSwordResults().setPoints(new RationalNumber(98));

        p4.getParticipantResult().getSmallSwordResults().setPlace(4);
        p4.getParticipantResult().getSmallSwordResults().setPoints(new RationalNumber(97));

        p5.getParticipantResult().getSmallSwordResults().setPlace(5);
        p5.getParticipantResult().getSmallSwordResults().setPoints(new RationalNumber(96));

        p6.getParticipantResult().getSmallSwordResults().setPlace(6);
        p6.getParticipantResult().getSmallSwordResults().setPoints(new RationalNumber(95));

        p1.getParticipantResult().calculateTriathlonPoints(); p2.getParticipantResult().calculateTriathlonPoints();
        p3.getParticipantResult().calculateTriathlonPoints(); p4.getParticipantResult().calculateTriathlonPoints();
        p5.getParticipantResult().calculateTriathlonPoints(); p6.getParticipantResult().calculateTriathlonPoints();


        int currentPlace = -1;
        int lastPoints = 10000;
        int iteration = 0;

        for(Participant p: Competition.getInstance().getParticipants()){
            iteration ++;

            if(lastPoints > p.getParticipantResult().getTriathlonOpenPoints()){
                currentPlace = iteration;
            }
            lastPoints = p.getParticipantResult().getTriathlonOpenPoints();
            p.getParticipantResult().setTriathlonOpen(Integer.toString(currentPlace));
        }
    }


    @Test
    public void exportResultsTest() {
        ResultExporter.exportResults();

    }
}