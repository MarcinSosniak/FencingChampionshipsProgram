package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FightDrawing.FightDrawStrategyPicker;
import model.command.AddRoundCommand;
import model.command.Command;
import model.command.CommandAddInjury;
import model.config.ConfigReader;
import model.config.ConfigUtils;
import model.enums.CompetitionState;
import model.enums.WeaponType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import util.RationalNumber;

public class WeaponCompetition implements Serializable {

    private WeaponType weaponType; // was final
    private CompetitionState competitionState;
    private ObservableList<Participant> participants;
    private ObservableList<Round> rounds;
    private CommandStack cStack= new CommandStack(); // was final

    public WeaponCompetition(WeaponType weaponType, ObservableList<Participant> participants){
        this.weaponType = weaponType;
        this.participants = participants;
        this.rounds = FXCollections.observableArrayList();
        this.competitionState = CompetitionState.INITIAL_STATE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof WeaponCompetition) {
            for (int i=0; i<((WeaponCompetition) obj).participants.size(); i++){
                Participant p = ((WeaponCompetition) obj).participants.get(i);
                boolean found = false;
                for (int j=0; j<this.participants.size(); j++) {
                    if (this.participants.get(j).equals(p)){
                        found = true;
                        break;
                    }
                } if (!found) return false;
            }
            for (int i=0; i<((WeaponCompetition) obj).rounds.size(); i++){
                Round r = ((WeaponCompetition) obj).rounds.get(i);
                boolean found = false;
                for (int j=0; j<this.rounds.size(); j++) {
                    if (this.rounds.get(j).equals(r)){
                        found = true;
                        break;
                    }
                } if (!found) return false;
            }

            return this.weaponType.equals(((WeaponCompetition) obj).weaponType) &&
                    this.competitionState.equals(((WeaponCompetition) obj).competitionState) &&
                    this.cStack.equals(((WeaponCompetition) obj).cStack);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.weaponType.hashCode() + this.competitionState.hashCode();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(weaponType);
        stream.writeObject(competitionState);
        ArrayList<Participant> participantArrayList = new ArrayList<>();
        participants.forEach(p -> participantArrayList.add(p));
        stream.writeObject(participantArrayList);

        ArrayList<Round> roundArrayList = new ArrayList<>();
        rounds.forEach(r -> roundArrayList.add(r));
        stream.writeObject(roundArrayList);
        stream.writeObject(cStack);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        weaponType = (WeaponType) stream.readObject();
        competitionState = (CompetitionState) stream.readObject();
        participants = FXCollections.observableArrayList((ArrayList<Participant>) stream.readObject());
        rounds = FXCollections.observableArrayList((ArrayList<Round>) stream.readObject());
        cStack = (CommandStack) stream.readObject();
    }

    public void addRound(AddRoundCommand.ValidInvocationChecker validInvocationChecker, Round round){
        Objects.requireNonNull(validInvocationChecker);
        rounds.add(round.setMyWeaponCompetition(this).drawGroups());
    }

    public void removeRound(AddRoundCommand.ValidInvocationChecker validInvocationChecker){
        Objects.requireNonNull(validInvocationChecker);
        rounds.remove(rounds.size()-1);
    }

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

    /** prepares list of commands to execute **/
    public List<Command> invalidateParticipant(CommandAddInjury.ValidInvocationChecker checker, Participant p) {
        Objects.requireNonNull(checker);
        List<Command> out = new ArrayList<>();
        for (CompetitionGroup g : (rounds.get(rounds.size()-1).getGroups())) {
            if (g.fInGroup(p)) {
                for (Fight fight : g.getFightsList()) {
                    if (!fight.fHasResult() && fight.fIn(p)) {
                        out.add(fight.getCommandSetLooser(p));
                    }
                }
            }
        }
        return out;
    }

    public util.RationalNumber getParticpantScore(Participant p) {
        util.RationalNumber out= new RationalNumber();
        for (Round round : rounds)
        {
            out= out.add(round.getParticpantScore(p));
        }
        return out;
    }

    public void startFirstRound() {
        RoundCreator rc = new RoundCreator(participants);
        rc.startRound();
    }

    public void startFirstRound(int gropuSize)
    {
        RoundCreator rc = new RoundCreator(participants,gropuSize);
        rc.startRound();
    }

