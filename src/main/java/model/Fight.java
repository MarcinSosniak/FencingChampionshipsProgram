package model;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.command.Command;
import model.command.CommandAddBattleResult;
import model.config.ConfigReader;
import model.enums.FightScore;
import org.omg.CORBA.Object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public  class Fight {
    private ObjectProperty<Participant> firstParticipant= new SimpleObjectProperty<>();
    private ObjectProperty<Participant> secondParticipant = new SimpleObjectProperty<>();
    private ObjectProperty<FightScore> score = new SimpleObjectProperty<>();
    private Round round;
    public Fight(Round round,Participant first, Participant second){
        this.round=round;
        this.firstParticipant.setValue(first);
        this.secondParticipant.setValue(second);
        score.setValue(FightScore.NULL_STATE);
        round.addExcpectedFightToParticipant(first);
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


    public void setFightScore(CommandAddBattleResult.ValidInvocationChecker validInvocationChecker, FightScore score)
    {
        Objects.requireNonNull(validInvocationChecker);
        this.score.setValue(score);
    }

    public boolean fHasResult(){return score.get()==FightScore.NULL_STATE;}

    public void commandSetFightScoreDirect(FightScore score) {
        round.getCStack().executeCommand(new CommandAddBattleResult(this,score));
    }

    public void commandSetWinner(CommandAddBattleResult.ValidInvocationChecker checker, Participant winner){
        Objects.requireNonNull(checker);
        round.getCStack().executeCommand(new CommandAddBattleResult(this,winner));
    }


    // ???????
    public Command getCommandSetLooser(Participant p) /**does NOT. I REPEAT DOES NOT PUT IN COMMAND STACK. FOR USE IN OTHER COMMANDS ONLY**/
    {
        return new CommandAddBattleResult(this,p,true);
    }

    public FightScore getScoreWithWinner(CommandAddBattleResult.ValidInvocationChecker validInvocationChecker,
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

    public FightScore getScoreWithLoser(CommandAddBattleResult.ValidInvocationChecker validInvocationChecker,
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

    public void updateRoundScore(CommandAddBattleResult.ValidInvocationChecker validInvocationChecker, boolean reverse)
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
            round.addPointsFromFight(firstParticipant.get(),multiplier*doublePoints);
            round.addPointsFromFight(secondParticipant.get(),multiplier*doublePoints);
        }
        if (score.get()== FightScore.WON_FIRST)
        {
            round.addPointsFromFight(firstParticipant.get(),multiplier*winPoints);
            round.addPointsFromFight(secondParticipant.get(),multiplier*loosePoints);
        }
        if (score.get()== FightScore.WON_SECOND)
        {
            round.addPointsFromFight(firstParticipant.get(),multiplier*loosePoints);
            round.addPointsFromFight(secondParticipant.get(),multiplier*winPoints);
        }
    }


}
