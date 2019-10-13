package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.command.Command;
import model.enums.WeaponType;

import java.util.ArrayList;
import java.util.List;

public class WeaponCompetition {

    private WeaponType weaponType;
    private ObservableList<Participant> participants;
    private ObservableList<Round> rounds;
    private CommandStack cStack= new CommandStack();

    public WeaponCompetition(WeaponType weaponType, ObservableList<Participant> participants){
        this.weaponType = weaponType;
        this.participants = participants;
        this.rounds = FXCollections.observableArrayList();
    }

    public void addParticipantToWeaponCompetition (Participant participant){ participants.add(participant); }

    public void removeParticipantFromWeaponCompetition (Participant participant){ participants.remove(participant); }

    public void addRound(Round round){ rounds.add(round.setMyWeaponCompetition(this).drawGroups()); }

    public String groupForParticipant(Participant p){
        return "a";
    }

    public WeaponType getWeaponType(){
        return this.weaponType;
    }

    public List<Participant> getParticipants(){
        return participants;
    }

    public CommandStack getcStack() { return cStack; }

    /**DOES NOT ACTAULLY DO ANYTHING**/
    public List<Command> invalidateParticipant(Participant p) /** DO NOT CALL UNLESS THOURGH COMMAND **/
    {
        List<Command> out = new ArrayList<>();
        for(CompetitionGroup g : (rounds.get(rounds.size()-1).getGroups()))
        {
            if(g.fInGroup(p))
            {
                for(Fight fight : g.getFightsList())
                {
                    if(!fight.fHasResult() && fight.fIn(p))
                    {
                        out.add(fight.getCommandSetLooser(p));
                    }
                }
            }
        }
        return out;
    }

}