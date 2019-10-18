package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.KillerDrawing.KillerRandomizerStrategy;
import javafx.collections.ObservableList;
import model.enums.WeaponType;
import model.exceptions.NoSuchCompetitionException;
import model.KillerDrawing.KillerRandomizerStrategy;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.List;


public class Competition {

    private ObservableList<WeaponCompetition> weaponCompetitions= FXCollections.observableArrayList();
    private ObservableList<Participant> participants= FXCollections.observableArrayList();
    private KillerRandomizerStrategy killerRandomizerStrategy;

    private Competition(util.Pair<ObservableList<Participant>,WeaponType> participants1,
                        util.Pair<ObservableList<Participant>,WeaponType> participants2,
                        util.Pair<ObservableList<Participant>,WeaponType> participants3,
                        KillerRandomizerStrategy killerRandomizerStrategy)
    {
        weaponCompetitions.add(new WeaponCompetition(participants1.snd(),participants1.fst()));
        weaponCompetitions.add(new WeaponCompetition(participants2.snd(),participants2.fst()));
        weaponCompetitions.add(new WeaponCompetition(participants3.snd(),participants3.fst()));
        participants.addAll(participants1.fst());
        for(Participant p : participants2.fst())
        {
            if(!participants.contains(p))
            {
                participants.add(p);
            }
        }
        for(Participant p : participants3.fst())
        {
            if(!participants.contains(p))
            {
                participants.add(p);
            }
        }
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

    public WeaponCompetition getSingleWeaponCompetition(WeaponType wt)
    {
        for(WeaponCompetition wc : weaponCompetitions)
        {
            if (wc.getWeaponType()==wt)
                return wc;
        }
        throw new IllegalStateException("Asked for Weapon type not in competitions");
    }

    private static Competition instance=null;

    public static Competition getInstance()
    {
        if(instance==null)
            throw new IllegalStateException("instance not created");
        return instance;
    }

    public static Competition init(util.Pair<ObservableList<Participant>,WeaponType> participants1,
                                   util.Pair<ObservableList<Participant>,WeaponType> participants2,
                                   util.Pair<ObservableList<Participant>,WeaponType> participants3,
                                   KillerRandomizerStrategy killerRandomizerStrategy)
    {
        if(instance!=null)
            throw new InvalidStateException("Cannot reinitialize Comeptitions");
        instance= new Competition(participants1,participants2,participants3,killerRandomizerStrategy);
        return instance;
    }
    public  static Competition init(Competition deserilized)
    {
        if(instance!=null)
            throw new InvalidStateException("Cannot reinitialize Comeptitions");
        instance=deserilized;
        return instance;
    }


}
