package model;

import javafx.collections.ObservableList;
import model.KillerDrawing.KillerRandomizerStrategy;
import javafx.collections.ObservableList;
import model.enums.WeaponType;
import model.exceptions.NoSuchCompetitionException;
import model.KillerDrawing.KillerRandomizerStrategy;
import java.util.List;


public class Competition {

    private ObservableList<WeaponCompetition> weaponCompetitions;
    private ObservableList<Participant> participants;
    private KillerRandomizerStrategy killerRandomizerStrategy;

    public Competition(ObservableList<WeaponCompetition> competitions, ObservableList<Participant> participants, KillerRandomizerStrategy killerRandomizerStrategy){
        this.weaponCompetitions = competitions;
        this.participants = participants;
        this.killerRandomizerStrategy = killerRandomizerStrategy;
    }

    public void changeKillerRandomizerStrategy(KillerRandomizerStrategy killerRandomizerStrategy){
        this.killerRandomizerStrategy = killerRandomizerStrategy;
    }

    public KillerRandomizerStrategy getKillerRandomizerStrategy() { return killerRandomizerStrategy; }

    public List<Participant> getParticipants() { return participants; }

    public List<WeaponCompetition> getWeaponCompetitions() { return weaponCompetitions; }

    public WeaponCompetition getWeaponCompetition(WeaponType wt) throws NoSuchCompetitionException {
        for(WeaponCompetition wc:this.weaponCompetitions){
            if(wc.getWeaponType().equals(wt)){
                return wc;
            }
        }
        throw new NoSuchCompetitionException();
    }
}
