package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FightDrawing.FightDrawStrategyPicker;
import model.command.Command;
import model.config.ConfigReader;
import model.enums.JudgeState;
import model.enums.WeaponType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.RationalNumber;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

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

        csMock = mock(CommandStack.class);
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
    public void getParcipanntsForPlayOff() throws Exception{
        int points[] = {2,2,2,2};
        addParticipants(points);
        WeaponCompetition.RoundCreator rc=wc.prepareNewRound(3,3,false, FightDrawStrategyPicker.STRATEGY_NAMES.DEFAULT);
        List<Participant> playoffers= rc.getParticipantsForPlayoff();
        assertEquals(4,playoffers.size());
    }



}