    public Round getLastRound()
    {
        if (rounds.size() > 0) return rounds.get(rounds.size()-1);
        return null;
    }


    // WEAPON COMP ROUND CREATOR
    public class RoundCreator {
        private boolean fRoundReady = false;
        private Round _round;
        private ArrayList<Participant> participantsForRound = new ArrayList<>();
        private ArrayList<Participant> participantsForPlayoff = new ArrayList<>();
        private int groupSize;
        private int particpantsNeeded;

        public void startRound() {
            if (!fRoundReady)
                throw new IllegalStateException("Round cannot be started, resolve overtime/runoff/playoff");
            _round.drawGroups();
            cStack.executeCommand(new AddRoundCommand(WeaponCompetition.this, _round));
        }

        public boolean getfRoundReady() {
            return fRoundReady;
        }

        public List<Participant> getParticipantsForPlayoff() {
            return participantsForPlayoff;
        }

        public int getParticpantsNeededFromPlayoffs() {
            return particpantsNeeded - participantsForRound.size();
        }

        public void setPlayOffWinners(List<Participant> winners) {
            if (winners.size() + participantsForRound.size() != particpantsNeeded)
                throw new IllegalStateException("winners list is too short or to large");
            participantsForRound.addAll(winners);
            _round = new Round(WeaponCompetition.this, rounds.size() - 1, groupSize, participantsForRound, getFightDrawStrategyPicker());
            fRoundReady = true;
        }

        /**
         * throw no errors if particpantsNeeded > amount of paritcpants in lastRound
         **/
        public RoundCreator(int groupSize, int particpantsNeeded) {
            this.groupSize = groupSize;
            this.particpantsNeeded = particpantsNeeded;
            Round lastRound;
            lastRound = rounds.get(rounds.size() - 1);
            ArrayList<Participant> participantsEligible = new ArrayList<>(
                    lastRound.getParticipants().stream()
                            .filter(x -> x.isInjured(weaponType)).collect(Collectors.toList()));

            if (participantsEligible.size() <= particpantsNeeded) {
                participantsForRound.addAll(participantsEligible);
                fRoundReady = true;
                _round = new Round(WeaponCompetition.this, rounds.size() - 1, groupSize, participantsEligible, getFightDrawStrategyPicker());
                return;
            }
            participantsEligible.sort(new Comparator<Participant>() {
                @Override
                public int compare(Participant o1, Participant o2) {
                    return RationalNumber.compare(getParticpantScore(o1), getParticpantScore(o2)); // this *should* favour greater value
                }
            });

            RationalNumber cutoff = getParticpantScore(participantsEligible.get(particpantsNeeded - 1));
            participantsForRound.addAll(participantsEligible.stream()
                    .filter(x -> RationalNumber.greater(getParticpantScore(x), cutoff))
                    .collect(Collectors.toList()));
            participantsForPlayoff.addAll(participantsEligible.stream()
                    .filter(x -> getParticpantScore(x).equals(cutoff))
                    .collect(Collectors.toList()));
            if (participantsForPlayoff.size() + participantsForRound.size() == particpantsNeeded) {
                participantsForRound.addAll(participantsForPlayoff);
                fRoundReady = true;
                _round = new Round(WeaponCompetition.this, rounds.size(), groupSize, participantsForRound, getFightDrawStrategyPicker());
                return;
            }
            fRoundReady = false;
        }

        public RoundCreator(List<Participant> participants, int groupSize) {
            if (rounds.size() > 0)
                throw new IllegalStateException("use this to construct only the first round");
            participantsForRound.addAll(participants);
            _round = new Round(WeaponCompetition.this, 0, groupSize, participantsForRound, getFightDrawStrategyPicker());
            fRoundReady = true;
        }

        public RoundCreator(List<Participant> participants) {
            if (rounds.size() > 0)
                throw new IllegalStateException("use this to construct only the first round");
            int groupSize = ConfigReader.getInstance().getIntValue(ConfigUtils.getWeaponTag(weaponType), "START_GROUP_SIZE");
            participantsForRound.addAll(participants);
            _round = new Round(WeaponCompetition.this, 0, groupSize, participantsForRound, getFightDrawStrategyPicker());
            fRoundReady = true;
        }
    }
    public ObservableList<Participant> getParticipantsObservableList(){ return participants; }
}