package model;


import java.util.ArrayList;
import java.util.Arrays;

public abstract class Fight {
    private Participant firstParticipant;
    private Participant secondParticipant;
    private FightScore score;
    private Participant winner;

    public Fight(Participant first, Participant second){
        this.firstParticipant = first;
        this.secondParticipant = second;
    }

    public ArrayList<Participant> getFightParticipants(){
        return new ArrayList<>(Arrays.asList(firstParticipant, secondParticipant));
    }


    public FightScore getScore() { return score; }


    public abstract FightScore fight();


}
