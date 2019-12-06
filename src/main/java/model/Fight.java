package model;


import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import model.command.Command;
import model.command.CommandAddBattleResult;
import model.command.ValidInvocationChecker;
import model.config.ConfigReader;
import model.enums.FightScore;
import model.enums.WeaponType;


import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public  class Fight implements Serializable {
    private ObjectProperty<Participant> firstParticipant= new SimpleObjectProperty<>();
    private ObjectProperty<Participant> secondParticipant = new SimpleObjectProperty<>();
    private ObjectProperty<FightScore> score = new SimpleObjectProperty<>();
    private StringProperty firstParticipantStringProperty = new SimpleStringProperty();
    private StringProperty secondParticipantStringProperty = new SimpleStringProperty();
    private Round round;
    private static final long serialVersionUID = 2;
    private boolean fSet=false;


    public String getFirstParticipantStringProperty() {
        return firstParticipantStringProperty.get();
    }

    public StringProperty firstParticipantStringProperty() {
        return firstParticipantStringProperty;
    }

    public String getSecondParticipantStringProperty() {
        return secondParticipantStringProperty.get();
    }

    public StringProperty secondParticipantStringProperty() {
        return secondParticipantStringProperty;
    }

    public Round getRound() {
        return round;
    }

    public Fight(Round round,Participant first, Participant second){
        this.round=round;
        this.firstParticipant.setValue(first);
        this.secondParticipant.setValue(second);
        score.setValue(FightScore.NULL_STATE);
        this.firstParticipantStringProperty.setValue(this.getFirstParticipant().nameProperty().getValue() + " " + this.getFirstParticipant().surnameProperty().getValue());
        this.secondParticipantStringProperty.setValue(this.getSecondParticipant().nameProperty().getValue() + " " + this.getSecondParticipant().surnameProperty().getValue());
        System.out.println("Adding participants to round  weapon : " + WeaponType.str(round.getMyWeaponCompetition().getWeaponType()));

    }

    public Participant getFirstParticipant() {
        return firstParticipant.get();
    }

    public ObjectProperty<Participant> firstParticipantProperty() {
        return firstParticipant;
    }

    public Participant getSecondParticipant() {
        return secondParticipant.get();
    }

    public ObjectProperty<Participant> secondParticipantProperty() {
        return secondParticipant;
    }

    public ObjectProperty<FightScore> scoreProperty() {
        return score;
    }

    public ArrayList<Participant> getFightParticipants(){
        return new ArrayList<>(Arrays.asList(firstParticipant.get(), secondParticipant.get()));
    }


    public FightScore getScore() { return score.get(); }

    public String toString() {
        return this.firstParticipant.toString() + " x " + this.secondParticipant.toString();
    }

    public void setFightScore(ValidInvocationChecker validInvocationChecker, FightScore score)
    {
        if(fSet && score==FightScore.NULL_STATE) {
            round.decPlayedFights();
            fSet=false;
        }
        if(!fSet && score!=FightScore.NULL_STATE)
        {
            round.incPlayedFights();
            fSet=true;
        }
        Objects.requireNonNull(validInvocationChecker);
        this.secondParticipantStringProperty.setValue(this.secondParticipantStringProperty().getValue());
        this.firstParticipantStringProperty.setValue(this.firstParticipantStringProperty().getValue());
        this.score.setValue(score);
    }

    public boolean fHasResult(){return score.get()==FightScore.NULL_STATE;}

    public void commandSetFightScoreDirect(FightScore score) {
        round.getCStack().executeCommand(new CommandAddBattleResult(this,score));
    }

    public void commandSetWinner(ValidInvocationChecker checker, Participant winner){
        Objects.requireNonNull(checker);
        round.getCStack().executeCommand(new CommandAddBattleResult(this,winner));
    }


    // ???????
    public Command getCommandSetLooser(Participant p) /**does NOT. I REPEAT DOES NOT PUT IN COMMAND STACK. FOR USE IN OTHER COMMANDS ONLY**/
    {
        return new CommandAddBattleResult(this,p,true);
    }

    public FightScore getScoreWithWinner(ValidInvocationChecker validInvocationChecker,
                                         Participant winner) // doesn't set anything
    {
        Objects.requireNonNull(validInvocationChecker);
        if(firstParticipant.equals(winner))
            return FightScore.WON_FIRST;
        else if(secondParticipant.equals(winner))
            return FightScore.WON_SECOND;
        else
            throw new IllegalArgumentException("participant missmatch, one to be winner is not in fight");
    }

    public FightScore getScoreWithLoser(ValidInvocationChecker validInvocationChecker,
                                        Participant loser) // doesn't set anything
    {
        Objects.requireNonNull(validInvocationChecker);
        if(firstParticipant.equals(loser))
            return FightScore.WON_SECOND;
        else if(secondParticipant.equals(loser))
            return FightScore.WON_FIRST;
        else
            throw new IllegalArgumentException("participant missmatch, one to be winner is not in fight");
    }



    public void setDouble(){
        score.set(FightScore.DOUBLE);
    }

    public boolean fIn(Participant part)
    {
        if (part.equals(firstParticipant) || part.equals(secondParticipant))
            return true;
        return false;
    }

    public Participant getWinner()
    {
        FightScore actualScore= score.get();
        if(actualScore==null)
            return null;
        if (actualScore==FightScore.WON_FIRST)
            return firstParticipant.get();
        if (actualScore==FightScore.WON_SECOND)
            return  secondParticipant.get();
        else
            return null;
    }

    public void updateScore(ValidInvocationChecker validInvocationChecker, boolean reverse)
    {
        Objects.requireNonNull(validInvocationChecker);
        int multiplier=1;
        if(reverse)
            multiplier=-1;
        ConfigReader cfg = ConfigReader.getInstance();
        int winPoints=cfg.getIntValue("points","WIN",2);
        int loosePoints=cfg.getIntValue("points","LOSE",0);
        int doublePoints=cfg.getIntValue("points","DOUBLE",-1);
        if (score.get() == FightScore.DOUBLE)
        {
            round.addPointsFromFight(validInvocationChecker, firstParticipant.get(),multiplier*doublePoints);
            round.addPointsFromFight(validInvocationChecker, secondParticipant.get(),multiplier*doublePoints);
        }
        if (score.get()== FightScore.WON_FIRST)
        {
            round.addPointsFromFight(validInvocationChecker, firstParticipant.get(),multiplier*winPoints);
            round.addPointsFromFight(validInvocationChecker, secondParticipant.get(),multiplier*loosePoints);
        }
        if (score.get()== FightScore.WON_SECOND)
        {
            round.addPointsFromFight(validInvocationChecker, firstParticipant.get(),multiplier*loosePoints);
            round.addPointsFromFight(validInvocationChecker, secondParticipant.get(),multiplier*winPoints);
        }
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(firstParticipant.get());
        stream.writeObject(secondParticipant.get());
        stream.writeObject(score.get());
        stream.writeObject(round);
        stream.writeObject(firstParticipantStringProperty.get());
        stream.writeObject(secondParticipantStringProperty.get());
        stream.writeBoolean(fSet);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        firstParticipant = new SimpleObjectProperty<>((Participant) stream.readObject());
        secondParticipant = new SimpleObjectProperty<>((Participant) stream.readObject());
        score = new SimpleObjectProperty<>((FightScore) stream.readObject());
        round = (Round) stream.readObject();
        firstParticipantStringProperty = new SimpleStringProperty((String) stream.readObject());
        secondParticipantStringProperty = new SimpleStringProperty((String) stream.readObject());
        fSet=stream.readBoolean();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Fight){
            return this.firstParticipant.get().equals(((Fight) obj).firstParticipant.get()) &&
                    this.secondParticipant.get().equals(((Fight) obj).secondParticipant.get()); //&&
                  //  this.score.get().equals(((Fight) obj).score.get());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.firstParticipant.hashCode() * 8 + this.secondParticipant.hashCode();
    }


    public static Callback<Fight, Observable[]> extractor(){
        System.out.println("callback");
        return (Fight f) ->
            new Observable[]{f.score,f.firstParticipantProperty(),f.secondParticipantProperty(),f.secondParticipantStringProperty(),f.firstParticipantStringProperty()};
    }


}
