package model;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.enums.FightScore;

import java.util.ArrayList;
import java.util.Arrays;

public  class Fight {
    private ObjectProperty<Participant> firstParticipant= new SimpleObjectProperty<>();
    private ObjectProperty<Participant> secondParticipant = new SimpleObjectProperty<>();
    private ObjectProperty<FightScore> score = new SimpleObjectProperty<>();

    public Fight(Participant first, Participant second){
        this.firstParticipant.setValue(first);
        this.secondParticipant.setValue(second);
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


    public void setFightScore(FightScore score)
    {
        this.score.setValue(score);
    }

    public void setWinner(Participant winner)
    {
        if(firstParticipant.equals(winner))
            score.setValue(FightScore.WON_FIRST);
        else if(secondParticipant.equals(winner))
            score.setValue(FightScore.WON_SECOND);
        else
            throw new IllegalArgumentException("participant missmatch, one to be winner is not in fight");
    }
    public void setDouble()
    {
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


}
