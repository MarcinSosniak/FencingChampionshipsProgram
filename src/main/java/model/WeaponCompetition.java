package model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FightDrawing.FightDrawStrategy;
import model.FightDrawing.FightDrawStrategyPicker;
import model.KillerDrawing.KillerRandomizerStrategyPicker;
import model.command.AddRoundCommand;
import model.command.Command;
import model.command.ValidInvocationChecker;
import model.config.ConfigReader;
import model.config.ConfigUtils;
import model.enums.CompetitionState;
import model.enums.FightScore;
import model.enums.WeaponType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import model.exceptions.NoSuchWeaponException;
import util.RationalNumber;

public class WeaponCompetition implements Serializable {

    private static final long serialVersionUID = 5;
    private WeaponType weaponType; // was final
    private CompetitionState competitionState;
    private ObservableList<Participant> participants;
    private ObservableList<Round> rounds;
    private CommandStack cStack= new CommandStack(); // was final

    private SimpleObjectProperty<Round> finalRound = new SimpleObjectProperty<>(null);
    private SimpleObjectProperty<Round> semifinalRound = new SimpleObjectProperty<>(null);

    public WeaponCompetition(WeaponType weaponType, ObservableList<Participant> participants){
        this.weaponType = weaponType;
        this.participants = participants;
        this.rounds = FXCollections.observableArrayList();
        this.competitionState = CompetitionState.INITIAL_STATE;

        for (Participant p: participants) p.setfWeaponParticipant(weaponType, true);
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
        stream.writeObject(semifinalRound.get());
        stream.writeObject(finalRound.get());
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        weaponType = (WeaponType) stream.readObject();
        competitionState = (CompetitionState) stream.readObject();
        participants = FXCollections.observableArrayList((ArrayList<Participant>) stream.readObject());
        rounds = FXCollections.observableArrayList((ArrayList<Round>) stream.readObject());
        cStack = (CommandStack) stream.readObject();
        semifinalRound = new SimpleObjectProperty<>((Round) stream.readObject());
        finalRound = new SimpleObjectProperty<>((Round) stream.readObject());
    }

    public void addRound(ValidInvocationChecker validInvocationChecker, Round round){
        Objects.requireNonNull(validInvocationChecker);
//        rounds.add(round.setMyWeaponCompetition(this).drawGroups());
        rounds.add(round);
    }

    public void removeRound(ValidInvocationChecker validInvocationChecker){
        Objects.requireNonNull(validInvocationChecker);
        rounds.remove(rounds.size()-1);
    }

