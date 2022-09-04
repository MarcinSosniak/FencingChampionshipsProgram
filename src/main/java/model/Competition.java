package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.KillerDrawing.KillerRandomizerStrategy;
import model.enums.WeaponType;
import util.ObservableUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class Competition implements Serializable {

    private static Competition instance = null;
    private ObservableList<WeaponCompetition> weaponCompetitions = FXCollections.observableArrayList();
    private ObservableList<Participant> participants = FXCollections.observableArrayList();
    private KillerRandomizerStrategy killerRandomizerStrategy;
    private String competitionName;
    private String password;

    private static final long serialVersionUID = 6529685098267757690L;



    public Competition(util.Pair<ObservableList<Participant>,WeaponType> participants1,
                        util.Pair<ObservableList<Participant>,WeaponType> participants2,
                        util.Pair<ObservableList<Participant>,WeaponType> participants3,
                        KillerRandomizerStrategy killerRandomizerStrategy, String password)
    {
        weaponCompetitions.add(new WeaponCompetition(participants1.snd(),participants1.fst()));
        weaponCompetitions.add(new WeaponCompetition(participants2.snd(),participants2.fst()));
        weaponCompetitions.add(new WeaponCompetition(participants3.snd(),participants3.fst()));
        participants.addAll(participants1.fst());
        for (Participant p : participants2.fst()) {
            if (!participants.contains(p)) {
                participants.add(p);
            }
        }

        for (Participant p : participants3.fst()) {
            if(!participants.contains(p)) {
                participants.add(p);
            }
        }
        this.killerRandomizerStrategy = killerRandomizerStrategy;
        this.password = password;
        AppMode.init(password);
    }

    public static Competition init(util.Pair<ObservableList<Participant>,WeaponType> participants1,
                                   util.Pair<ObservableList<Participant>,WeaponType> participants2,
                                   util.Pair<ObservableList<Participant>,WeaponType> participants3,
                                   KillerRandomizerStrategy killerRandomizerStrategy, String password) {
        if (instance != null)
            throw new IllegalStateException("Cannot reinitialize Comeptitions");
        instance= new Competition(participants1,participants2,participants3,killerRandomizerStrategy, password);
        return instance;
    }

    public static Competition init(Competition deserialized) {
        if (instance != null)
            throw new IllegalStateException("Cannot reinitialize Comeptition");
        instance = deserialized;
        return instance;
    }

    public static Competition getInstance() {
        if (instance == null) throw new IllegalStateException("instance not created");
        return instance;
    }

    public static void nullInstance(){
        instance = null;
    }

    public List<Participant> getParticipants() { return participants; }

    public List<WeaponCompetition> getWeaponCompetitions() { return weaponCompetitions; }


    public WeaponCompetition getSingleWeaponCompetition(WeaponType wt) {
        for (WeaponCompetition wc : weaponCompetitions) {
            if (wc.getWeaponType() == wt) {
                return wc;
            }
        }
        throw new IllegalStateException("Asked for Weapon type not in competitions");
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(ObservableUtils.toList(weaponCompetitions));
        stream.writeObject(ObservableUtils.toList(participants));
        stream.writeObject(killerRandomizerStrategy);
        stream.writeObject(competitionName);
        stream.writeObject(password);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        weaponCompetitions = FXCollections.observableArrayList((List<WeaponCompetition>) stream.readObject());
        participants = FXCollections.observableArrayList((List<Participant>) stream.readObject());
        killerRandomizerStrategy = (KillerRandomizerStrategy) stream.readObject();
        competitionName = (String) stream.readObject();
        password = (String) stream.readObject();
    }

    public ObservableList<Participant> getParticipantsObservableList(){ return participants; }

    public WeaponCompetition getWeaponCompetition(WeaponType wt){
        return weaponCompetitions.stream().filter(wc -> wc.getWeaponType().equals(wt)).findFirst().get();
    }

    public String getCompetitionName() { return competitionName; }

    public void setCompetitionName(String competitionName) { this.competitionName = competitionName; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    /** For final results required */
    public void calculateResults() {
        for(Participant p : participants){
            ParticipantResult pr = new ParticipantResult(p);
            p.setParticipantResult(pr);
        }

        for(WeaponCompetition wc: weaponCompetitions){
            wc.calculateResults();
        }
        /* Calculating triathlon results*/
        for(Participant p : participants){
            p.getParticipantResult().calculateTriathlonPoints();
            System.out.println("#####");
            System.out.println(p.getParticipantResult().triathlonOpenPointsProperty().get());
            System.out.println(p.getParticipantResult().triathlonWomenPointsProperty().get());
        }
        /* Now we need to set place for each triathlon */
        Comparator<Participant> compareTriathlonOpen = (Participant p1, Participant p2) ->{
            int point1 = p1.getParticipantResult().getTriathlonOpenPoints();
            int point2 = p2.getParticipantResult().getTriathlonOpenPoints();
            if (point1 == point2){
                return 0;
            }else if (point1 > point2){
                return 1;
            }else {
                return -1;
            }
        };
        Comparator<Participant> compareTriathlonWomen = (Participant p1, Participant p2) ->{
            int point1 = p1.getParticipantResult().getTriathlonWomenPoints();
            int point2 = p2.getParticipantResult().getTriathlonWomenPoints();
            if (point1 == point2){
                return 0;
            }else if (point1 > point2){
                return 1;
            }else {
                return -1;
            }
        };

        /** floor place */
//        /* Now we need to assign place to each participant in OPEN triathlon */
//        participants.sort(compareTriathlonOpen);
//        Integer currentPlace = participants.size();
//        int lastPoints = -1000;
//        int iteration = participants.size() + 1;
//
//        for(Participant p : participants){
//            iteration --;
//            boolean fSwitchPlace = false;
//            int toCompare = p.getParticipantResult().getTriathlonOpenPoints();
//            if (toCompare > lastPoints){
//                fSwitchPlace = true;
//            }
//            lastPoints = toCompare;
//            if (fSwitchPlace) {
//                currentPlace = iteration;
//            }
//            p.getParticipantResult().setTriathlonOpen(currentPlace.toString());
//        }
//
//        /* Now we need to assign place to each participant in WOMEN triathlon*/
//        participants.sort(compareTriathlonWomen);
//        currentPlace = participants.size();
//        lastPoints = -1000;
//
//        /* Here we should have only women's places iteration is no longer valid name */
//        iteration = 1;
//        for(Participant p : participants){
//            if(p.isfFemale())
//                iteration++;
//        }
//
//        for(Participant p : participants){
//            if(!p.isfFemale()){
//                p.getParticipantResult().triathlonWomenPointsProperty().set(-1);
//                p.getParticipantResult().setTriathlonWomen("--");
//                continue;
//            }else{
//                p.getParticipantResult().triathlonWomenPointsProperty().set(p.getParticipantResult().getTriathlonOpenPoints());
//            }
//            iteration --;
//            boolean fSwitchPlace = false;
//            int toCompare = p.getParticipantResult().getTriathlonWomenPoints();
//            if (toCompare > lastPoints){
//                fSwitchPlace = true;
//            }
//            lastPoints = toCompare;
//            if (fSwitchPlace) {
//                currentPlace = iteration;
//            }
//            p.getParticipantResult().setTriathlonWomen(currentPlace.toString());
//        }


        /** Ceil place */
        participants.sort(compareTriathlonOpen);
        Collections.reverse(participants);


        int currentPlace = -1;
        int lastPoints = 10000;
        int iteration = 0;
        //boolean fSwitchPlace = false;
        for(Participant p: participants){
            iteration ++;
            //fSwitchPlace = false;
            if(lastPoints > p.getParticipantResult().getTriathlonOpenPoints()){
                currentPlace = iteration;
            }
            lastPoints = p.getParticipantResult().getTriathlonOpenPoints();
            p.getParticipantResult().setTriathlonOpen(Integer.toString(currentPlace));
        }

        participants.sort(compareTriathlonWomen);
        Collections.reverse(participants);

        currentPlace = -1;
        iteration = 0;
        lastPoints = 10000;
        for(Participant p:participants){
            if(!p.isfFemale()) {
                p.getParticipantResult().setTriathlonWomen("--");
                continue;
            }
            iteration ++;
            if(lastPoints > p.getParticipantResult().getTriathlonOpenPoints()){
                currentPlace = iteration;
            }
            lastPoints = p.getParticipantResult().getTriathlonOpenPoints();
            p.getParticipantResult().setTriathlonWomen(Integer.toString(currentPlace));
        }
    }

    @Override
    public String toString() {
        return "Competition{" +
                "weaponCompetitions=" + weaponCompetitions +
                ", participants=" + participants +
                ", killerRandomizerStrategy=" + killerRandomizerStrategy +
                ", competitionName='" + competitionName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Competition)) return false;
        Competition that = (Competition) o;
        return ObservableUtils.equals(weaponCompetitions, that.weaponCompetitions)
                && ObservableUtils.equals(participants, that.participants)
                && Objects.equals(killerRandomizerStrategy, that.killerRandomizerStrategy)
                && Objects.equals(competitionName, that.competitionName)
                && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weaponCompetitions, participants, killerRandomizerStrategy, competitionName, password);
    }
}
