package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.KillerDrawing.KillerRandomizerStrategy;
import model.enums.WeaponType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Competition implements Serializable {

    private static Competition instance = null;
    private ObservableList<WeaponCompetition> weaponCompetitions = FXCollections.observableArrayList();
    private ObservableList<Participant> participants = FXCollections.observableArrayList();
    private KillerRandomizerStrategy killerRandomizerStrategy;
    private String competitionName;
    private AppMode mode;
    private String password;

    private static final long serialVersionUID = 6529685098267757690L;



    public Competition(util.Pair<ObservableList<Participant>,WeaponType> participants1,
                        util.Pair<ObservableList<Participant>,WeaponType> participants2,
                        util.Pair<ObservableList<Participant>,WeaponType> participants3,
                        KillerRandomizerStrategy killerRandomizerStrategy, String password)
    {
        System.out.println("muuuuuuuuu");
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

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Competition) {
            List<Participant> otherParticipants = ((Competition) obj).getParticipants();
            boolean found;
            for (Participant p1 : getParticipants()) {
                found = false;
                for (Participant p2: otherParticipants){
                    if (p1.equals(p2)) { found = true; break; };
                    System.out.println(p2.getName());
                }
                System.out.println(p1.getName());
                if (!found) return false;
            }

            List<WeaponCompetition> otherWeaponCompetitions = ((Competition) obj).getWeaponCompetitions();
            for (WeaponCompetition w1 : getWeaponCompetitions()) {
                found = false;
                for (WeaponCompetition w2: otherWeaponCompetitions){
                    if (w1.equals(w2)) { found = true; break; };
                }
                if (!found) return false;
            }
            return true;
        }
        return false;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
       // stream.writeObject(password);
        ArrayList<WeaponCompetition> weaponCompetitionArrayList = new ArrayList<>();
        weaponCompetitions.forEach(wc -> weaponCompetitionArrayList.add(wc)) ;
        stream.writeObject(weaponCompetitionArrayList);

        ArrayList<Participant> participants = new ArrayList<>();
        this.participants.forEach(p -> participants.add(p)) ;
        stream.writeObject(participants);
        stream.writeObject(killerRandomizerStrategy);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        //password = (String) stream.readObject();
        weaponCompetitions = FXCollections.observableArrayList((ArrayList<WeaponCompetition>) stream.readObject());
        participants = FXCollections.observableArrayList((ArrayList<Participant>) stream.readObject());
        killerRandomizerStrategy = (KillerRandomizerStrategy) stream.readObject();
    }

    public ObservableList<Participant> getParticipantsObservableList(){ return participants; }

    public WeaponCompetition getWeaponCompetition(WeaponType wt){
        return weaponCompetitions.stream().filter(wc -> wc.getWeaponType().equals(wt)).findFirst().get();
    }

    public String getCompetitionName() { return competitionName; }

    public void setCompetitionName(String competitionName) { this.competitionName = competitionName; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}
