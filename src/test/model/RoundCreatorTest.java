package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FightDrawing.FightDrawStrategyPicker;
import model.command.Command;
import model.config.ConfigReader;
import model.enums.FightScore;
import model.enums.JudgeState;
import model.enums.WeaponType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import util.RationalNumber;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class RoundCreatorTest {

    private WeaponCompetition wc =null;
    private CommandStack csMock = null;
    private Round base_round = null;
    private ObservableList<Participant> wcInsideParticipantList= null;
    private Map<Participant,ObjectProperty<RationalNumber>> baseRoundInsideScore = null;
    private WeaponType wt=WeaponType.SMALL_SWORD;
    private ObservableList<Participant> baseRoundInsideParticipantsList = null;
    @Before
    public void prepare_test() throws Exception {
        ObservableList<Participant> oParts= FXCollections.observableArrayList();
        wc = new WeaponCompetition(wt,oParts);
        base_round = new Round(wc,  0, 0, new ArrayList<Participant>(),new FightDrawStrategyPicker(FightDrawStrategyPicker.STRATEGY_NAMES.FIRST), false, false);

        Field stackField = wc.getClass().getDeclaredField("cStack");
        Field roundsField = wc.getClass().getDeclaredField("rounds");
        Field participantsField  = wc.getClass().getDeclaredField("participants");
        Field pointField = base_round.getClass().getDeclaredField("roundScore");
        Field roundParticipantsField = base_round.getClass().getDeclaredField("participants");
        roundParticipantsField.setAccessible(true);
        pointField.setAccessible(true);
        participantsField.setAccessible(true);
        roundsField.setAccessible(true);
        stackField.setAccessible(true);

        baseRoundInsideParticipantsList = (ObservableList<Participant>) roundParticipantsField.get(base_round);

        baseRoundInsideScore = (Map<Participant,ObjectProperty<RationalNumber>>) pointField.get(base_round);


        wcInsideParticipantList = (ObservableList<Participant>) participantsField.get(wc);

        CommandStack cs = new CommandStack();
//        csMock = mock(CommandStack.class, Mockito.CALLS_REAL_METHODS);
        csMock = spy(cs);
        stackField.set(wc,csMock);
        List<Round> rounds_iniside_list = (List<Round>) roundsField.get(wc);
        rounds_iniside_list.add(base_round);
        Field cReader = ConfigReader.class.getDeclaredField("instance");
        ConfigReader crMock = mock(ConfigReader.class);
        cReader.setAccessible(true);
        cReader.set(null,crMock);
    }

    public void  addParticipant(String name, String surname, JudgeState state, RationalNumber points) throws Exception
    {
        Participant p = new Participant(name,surname,"nowhere","A",state, new Date(),0,0,0);
        if(wcInsideParticipantList.contains(p))
        {
            throw new IllegalStateException("cannot add duplicate of another p");
        }
        wcInsideParticipantList.add(p);
        baseRoundInsideParticipantsList.add(p);
        baseRoundInsideScore.put(p,new SimpleObjectProperty<>(points));

    }


    public void addParticipants(int[] points) throws Exception {
        String baseName = "n_";
        String baseSurname = "s_";
        for (int i = 0; i < points.length; i++)
        {
            addParticipant(baseName+Integer.toString(i), baseSurname+Integer.toString(i), JudgeState.NON_JUDGE,new RationalNumber(points[i]));
        }
    }


    @Test
    public void getParticpantsNeededFromPlayoffs() throws Exception {
        int points[] = {2,2,1,0,0};
        addParticipants(points);
        WeaponCompetition.RoundCreator rc=wc.prepareNewRound(3,3,false, FightDrawStrategyPicker.STRATEGY_NAMES.DEFAULT);
        assertEquals(true,rc.getfRoundReady());
    }

    @Test
    public void getParticpantsNeededFromPlayoffs_MorePlayers() throws Exception {
        int points[] = {2,2,2,1,1,0,0};
        addParticipants(points);
        WeaponCompetition.RoundCreator rc=wc.prepareNewRound(3,3,false, FightDrawStrategyPicker.STRATEGY_NAMES.DEFAULT);
        assertEquals(true,rc.getfRoundReady());
    }
    @Test
    public void getParticpantsNeededFromPlayoffs_PlayoffsNeeded() throws Exception {
        int points[] = {2,2,2,2,1,0,0};
        addParticipants(points);
        WeaponCompetition.RoundCreator rc=wc.prepareNewRound(3,3,false, FightDrawStrategyPicker.STRATEGY_NAMES.DEFAULT);
        assertEquals(false,rc.getfRoundReady());
    }

    @Test
    public void setPlayOffWinners() throws Exception {
        int points[] = {2,2,2,2};
        addParticipants(points);
        WeaponCompetition.RoundCreator rc=wc.prepareNewRound(3,3,false, FightDrawStrategyPicker.STRATEGY_NAMES.DEFAULT);
        List<Participant> playoffers= rc.getParticipantsForPlayoff();
        assertEquals(4,playoffers.size());
        List<Participant> playOfWinners = new ArrayList<>();
        Collections.addAll(playOfWinners,wcInsideParticipantList.get(0),wcInsideParticipantList.get(1),wcInsideParticipantList.get(2));
        // test
        rc.setPlayOffWinners(playOfWinners);
        assertEquals(true,rc.getfRoundReady());
        rc.startRound();
         // verify
        Round nr = wc.getLastRound();
        nr.getParticipants().removeAll(playoffers);
        assertEquals(0,nr.getParticipants().size());
    }

    @Test
    public void prepareFinalRoundTest() throws Exception{


        for (FightScore f1:FightScore.values()){
            for(FightScore f2:FightScore.values()){
                prepare_test();

                int[] points ={1,2,3,4};
                addParticipants(points);
                Field fSemiFinalField = base_round.getClass().getDeclaredField("fSemiFinal");
                fSemiFinalField.setAccessible(true);
                fSemiFinalField.set(base_round,true);


                if(f1.equals(FightScore.NO_POINTS) || f1.equals(FightScore.NULL_STATE) || f2.equals(FightScore.NO_POINTS) || f2.equals(FightScore.NULL_STATE) ){
                    continue;
                }
                Participant p1 = baseRoundInsideParticipantsList.get(0);
                Participant p2 = baseRoundInsideParticipantsList.get(1);
                Participant p3 = baseRoundInsideParticipantsList.get(2);
                Participant p4 = baseRoundInsideParticipantsList.get(3);


                Fight semiFinalFight1 = new Fight(base_round,baseRoundInsideParticipantsList.get(0),baseRoundInsideParticipantsList.get(1));
                Fight semiFinalFight2 = new Fight(base_round,baseRoundInsideParticipantsList.get(2),baseRoundInsideParticipantsList.get(3));
                Field score1 = semiFinalFight1.getClass().getDeclaredField("score");
                score1.setAccessible(true);
                ObjectProperty<FightScore> tmp = (ObjectProperty<FightScore>) score1.get(semiFinalFight1);
                tmp.setValue(f1);
                score1.set(semiFinalFight1,tmp);

                Field score2 = semiFinalFight2.getClass().getDeclaredField("score");
                score2.setAccessible(true);
                tmp = (ObjectProperty<FightScore>) score2.get(semiFinalFight2);
                tmp.setValue(f2);
                score2.set(semiFinalFight2,tmp);

                List<Fight> fights1 = new ArrayList<Fight>();
                List<Fight> fights2 = new ArrayList<Fight>();
                fights1.add(semiFinalFight1);
                fights2.add(semiFinalFight2);
                CompetitionGroup cg1 = new CompetitionGroup(fights1);
                CompetitionGroup cg2 = new CompetitionGroup(fights2);

                List<CompetitionGroup> groups = base_round.getGroups();

                groups.add(cg1);
                groups.add(cg2);

                WeaponCompetition.RoundCreator rc = wc.prepareNewRound(2,20,false, FightDrawStrategyPicker.STRATEGY_NAMES.FINAL);
                rc.startRound();

                Round r = wc.getLastRound();
                System.out.println(f1.toString());
                System.out.println(f2.toString());
                //assertTrue(r.getfFinal());
                List<CompetitionGroup> competitionGroups = r.getGroups();

                CompetitionGroup finalFirstPlaceGroup = null;
                CompetitionGroup finalThirdPlaceGroup = null;

                finalFirstPlaceGroup = competitionGroups.get(0);
                finalThirdPlaceGroup = competitionGroups.get(1);



                Fight finalFight = finalFirstPlaceGroup
                        .getFightsList()
                        .get(0);
                Fight thirdFight = finalThirdPlaceGroup.getFightsList().get(0);

                Participant pf1 = finalFight.getFirstParticipant();
                Participant pf2 = finalFight.getSecondParticipant();
                Participant pt1 = thirdFight.getFirstParticipant();
                Participant pt2 = thirdFight.getSecondParticipant();


                switch (f1){
                    case WON_FIRST:
                        switch (f2){
                            case WON_FIRST:
                                assertTrue(pf1.equals(p1) || pf1.equals(p3));
                                assertTrue(pf2.equals(p1) || pf2.equals(p3));
                                assertFalse(pf1.equals(pf2));

                                assertTrue(pt1.equals(p2) || pt1.equals(p4));
                                assertTrue(pt2.equals(p2) || pt2.equals(p4));
                                assertFalse(pt1.equals(pt2));
                                break;
                            case WON_SECOND:
                                assertTrue(pf1.equals(p1) || pf1.equals(p4));
                                assertTrue(pf2.equals(p1) || pf2.equals(p4));
                                assertFalse(pf1.equals(pf2));

                                assertTrue(pt1.equals(p2) || pt1.equals(p3));
                                assertTrue(pt2.equals(p2) || pt2.equals(p3));
                                assertFalse(pt1.equals(pt2));



                                break;
                            case DOUBLE:
                                assertTrue(pf1.equals(p1) || pf1.equals(p2));
                                assertTrue(pf2.equals(p1) || pf2.equals(p2));
                                assertFalse(pf1.equals(pf2));

                                assertTrue(pt1.equals(p3) || pt1.equals(p4));
                                assertTrue(pt2.equals(p3) || pt2.equals(p4));
                                assertFalse(pt1.equals(pt2));
                                break;

                        }
                        break;
                    case WON_SECOND:
                        switch (f2){
                            case WON_FIRST:
                                assertTrue(pf1.equals(p2) || pf1.equals(p3));
                                assertTrue(pf2.equals(p2) || pf2.equals(p3));
                                assertFalse(pf1.equals(pf2));

                                assertTrue(pt1.equals(p1) || pt1.equals(p4));
                                assertTrue(pt2.equals(p1) || pt2.equals(p4));
                                assertFalse(pt1.equals(pt2));
                                break;
                            case WON_SECOND:
                                assertTrue(pf1.equals(p2) || pf1.equals(p4));
                                assertTrue(pf2.equals(p2) || pf2.equals(p4));
                                assertFalse(pf1.equals(pf2));

                                assertTrue(pt1.equals(p1) || pt1.equals(p3));
                                assertTrue(pt2.equals(p1) || pt2.equals(p3));
                                assertFalse(pt1.equals(pt2));
                                break;
                            case DOUBLE:
                                assertEquals(r,base_round);
                                break;
                        }
                        break;
                    case DOUBLE:
                        switch (f2){
                            case WON_FIRST:
                            case WON_SECOND:
                            case DOUBLE:
                                assertEquals(r,base_round);
                                break;
                        }
                        break;
                }
            }
        }
    }


    @Test
    public void getParcipanntsForPlayOff() throws Exception{
        int points[] = {2,2,2,2};
        addParticipants(points);
        WeaponCompetition.RoundCreator rc=wc.prepareNewRound(3,3,false, FightDrawStrategyPicker.STRATEGY_NAMES.DEFAULT);
        List<Participant> playoffers= rc.getParticipantsForPlayoff();
        assertEquals(4,playoffers.size());
    }



}