package model;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.command.Command;
import model.command.CommandAddBattleResult;
import model.command.ValidInvocationChecker;
import model.enums.FightScore;
import model.enums.WeaponType;
import model.exceptions.NoSuchWeaponException;
import util.RationalNumber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.FightDrawing.FightDrawStrategy;
import model.FightDrawing.FightDrawStrategyPicker;
import model.KillerDrawing.KillerRandomizerStrategyPicker;
import model.command.ChangePointsCommand;
import util.RationalNumber;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Round implements Serializable {

    private static final long serialVersionUID = 4;
    private int roundNumber;
    private int groupSize;
    // last cut-off
    private FightDrawStrategy fightDrawStrategy;
    private ObservableList<CompetitionGroup> groups=null;
    private ObservableList<Participant> participants;
    private Map<Participant, ObjectProperty<RationalNumber>> roundScore = FXCollections.observableHashMap();
    private Map<Participant,Integer> participantDoubleFightNumber= new HashMap<>();
    private int participantExcpectedFightNumber; // size of group -1
    private WeaponCompetition myWeaponCompetition;
    private SimpleObjectProperty<Fight> lastModyfiedFight;

    public Fight getLastModyfiedFight() {
        this.groups.get(0).getFightsList().get(0).commandSetFightScoreDirect(FightScore.WON_SECOND);
        return this.groups.get(0).getFightsList().get(0);
    }


    //depriciated
    private Round(int roundNumber, int groupSize,ArrayList<Participant> participants, FightDrawStrategyPicker fightDrawStrategyPicker){
        this.roundNumber = roundNumber;
        this.groupSize = groupSize;
        this.participantExcpectedFightNumber=groupSize-1;
        this.fightDrawStrategy = fightDrawStrategyPicker.pick(KillerRandomizerStrategyPicker.KillerRandomizerStrategy());
        this.participants= FXCollections.observableArrayList(participants);
        /*TODO: Refactor */
        this.lastModyfiedFight = new SimpleObjectProperty<>();
        for(Participant p : participants)
        {
            participantDoubleFightNumber.put(p,0);
            roundScore.put(p,new SimpleObjectProperty<>(new RationalNumber(0)));
        }
    }

    public Round(WeaponCompetition myWeaponCompetition,int roundNumber, int groupSize,ArrayList<Participant> participants, FightDrawStrategyPicker fightDrawStrategyPicker){
        this.myWeaponCompetition=myWeaponCompetition;
        this.roundNumber = roundNumber;
        this.groupSize = groupSize;
        this.participantExcpectedFightNumber=groupSize-1;
        this.fightDrawStrategy = fightDrawStrategyPicker.pick(KillerRandomizerStrategyPicker.KillerRandomizerStrategy());
        this.participants= FXCollections.observableArrayList(participants);
        /*TODO: Refactor */
        this.lastModyfiedFight = new SimpleObjectProperty<>();
        for(Participant p : participants)
        {
            participantDoubleFightNumber.put(p,0);
            roundScore.put(p,new SimpleObjectProperty<>(new RationalNumber(0)));
        }
        drawGroups();
    }

    public Round setMyWeaponCompetition(WeaponCompetition myWeaponCompetition) {
        this.myWeaponCompetition = myWeaponCompetition;
        return this;
    }


    public WeaponCompetition getMyWeaponCompetition() {
        return myWeaponCompetition;
    }

    public ObservableList<Participant> getParticipants() {
        return participants;
    }

    public ObservableList<CompetitionGroup> getGroups() {
        return groups;
    }

    public CommandStack getCStack() {
        return getMyWeaponCompetition().getcStack();}

    public int getGroupSize() { return groupSize; }

    public int getRoundNumber() { return roundNumber; }


    public Round drawGroups() {
        groups = FXCollections.observableArrayList(fightDrawStrategy.drawFightsForRound(this,groupSize,participants));
        return this;
    }

    public void addExcpectedFightToParticipant(Participant p) {
        Integer current = participantDoubleFightNumber.get(p);
        participantDoubleFightNumber.put(p,current+1);
    }

    public void addPointsFromFight(ValidInvocationChecker checker, Participant p, int points) {
        Objects.requireNonNull(checker);
        RationalNumber pScoreMultiplier = new RationalNumber(participantExcpectedFightNumber*2,participantDoubleFightNumber.get(p));
        RationalNumber participant_score= roundScore.get(p).get();
        RationalNumber points_to_add=pScoreMultiplier.multiply(points);
        RationalNumber after_add=participant_score.add(points_to_add);
        participant_score.set(after_add);
        p.addPointsForWeapon(checker,myWeaponCompetition.getWeaponType(),points_to_add);
    }

    public RationalNumber getParticpantScore(Participant p)
    {
        return roundScore.get(p).get();
    }


    public void addRoundScorePoints (ValidInvocationChecker checker, Participant p, RationalNumber points){
        Objects.requireNonNull(checker);
        RationalNumber newPoints = roundScore.get(p).get().add(points);
        roundScore.replace(p, new SimpleObjectProperty<>(newPoints));

        System.out.println("in ADD: " + roundScore.get(p).get());
    }


    public void subtractRoundScorePoints (ValidInvocationChecker checker, Participant p, RationalNumber points){
        Objects.requireNonNull(checker);
        roundScore.get(p).get().substract(points);
    }


    public ObjectProperty<RationalNumber> getParticpantScoreProperty(Participant p)
    {
        return roundScore.get(p);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeInt(roundNumber);
        stream.writeInt(groupSize);
        stream.writeObject(fightDrawStrategy);
        ArrayList<CompetitionGroup> competitionGroups = new ArrayList<>();
        groups.forEach(g -> competitionGroups.add(g)) ;
        stream.writeObject(competitionGroups);
        ArrayList<Participant> participants = new ArrayList<>();
        participants.forEach(p -> participants.add(p)) ;
        stream.writeObject(participants);

        Map<Participant, util.RationalNumber> scores = new HashMap<>();
        roundScore.forEach((p, rn) -> scores.put(p, rn.get()));
        stream.writeObject(scores);

        stream.writeObject(participantDoubleFightNumber);
        stream.writeInt(participantExcpectedFightNumber);
        stream.writeObject(myWeaponCompetition);
        stream.writeObject(lastModyfiedFight.get());
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        roundNumber = stream.readInt();
        groupSize = stream.readInt();
        fightDrawStrategy = (FightDrawStrategy) stream.readObject();
        groups = FXCollections.observableArrayList((ArrayList<CompetitionGroup>) stream.readObject());
        participants = FXCollections.observableArrayList((ArrayList<Participant>) stream.readObject());
        roundScore = FXCollections.observableHashMap();
        Map<Participant, RationalNumber> m = (Map<Participant, RationalNumber>) stream.readObject();
        m.forEach((p, r) -> roundScore.put(p, new SimpleObjectProperty<>(r)));
        participantDoubleFightNumber = (Map<Participant,Integer>) stream.readObject();
        participantExcpectedFightNumber = stream.readInt();
        myWeaponCompetition = (WeaponCompetition) stream.readObject();
        lastModyfiedFight = new SimpleObjectProperty<>((Fight) stream.readObject());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Round) {
            return (this.roundNumber == ((Round) obj).roundNumber) &&
                    (this.groupSize == ((Round) obj).groupSize) &&
                    (this.participantExcpectedFightNumber == ((Round) obj).participantExcpectedFightNumber);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.roundNumber + this.groupSize + this.participantExcpectedFightNumber;
    }
}