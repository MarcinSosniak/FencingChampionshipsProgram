package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FightDrawing.FightDrawStrategyPicker;
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

    public FightDrawStrategyPicker getFightDrawStrategyPicker()
    {
        return  new FightDrawStrategyPicker();
    }

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

    // WEAPON COMP ROUDN CREATOR

    public class roundCreator
    {
        private boolean fRoundReady=false;
        private Round round;
        private List<Participant> participantsForRound;
        private List<Participant> participantsFromLastRound;
        private int groupSize;
        private int particpantsNeeded;

        public void startRound()
        {
            if(!fRoundReady)
                throw new IllegalStateException("Round cannot be started, resolve overtime/runoff/playoff");
            round.drawGroups();
            cStack.executeCommand(new AddRoundCommand(round));
        }

        public boolean getfRoundReady()
        {
            return fRoundReady;
        }

        public List<Participant> getParticipantsForPlayoff()
        {
            return null;
        }

        /**
         * throw no errors if particpantsNeeded > amount of paritcpants in lastRound
         * last round may be null
          **/
        public roundCreator(int groupSize, int particpantsNeeded, Round lastRound)
        {

        }
    }

    private class AddRoundCommand implements Command
    {

        @Override
        public void execute() {
            rounds.add(_round);
        }

        @Override
        public void undo() {
            rounds.remove(rounds.size()-1);
        }

        @Override
        public void redo() {
            execute();
        }

        private Round _round;

        public AddRoundCommand(Round round)
        {
            this._round=round;
        }
    }

}