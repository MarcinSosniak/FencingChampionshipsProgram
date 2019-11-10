package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FightDrawing.FightDrawStrategyPicker;
import model.command.Command;
import model.config.ConfigReader;
import model.config.ConfigUtils;
import model.enums.CompetitionState;
import model.enums.WeaponType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import util.RationalNumber;

public class WeaponCompetition {

    private final WeaponType weaponType;
    private final  WeaponType weaponType;
    private CompetitionState competitionState;
    private ObservableList<Participant> participants;
    private ObservableList<Round> rounds;
    private final CommandStack cStack = new CommandStack();

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

    public CompetitionState getWeaponCompetitionState(){
        return this.competitionState;
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
    public List<Command> invalidateParticipant(Participant p) /** DO NOT CALL UNLESS THOURGH COMMAND **/ {
        List<Command> out = new ArrayList<>();
        for(CompetitionGroup g : (rounds.get(rounds.size()-1).getGroups())) {
            if(g.fInGroup(p)) {
                for(Fight fight : g.getFightsList()) {
                    if(!fight.fHasResult() && fight.fIn(p)) {
                        out.add(fight.getCommandSetLooser(p));
                    }
                }
            }
        }
        return out;
    }

    public util.RationalNumber getParticipantScore(Participant p) {
        util.RationalNumber out= new RationalNumber();
        for (Round round : rounds) {
            out = out.add(round.getParticpantScore(p));
        }
        return out;
    }

    public void startFirstRound() {
        RoundCreator rc = new RoundCreator(participants);
        rc.startRound();
    }

    public void startFirstRound(int gropuSize) {
        RoundCreator rc = new RoundCreator(participants, gropuSize);
        rc.startRound();
    }

    public Round getLastRound()
    {
        return rounds.get(rounds.size()-1);
    }

    // WEAPON COMP ROUDN CREATOR

    public class RoundCreator
    {
        private boolean fRoundReady=false;
        private Round _round;
        private ArrayList<Participant> participantsForRound = new ArrayList<>();
        private ArrayList<Participant> participantsForPlayoff=new ArrayList<>();
        private int groupSize;
        private int particpantsNeeded;

        public void startRound()
        {
            if(!fRoundReady)
                throw new IllegalStateException("Round cannot be started, resolve overtime/runoff/playoff");
            _round.drawGroups();
            cStack.executeCommand(new AddRoundCommand(_round));
        }

        public boolean getfRoundReady()
        {
            return fRoundReady;
        }

        public List<Participant> getParticipantsForPlayoff()
        {
            return participantsForPlayoff;
        }

        public int getParticpantsNeededFromPlayoffs()
        {
            return particpantsNeeded - participantsForRound.size();
        }

        public void setPlayOffWinners(List<Participant> winners)
        {
            if(winners.size() +participantsForRound.size() != particpantsNeeded )
                throw new IllegalStateException("winners list is too short or to large");
            participantsForRound.addAll(winners);
            _round= new Round(WeaponCompetition.this,rounds.size()-1,groupSize,participantsForRound,getFightDrawStrategyPicker());
            fRoundReady=true;
        }

        /**
         * throw no errors if particpantsNeeded > amount of paritcpants in lastRound
         *
          **/
        public RoundCreator(int groupSize, int particpantsNeeded)
        {
            this.groupSize=groupSize;
            this.particpantsNeeded=particpantsNeeded;
            Round lastRound;
            lastRound=rounds.get(rounds.size()-1);
            ArrayList<Participant> participantsEligible= new ArrayList<>(
                    lastRound.getParticipants().stream()
                    .filter(x -> x.isInjured(weaponType)).collect(Collectors.toList()));

            if (participantsEligible.size() <= particpantsNeeded)
            {
                participantsForRound.addAll(participantsEligible);
                fRoundReady=true;
                _round = new Round(rounds.size()-1,groupSize,participantsEligible,getFightDrawStrategyPicker());
                return;
            }
            participantsEligible.sort(new Comparator<Participant>() {
                @Override
                public int compare(Participant o1, Participant o2) {
                    return RationalNumber.compare(getParticpantScore(o1),getParticpantScore(o2)); // this *should* favour greater value
                }
            });

            RationalNumber cutoff= getParticpantScore(participantsEligible.get(particpantsNeeded-1));
            participantsForRound.addAll(participantsEligible.stream()
                    .filter(x -> RationalNumber.greater(getParticpantScore(x),cutoff))
                    .collect(Collectors.toList()));
            participantsForPlayoff.addAll(participantsEligible.stream()
                    .filter(x -> getParticpantScore(x).equals(cutoff))
                    .collect(Collectors.toList()));
            if(participantsForPlayoff.size() + participantsForRound.size()== particpantsNeeded)
            {
                participantsForRound.addAll(participantsForPlayoff);
                fRoundReady=true;
                _round = new Round(rounds.size(),groupSize,participantsForRound,getFightDrawStrategyPicker());
                return;
            }
            fRoundReady=false;
        }

        public RoundCreator(List<Participant> participants, int groupSize)
        {
            if(rounds.size() > 0)
                throw  new IllegalStateException("use this to construct only the first round");
            participantsForRound.addAll(participants);
            _round=new Round(0,groupSize,participantsForRound,getFightDrawStrategyPicker());
            fRoundReady=true;
        }

        public RoundCreator(List<Participant> participants)
        {
            if(rounds.size() > 0)
                throw  new IllegalStateException("use this to construct only the first round");
            int groupSize = ConfigReader.getInstance().getIntValue(ConfigUtils.getWeaponTag(weaponType),"START_GROUP_SIZE");
            participantsForRound.addAll(participants);
            _round=new Round(0,groupSize,participantsForRound,getFightDrawStrategyPicker());
            fRoundReady=true;
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