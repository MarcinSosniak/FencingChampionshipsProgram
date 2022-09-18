package model.FightDrawing;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.*;
import model.KillerDrawing.RandomKillerRandomizationStrategy;
import model.enums.JudgeState;
import model.enums.WeaponType;
import net.bytebuddy.matcher.CollectionErasureMatcher;
import org.junit.Test;
import org.mockito.Mockito;
import util.RationalNumber;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpacingStrategyTest {
    @Test
    public void drawFightsForRoundNoKillers() throws Exception {
        //prepare
        List<Participant> participants=new ArrayList<>();
        int PARTICIPANTS_COUNT = 10;
        for(int i=0;i<PARTICIPANTS_COUNT;i++)
        {
            Participant p = mock(Participant.class);
            doReturn(new SimpleStringProperty("n"+Integer.toString(i))).when(p).nameProperty();
            doReturn(new SimpleStringProperty("s"+Integer.toString(i))).when(p).surnameProperty();
            Mockito.doReturn(new SimpleObjectProperty<>(new RationalNumber(i))).when(p).getOldSeasonPointsForWeaponProperty(any());
            participants.add(p);
        }
        WeaponCompetition wc = Mockito.mock(WeaponCompetition.class);
        Round r = Mockito.mock(Round.class);
        doReturn(wc).when(r).getMyWeaponCompetition();
        doReturn(WeaponType.SMALL_SWORD).when(wc).getWeaponType();

        SpacingStrategy s = new SpacingStrategy(new RandomKillerRandomizationStrategy());
        //test
        List<CompetitionGroup> cg = s.drawFightsForRoundWithZeroedGroupId(r,5,participants);

        //after
        List<List<Participant>> cgPart = new ArrayList<>();

        for (CompetitionGroup g : cg)
        {
            List<Participant> gp = new ArrayList<>();
            for (Fight f : g.getFightsList())
            {
                if(!gp.contains(f.getFirstParticipant()))
                    gp.add(f.getFirstParticipant());
                if(!gp.contains(f.getSecondParticipant()))
                    gp.add(f.getSecondParticipant());
            }
            cgPart.add(gp);
        }
        for(List<Participant> cgp : cgPart)
        {
            System.out.println("\n\n");
            for(Participant p : cgp)
            {
                System.out.println(p.getOldSeasonPointsForWeaponProperty(WeaponType.SMALL_SWORD));
            }
        }

        List<Integer> pointsForGroup = new ArrayList<>();
        for(List<Participant> cgp : cgPart)
        {
            Integer sum = 0;
            System.out.println("\n\n");
            for(Participant p : cgp)
            {
                sum+=Integer.valueOf(p.getOldSeasonPointsForWeaponProperty(WeaponType.SMALL_SWORD).get().toString());
            }
            pointsForGroup.add(sum);
        }
        //Asuumes tat they have poins as their postiions, one by one.
        Double avg =0.;
        for (Integer i : pointsForGroup)
        {
            avg+=i;
        }
        avg /= pointsForGroup.size();
        for(Integer i : pointsForGroup)
        {
            if( 1.2*avg < i)
            {
                System.out.println(Double.toString(1.2*avg) + " < "+ Integer.toString(i));
                fail();
            }
            if(0.8 *avg > i)
            {
                System.out.println(Double.toString(0.8*avg) + " < "+ Integer.toString(i));
                fail();
            }

        }
    }

    @Test
    public void drawFightsForRound_precise() throws Exception
    {
        List<Participant> participants = new ArrayList<>();
        final int PARTICIPANTS_COUNT = 6; // DO NOT CHANGE
        final int GROUP_SIZE = 3;//DONOT CHANGE
        for (int i = 0; i < PARTICIPANTS_COUNT; i++) {
            Participant p = mock(Participant.class);
            doReturn(new SimpleStringProperty("n" + Integer.toString( i))).when(p).nameProperty();
            doReturn(new SimpleStringProperty("s" + Integer.toString( i))).when(p).surnameProperty();
            Mockito.doReturn(new SimpleObjectProperty<>(new RationalNumber(PARTICIPANTS_COUNT-i))).when(p).getOldSeasonPointsForWeaponProperty(any());
            participants.add(p);
        }
        WeaponCompetition wc = Mockito.mock(WeaponCompetition.class);
        Round r = Mockito.mock(Round.class);
        doReturn(wc).when(r).getMyWeaponCompetition();
        doReturn(WeaponType.SMALL_SWORD).when(wc).getWeaponType();

        SpacingStrategy s = new SpacingStrategy(new RandomKillerRandomizationStrategy());
        //test
        List<CompetitionGroup> cgs = s.drawFightsForRoundWithZeroedGroupId(r, GROUP_SIZE, participants);
        //verify
        /*
        | Group 0  | Group 1  |
        +----------+----------+
        |    p0    |   p1     |
        +----------+----------+
        |    p3    |   p2     |
        +----------+----------+
        |    p4    |   p5     |
        +----------+----------+
        */

        //GROUP 0
        CompetitionGroup cg = cgs.get(1);
        List<Participant> participantsToVerify  = new ArrayList<>();
        Collections.addAll(participantsToVerify,participants.get(0),participants.get(3),participants.get(4));
        for (Fight f: cg.getFightsList())
        {
            assertEquals(true,participantsToVerify.contains(f.getFirstParticipant()));
            assertEquals(true,participantsToVerify.contains(f.getSecondParticipant()));
        }

        System.out.println("\n\n");
        //GROUP 1
        cg=cgs.get(0);
        participantsToVerify.clear();
        Collections.addAll(participantsToVerify,participants.get(1),participants.get(2),participants.get(5));
        for (Fight f: cg.getFightsList())
        {
            assertEquals(true,participantsToVerify.contains(f.getFirstParticipant()));
            assertEquals(true,participantsToVerify.contains(f.getSecondParticipant()));
        }
    }





    @Test
    public void drawFightsForRoundKillers() throws Exception {
        //prepare
        List<Participant> participants = new ArrayList<>();
        int PARTICIPANTS_COUNT = 14;
        int GROUP_SIZE = 3;
        int N = PARTICIPANTS_COUNT/GROUP_SIZE;
        if (PARTICIPANTS_COUNT%GROUP_SIZE != 0)
        {
            N++;
        }
        for (int i = 0; i < PARTICIPANTS_COUNT; i++) {
            Participant p = mock(Participant.class);
            doReturn(new SimpleStringProperty("n" + Integer.toString( PARTICIPANTS_COUNT-i))).when(p).nameProperty();
            doReturn(new SimpleStringProperty("s" + Integer.toString( PARTICIPANTS_COUNT-i))).when(p).surnameProperty();
            if(i<N)
                doReturn(JudgeState.MAIN_JUDGE).when(p).getJudgeState();
            else
                doReturn(JudgeState.NON_JUDGE).when(p).getJudgeState();
            Mockito.doReturn(new SimpleObjectProperty<>(new RationalNumber(PARTICIPANTS_COUNT-i))).when(p).getOldSeasonPointsForWeaponProperty(any());
            participants.add(p);
        }
        WeaponCompetition wc = Mockito.mock(WeaponCompetition.class);
        Round r = Mockito.mock(Round.class);
        doReturn(wc).when(r).getMyWeaponCompetition();
        doReturn(WeaponType.SMALL_SWORD).when(wc).getWeaponType();

        SpacingStrategy s = new SpacingStrategy(new RandomKillerRandomizationStrategy());
        //test
        List<CompetitionGroup> cgs = s.drawFightsForRoundWithZeroedGroupId(r, GROUP_SIZE, participants);
        //
        List<Participant> topN = new ArrayList<>(participants);
        topN.sort(new Comparator<Participant>() {
            @Override
            public int compare(Participant o1, Participant o2) {
                return -RationalNumber.compare(o1.getOldSeasonPointsForWeaponProperty(WeaponType.SMALL_SWORD).getValue(),
                        o2.getOldSeasonPointsForWeaponProperty(WeaponType.SMALL_SWORD).get());
            }
        });

        topN = topN.subList(0,N-1); // imporrtant that top fighters will not fight against each other

        List<List<Participant>> particpantsInGroups = new ArrayList<>();
        for( CompetitionGroup cg : cgs)
        {
            List<Participant> partInGroup = new ArrayList<>();
            for(Fight f : cg.getFightsList())
            {
                if(!partInGroup.contains(f.getFirstParticipant()))
                    partInGroup.add(f.getFirstParticipant());
                if(!partInGroup.contains(f.getSecondParticipant()))
                    partInGroup.add(f.getSecondParticipant());
                if(topN.contains(f.getFirstParticipant()) && topN.contains(f.getSecondParticipant()))
                    fail();
            }
            particpantsInGroups.add(partInGroup);
        }

    }



    }