    public String groupForParticipant(Participant p){
        Round r = this.getLastRound();
        String to_ret = "";
        for(CompetitionGroup cg: r.getGroups()){
            if(cg.fInGroup(p)){
                if(!to_ret.equals("")){
                    to_ret += " | ";
                }
                to_ret += cg.getGroupID();
            }
        }
        return to_ret;
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

    public void addParticipants(List<Participant> participants)
    {
        this.participants.addAll(participants);
    }
    /** prepares list of commands to execute **/
    public List<Command> invalidateParticipant(ValidInvocationChecker checker, Participant p) {
        Objects.requireNonNull(checker);
        System.out.println("in invalidate");
        List<Command> out = new ArrayList<>();
        for (CompetitionGroup g : getLastRound().getGroups()) {
            if (g.fInGroup(p)) {
                for (Fight fight : g.getFightsList()) {
                    if (!fight.fHasResult() && fight.fIn(p)) {
                        out.add(fight.getCommandSetLooser(checker,p));
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
        RoundCreator rc = new RoundCreator(participants,participants.size()+2,FightDrawStrategyPicker.STRATEGY_NAMES.FIRST);
        rc.startRound();
    }

    public void startFirstRound(int gropuSize) {
        String strategyName= ConfigReader.getInstance().getStringValue(WeaponType.str(weaponType)+"_ROUND_"+Integer.toString(rounds.size()),"STRATEGY","DEFAULT");
        RoundCreator rc = new RoundCreator(participants,gropuSize,FightDrawStrategyPicker.STRATEGY_NAMES.fromString(strategyName));
        rc.startRound();
    }

    public void setFinalRound() {
        this.finalRound.set(this.getLastRound());
    }

    /* Argument in call */
    public RoundCreator prepareNewRound(int groupSize, int participantsCount, boolean fSemiFinal, FightDrawStrategyPicker.STRATEGY_NAMES strat)
    {
        return new RoundCreator(groupSize,participantsCount,fSemiFinal,strat);
    }

    /* From config if final */
    public RoundCreator prepareNewRound()
    {
        int groupSize  = ConfigReader.getInstance().getIntValue(WeaponType.str(weaponType)+"_ROUND_"+Integer.toString(rounds.size()),"GROUP_SIZE",3);
        int participantsCount= ConfigReader.getInstance().getIntValue(WeaponType.str(weaponType)+"_ROUND_"+Integer.toString(rounds.size()),"PARTICIPANTS_COUNT",3);
        int finalRoundNumber = ConfigReader.getInstance().getIntValue(WeaponType.str(weaponType).toUpperCase(), "FINAL_ROUND_NUMBER");
        String fightDrawStrategyName = ConfigReader.getInstance().getStringValue(WeaponType.str(weaponType)+"_ROUND_"+Integer.toString(rounds.size()),"STRATEGY","DEFAULT");
        if(WeaponCompetition.this.rounds.size() == finalRoundNumber - 1)
            return new RoundCreator(groupSize,participantsCount,true,FightDrawStrategyPicker.STRATEGY_NAMES.fromString(fightDrawStrategyName));
        else
            return new RoundCreator(groupSize,participantsCount,false,FightDrawStrategyPicker.STRATEGY_NAMES.fromString(fightDrawStrategyName));
    }


    public Round getLastRound()
    {
        if (rounds.size() > 0) return rounds.get(rounds.size()-1);
        return null;
    }
    public int getNextRoundNumber()
    {
        return rounds.size();
    }


    // WEAPON COMP ROUND CREATOR
    public class RoundCreator {
        private boolean fRoundReady = false;
        private Round _round;
        private ArrayList<Participant> participantsForRound = new ArrayList<>();
        private ArrayList<Participant> participantsForPlayoff = new ArrayList<>();
        private int groupSize;
        private int particpantsNeeded;
        private boolean fSemiFinal;
        private FightDrawStrategyPicker.STRATEGY_NAMES stratName=FightDrawStrategyPicker.STRATEGY_NAMES.DEFAULT;

        public void startRound() {
            if (!fRoundReady)
                throw new IllegalStateException("Round cannot be started, resolve overtime/runoff/playoff");
//            _round.drawGroups();
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
            boolean fFinal = WeaponCompetition.this.getLastRound().isSemiFinal();
            _round = new Round(WeaponCompetition.this, rounds.size(), groupSize, participantsForRound, new FightDrawStrategyPicker(stratName),fFinal,this.fSemiFinal);
            fRoundReady = true;
        }

        /**
         * throw no errors if particpantsNeeded > amount of paritcpants in lastRound
         **/
        private void prepareSemiFinal(){
            //take 4 with highest points
            this.groupSize=2;
            this.particpantsNeeded=4;
            List<Participant> participants = WeaponCompetition.this.getLastRound().getParticipants();
            participants.sort((Participant p1,Participant p2) -> {
                try {
                    RationalNumber pointp1 = p1.getPointsForWeaponProperty(WeaponCompetition.this.weaponType).get();
                    RationalNumber pointp2 = p2.getPointsForWeaponProperty(WeaponCompetition.this.weaponType).get();
                    return RationalNumber.compare(pointp1,pointp2);
                }catch (NoSuchWeaponException e){
                    return 0;
                }
            });
            try {
                Collections.reverse(participants);
                List<Participant> top4current = new ArrayList<>();
                top4current.add(participants.get(0));
                top4current.add(participants.get(1));
                top4current.add( participants.get(2));
                top4current.add(participants.get(3));

                RationalNumber lowestPoints = top4current.get(top4current.size()-1).getPointsForWeaponProperty(WeaponCompetition.this.weaponType).get();

                // szukam wiekszych lub rownych od lowest points
                int found = 0;
                for(Participant p: participants){
                    if(RationalNumber.compare(p.getPointsForWeaponProperty(WeaponCompetition.this.weaponType).get(),lowestPoints) >= 0){
                        found ++;
                    }
                }
                if(found > 4){
                    top4current = top4current.stream().filter(p-> {
                        try{
                            return ! p.getPointsForWeaponProperty(WeaponCompetition.this.weaponType).get().equals(lowestPoints);
                        }
                        catch (Exception ex)
                        {
                            return false;
                        }
                    }).collect(Collectors.toList());
                    /* TODO: playoffs needed */
                    this.participantsForPlayoff.addAll(participants.stream().filter(p-> {
                        try {
                            return p.getPointsForWeaponProperty(WeaponCompetition.this.weaponType).get().equals(lowestPoints);
                        } catch (Exception ex) {
                            return false;
                        }
                    }).collect(Collectors.toList()));
                    this.participantsForRound.addAll(top4current);
                    this.fRoundReady=false;
                    // whole is fine but we need to inform how many people to add
                    this.particpantsNeeded = 4;
                    return;
                }else{ /* Should be 4 of then prepare round */
                    ArrayList<Participant> participantsToRound = new ArrayList<>();
                    this.participantsForRound.addAll(top4current);
                    this._round = new Round(WeaponCompetition.this,WeaponCompetition.this.getLastRound().getRoundNumber()+1,2,participantsForRound,getFightDrawStrategyPicker(),false,true);
                    this.fRoundReady=true;
                    return;
                }
            }catch (NoSuchWeaponException e){
                System.out.format("Some serious shit went wrong\n");
                e.printStackTrace();
            }
        }

        private void prepareFinalRound(){
            //if at least one is double there is no final
            // actualy it is BUT we know results because final round is the same as semi final round!
            Round roundBefore = WeaponCompetition.this.getLastRound();
            boolean fSemiFinalIsFinal = false;
            for(CompetitionGroup g: roundBefore.getGroups()){
                for(Fight f: g.getFightsList()){
                    if(f.getScore() == FightScore.DOUBLE){
                        fSemiFinalIsFinal = true;
                        break;
                        //actualy we could prepare here and return
                    }
                }
                if(fSemiFinalIsFinal){
                    break;
                }
            }

            /* Round final is as semiFinal*/
            if(fSemiFinalIsFinal){
                Round toRet = WeaponCompetition.this.getLastRound();
                toRet.setfFinal(true);
                fRoundReady=true;
                _round = toRet;
            }else{
                /* In this case we take 2 winners and set them to one final fight
                * and 2 losers and set them to 3rd place fight  */
                Participant pFinal1;
                Participant pFinal2;
                Participant pThird1;
                Participant pThird2;
                if(roundBefore.getGroups().get(0).getFightsList().get(0).getScore() == FightScore.WON_FIRST){
                    pFinal1 = roundBefore.getGroups().get(0).getFightsList().get(0).getFirstParticipant();
                    pThird1 = roundBefore.getGroups().get(0).getFightsList().get(0).getSecondParticipant();
                }else{
                    pFinal1 = roundBefore.getGroups().get(0).getFightsList().get(0).getSecondParticipant();
                    pThird1 = roundBefore.getGroups().get(0).getFightsList().get(0).getFirstParticipant();
                }

                if(roundBefore.getGroups().get(1).getFightsList().get(0).getScore() == FightScore.WON_FIRST){
                    pFinal2 = roundBefore.getGroups().get(1).getFightsList().get(0).getFirstParticipant();
                    pThird2 = roundBefore.getGroups().get(1).getFightsList().get(0).getSecondParticipant();
                }else{
                    pFinal2 = roundBefore.getGroups().get(1).getFightsList().get(0).getSecondParticipant();
                    pThird2 = roundBefore.getGroups().get(1).getFightsList().get(0).getFirstParticipant();
                }

                ArrayList<Participant> participants = new ArrayList<>();
                Collections.addAll(participants,pFinal1,pFinal2,pThird1,pThird2);
                Round toRet = new Round(WeaponCompetition.this,WeaponCompetition.this.getLastRound().getRoundNumber(),2,participants,
                        new FightDrawStrategyPicker(FightDrawStrategyPicker.STRATEGY_NAMES.FINAL),true,false);

                Fight finalFight = new Fight(toRet,pFinal1,pFinal2);
                List<Fight> finalFightList = new ArrayList<Fight>();
                finalFightList.add(finalFight);

                Fight thirdPlaceFight = new Fight(toRet,pThird1,pThird2);
                List<Fight> thirdPlaceFightList = new ArrayList<Fight>();
                thirdPlaceFightList.add(thirdPlaceFight);

                CompetitionGroup cgFinal = new CompetitionGroup(finalFightList);
                cgFinal.setGroupID("walka o pierwsze miejsce");
                CompetitionGroup cgThird = new CompetitionGroup(thirdPlaceFightList);
                cgThird.setGroupID("walka o trzecie miejsce");
                List<CompetitionGroup> cgList = new ArrayList<>();
                Collections.addAll(cgList,cgFinal,cgThird);

                /* Add posibility to prepare final round */
                toRet.prepareForFinals(cgList);
                fRoundReady=true;
                this._round = toRet;
            }
        }

        public RoundCreator(int groupSize, int particpantsNeeded,boolean fSemiFinal,FightDrawStrategyPicker.STRATEGY_NAMES stratName) {
            this.stratName=stratName;
            this.fSemiFinal=fSemiFinal;
            this.groupSize = groupSize;
            this.particpantsNeeded = particpantsNeeded;
            if(fSemiFinal)
            {
                prepareSemiFinal();
                return;
            }
            if(WeaponCompetition.this.getLastRound().isSemiFinal())
            {
                prepareFinalRound();
                return;
            }
            Round lastRound;
            lastRound = rounds.get(rounds.size() - 1);
            ArrayList<Participant> participantsEligible = new ArrayList<>(
                    lastRound.getParticipants().stream()
                            .filter(x -> !x.isInjured(weaponType)).collect(Collectors.toList()));

            if (participantsEligible.size() <= particpantsNeeded) {
                participantsForRound.addAll(participantsEligible);
                fRoundReady = true;
                _round = new Round(WeaponCompetition.this, rounds.size() - 1, groupSize, participantsEligible,
                        new FightDrawStrategyPicker(stratName),false,false);
                return;
            }
            participantsEligible.sort(new Comparator<Participant>() {
                @Override
                public int compare(Participant o1, Participant o2) {
                    return RationalNumber.compare(getParticpantScore(o1), getParticpantScore(o2)); // this *should* favour greater value
                }
            });
            Collections.reverse(participantsEligible);

            RationalNumber cutoff = getParticpantScore(participantsEligible.get(particpantsNeeded - 1));
            participantsForRound.addAll(participantsEligible.stream()
                    .filter(x -> RationalNumber.greater(getParticpantScore(x), cutoff))
                    .collect(Collectors.toList()));
            participantsForPlayoff.addAll(participantsEligible.stream()
                    .filter(x -> getParticpantScore(x).equals(cutoff))
                    .collect(Collectors.toList()));
            if (participantsForPlayoff.size()== (particpantsNeeded - participantsForRound.size())  || participantsForRound.size() == particpantsNeeded) {
                participantsForRound.addAll(participantsForPlayoff);
                fRoundReady = true;
                _round = new Round(WeaponCompetition.this, rounds.size()-1, groupSize, participantsForRound,  new FightDrawStrategyPicker(stratName),false,false);
                return;
            }
            fRoundReady = false;
        }

        public RoundCreator(List<Participant> participants, int groupSize,FightDrawStrategyPicker.STRATEGY_NAMES stratName) {
            this.stratName=stratName;
            if (rounds.size() > 0)
                throw new IllegalStateException("use this to construct only the first round");
            participantsForRound.addAll(participants);
            this.fSemiFinal=fSemiFinal;
            _round = new Round(WeaponCompetition.this, 0, groupSize, participantsForRound,  new FightDrawStrategyPicker(stratName),false,false);
            fRoundReady = true;
        }

//        public RoundCreator(List<Participant> participants) {
//            this.fSemiFinal = fSemiFinal;
//            if (rounds.size() > 0)
//                throw new IllegalStateException("use this to construct only the first round");
//            int groupSize = ConfigReader.getInstance().getIntValue(ConfigUtils.getWeaponTag(weaponType), "START_GROUP_SIZE");
//            participantsForRound.addAll(participants);
//            _round = new Round(WeaponCompetition.this, 0, groupSize, participantsForRound,  new FightDrawStrategyPicker(stratName),false,false);
//            fRoundReady = true;
//        }
    }
    public ObservableList<Participant> getParticipantsObservableList(){ return participants; }

    /** For final results required */
    public void calculateResults(){
        /* There is only two fights in finals and two group */
        Fight finalFight = finalRound.get().getGroups().get(0).getFightsList().get(0);
        if(finalFight.getScore().equals(FightScore.WON_FIRST)){
            finalFight.getFirstParticipant().getParticipantResult().getWeaponCompetitionResult(weaponType).setPlace(1);
            finalFight.getSecondParticipant().getParticipantResult().getWeaponCompetitionResult(weaponType).setPlace(2);
        }else if (finalFight.getScore().equals(FightScore.WON_SECOND)){
            finalFight.getFirstParticipant().getParticipantResult().getWeaponCompetitionResult(weaponType).setPlace(2);
            finalFight.getSecondParticipant().getParticipantResult().getWeaponCompetitionResult(weaponType).setPlace(1);
        }else { /* Assuming that double in final round make both on 2nd place*/
            finalFight.getFirstParticipant().getParticipantResult().getWeaponCompetitionResult(weaponType).setPlace(2);
            finalFight.getSecondParticipant().getParticipantResult().getWeaponCompetitionResult(weaponType).setPlace(2);
        }
        /* There is only one  */
        Fight thirdPlaceFight = finalRound.get().getGroups().get(1).getFightsList().get(0);
        if(thirdPlaceFight.getScore().equals(FightScore.WON_FIRST)){
            thirdPlaceFight.getFirstParticipant().getParticipantResult().getWeaponCompetitionResult(weaponType).setPlace(3);
            thirdPlaceFight.getSecondParticipant().getParticipantResult().getWeaponCompetitionResult(weaponType).setPlace(4);
        }else if (thirdPlaceFight.getScore().equals(FightScore.WON_SECOND)){
            thirdPlaceFight.getFirstParticipant().getParticipantResult().getWeaponCompetitionResult(weaponType).setPlace(4);
            thirdPlaceFight.getSecondParticipant().getParticipantResult().getWeaponCompetitionResult(weaponType).setPlace(3);
        }else { /* Assuming that double in final round make both on 2nd place*/
            thirdPlaceFight.getFirstParticipant().getParticipantResult().getWeaponCompetitionResult(weaponType).setPlace(4);
            thirdPlaceFight.getSecondParticipant().getParticipantResult().getWeaponCompetitionResult(weaponType).setPlace(4);
        }

        /* If first is greater than 1 */
        Comparator<Participant> compareByPoinst = (Participant p1,Participant p2) ->{
            try{
                RationalNumber pointsFirst = p1.getPointsForWeaponProperty(weaponType).get();
                RationalNumber pointsSecond = p2.getPointsForWeaponProperty(weaponType).get();
                return RationalNumber.compare(pointsFirst,pointsSecond);
            }catch (NoSuchWeaponException e){
                System.out.format("Some serious shit went wrong. This participant shouldn't be in the list\n");
                e.printStackTrace();
                return 0;
            }
        };

        /* Ceil placing */
        participants.sort(compareByPoinst);
        Collections.reverse(participants);
        int currenPlace = -1;
        RationalNumber lastPoints = new RationalNumber(1000000,1);
        int iteration = 4; /* Place to swap */
        for(Participant p : participants){
            try {
                p.getParticipantResult().getWeaponCompetitionResult(weaponType).setPoints(p.getPointsForWeaponProperty(weaponType).get());
            }catch (NoSuchWeaponException e){
                System.out.format("Some serious shit went wrong. This participant shouldn't be in the list\n");
                e.printStackTrace();
            }
            ParticipantResult.WeaponCompetitionResult weaponCompetitionResult = p.getParticipantResult().getWeaponCompetitionResult(weaponType);
            if (weaponCompetitionResult.getPlace() > 0){
                continue;
            }
            iteration ++;
            try{
                RationalNumber toCompare = p.getPointsForWeaponProperty(weaponType).get();
                System.out.println("$$ " + toCompare.toString());
                int compare = RationalNumber.compare(toCompare,lastPoints);
                System.out.println("$# " + compare);
                if (compare != 0 ){
                    currenPlace = iteration;
                    lastPoints = toCompare;
                }
                weaponCompetitionResult.setPlace(currenPlace);
            } catch (NoSuchWeaponException e){
                System.out.format("Some serious shit went wrong. This participant shouldn't be in the list\n");
                e.printStackTrace();
            }
        }

        /** Floor placing  */
//        participants.sort(compareByPoinst);
//        RationalNumber lastPoints = new RationalNumber(-1000000,1);
//        Integer iteration = participants.size() + 1;
//        Integer currentPlace = participants.size();
//        /* Sort should sort in ascending order that's why we start from lowest place */
//        //System.out.format("Printing calculate result for WC: " + this.weaponType.toString() + "\n");
//        for(Participant p : participants){
//            //System.out.format("#### Iteration " + iteration.toString() + "\n****Participant: " + p.getName()+ " " + p.getSurname() + "\n****Place: "+p.getParticipantResult().getWeaponCompetitionResult(weaponType).getPlace() + "\n\n");
//            /** Setting points in participant result */
//            try {
//                p.getParticipantResult().getWeaponCompetitionResult(weaponType).setPoints(p.getPointsForWeaponProperty(weaponType).get());
//            }catch (NoSuchWeaponException e){
//                System.out.format("Some serious shit went wrong. This participant shouldn't be in the list\n");
//                e.printStackTrace();
//            }
//            iteration --;
//            ParticipantResult.WeaponCompetitionResult weaponCompetitionResult = p.getParticipantResult().getWeaponCompetitionResult(weaponType);
//            if (weaponCompetitionResult.getPlace() > 0){
//                continue;
//            }
//            //System.out.format("****Skiping continue\n");
//            boolean fSwitchPlace = false;
//            try{
//                RationalNumber toCompare = p.getPointsForWeaponProperty(weaponType).get();
//                int compare = RationalNumber.compare(toCompare,lastPoints);
//                weaponCompetitionResult.setPoints(toCompare);
//                if (compare > 0){
//                    fSwitchPlace = true;
//                }
//                lastPoints = toCompare;
//            } catch (NoSuchWeaponException e){
//                System.out.format("Some serious shit went wrong. This participant shouldn't be in the list\n");
//                e.printStackTrace();
//            }
//
//            if (fSwitchPlace) {
//                currentPlace = iteration;
//            }
//            //System.out.format("****Setting place: " + currentPlace.toString() + "\n\n");
//            weaponCompetitionResult.setPlace(currentPlace);
//        }
    }
}