package model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.command.Command;
import model.enums.JudgeState;
import model.enums.WeaponType;
import model.exceptions.NoSuchWeaponException;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Participant {

    public void setName(String name) {
        this.name.set(name);
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public void setLocationGroup(String locationGroup) {
        this.locationGroup.set(locationGroup);
    }

    public void setfSmallSwordParticipant(boolean fSmallSwordParticipant) {
        if(!this.fSmallSwordParticipant.getValue().equals(fSmallSwordParticipant)) {
            if(this.weaponPointsProperty.containsKey(WeaponType.SMALL_SWORD) && !fSmallSwordParticipant){
                this.weaponPointsProperty.remove(WeaponType.SMALL_SWORD);
            }else{
                this.weaponPointsProperty.put(WeaponType.SMALL_SWORD,new SimpleIntegerProperty(0));
            }
            this.fSmallSwordParticipant.setValue(fSmallSwordParticipant);
        }
    }

    public void setfSabreParticipant(boolean fSabreParticipant) {
        if(!this.fSabreParticipant.getValue().equals(fSabreParticipant)) {
            if(this.weaponPointsProperty.containsKey(WeaponType.SABRE) && !fSabreParticipant){
                this.weaponPointsProperty.remove(WeaponType.SABRE);
            }else{
                this.weaponPointsProperty.put(WeaponType.SABRE,new SimpleIntegerProperty(0));
            }
            this.fSabreParticipant.setValue(fSabreParticipant);
        }
    }

    public void setfRapierParticipant(boolean fRapierParticipant) {
        if(!this.fRapierParticipant.getValue().equals(fRapierParticipant)) {
            if(this.weaponPointsProperty.containsKey(WeaponType.RAPIER) && !fRapierParticipant){
                this.weaponPointsProperty.remove(WeaponType.RAPIER);
            }else{
                this.weaponPointsProperty.put(WeaponType.RAPIER,new SimpleIntegerProperty(0));
            }
            this.fRapierParticipant.setValue(fRapierParticipant);
        }
    }

    public void setfWeaponParticipant(WeaponType wt,boolean fparticipant){
        switch (wt){
            case SABRE: setfSabreParticipant(fparticipant);break;
            case RAPIER: setfRapierParticipant(fparticipant); break;
            case SMALL_SWORD: setfSmallSwordParticipant(fparticipant);break;
            default: throw new InvalidParameterException();
        }
    }

    private StringProperty name;
    private StringProperty surname;
    private StringProperty location;
    private StringProperty locationGroup;
    private ObjectProperty<JudgeState> judgeState;
    private ObjectProperty<Date> licenseExpDate;
    private int timesKiller=0;


    private BooleanProperty fSabreInjury=new SimpleBooleanProperty(false);
    private BooleanProperty fRapierInjury=new SimpleBooleanProperty(false);
    private BooleanProperty fSmallSwordInjury=new SimpleBooleanProperty(false);

    /** for table view required */
    private BooleanProperty fSmallSwordParticipant;
    private BooleanProperty fSabreParticipant;
    private BooleanProperty fRapierParticipant;

    /** Points for table view required */
    private ObservableMap<WeaponType,IntegerProperty> weaponPointsProperty;

    public Participant(String name, String surname, String location, String locationGroup, JudgeState judgeState, Date licenceExpDate){
        this.name            = new SimpleStringProperty(name);
        this.surname         = new SimpleStringProperty(surname);
        this.location        = new SimpleStringProperty(location);
        this.locationGroup   = new SimpleStringProperty(locationGroup);
        this.judgeState      = new SimpleObjectProperty<>(judgeState);
        this.licenseExpDate  = new SimpleObjectProperty<>(licenceExpDate);

        this.fSmallSwordParticipant = new SimpleBooleanProperty(false);
        this.fSabreParticipant = new SimpleBooleanProperty(false);
        this.fRapierParticipant = new SimpleBooleanProperty(false);

        this.weaponPointsProperty = FXCollections.observableHashMap();
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public String getLocationGroup() {
        return locationGroup.get();
    }

    public StringProperty locationGroupProperty() {
        return locationGroup;
    }
    public ObjectProperty<JudgeState> judgeStateProperty() {
        return judgeState;
    }

    public ObjectProperty<Date> licenseExpDateProperty() {
        return licenseExpDate;
    }

    public BooleanProperty fSmallSwordParticipantProperty() {
        return fSmallSwordParticipant;
    }

    public BooleanProperty fSabreParticipantProperty() {
        return fSabreParticipant;
    }

    public BooleanProperty fRapierParticipantProperty() {
        return fRapierParticipant;
    }

    public void setLicenseExpDate(Date licenceExpDate){ this.licenseExpDate.setValue(licenceExpDate); }

    public Date getLicenseExpDate() {return licenseExpDate.get(); }

    public void setJudgeState(JudgeState judgeState){ this.judgeState.setValue(judgeState); }

    public JudgeState getJudgeState(){ return judgeState.get(); }

    public boolean isInjured(WeaponType wt)
    {
        if(wt==WeaponType.RAPIER)
            return fRapierInjury.get();
        if(wt==WeaponType.SABRE)
            return fSabreInjury.get();
        if(wt==WeaponType.SMALL_SWORD)
            return fSmallSwordInjury.get();
        throw new IllegalStateException("hue");
    }

    public IntegerProperty getPointsForWeaponProperty(WeaponType type) throws NoSuchWeaponException {
        if(weaponPointsProperty.containsKey(type)) return weaponPointsProperty.get(type);
        else throw new NoSuchWeaponException();
    }

    public void addInjury(WeaponType wt,WeaponCompetition wc){wc.getcStack().executeCommand(new CommandAddInjury(wt,wc));}

    public void addInjuries(List<WeaponType> wt,WeaponCompetition wc){wc.getcStack().executeCommand(new CommandAddInjury(wt,wc));}

    public int getTimesKiller() {
        return timesKiller;
    }

    public void setTimesKiller(int timesKiller) {
        this.timesKiller = timesKiller;
    }

    public void incTimesKiller()
    {
        this.timesKiller++;
    }

    public class CommandAddInjury implements Command {
        @Override
        public void execute() {
            oldFSabreInjury=fSabreInjury.get();
            oldFRapierInjury=fRapierInjury.get();
            oldFSmallSwordInjury=fSmallSwordInjury.get();
            fSabreInjury.set(_fSabreInjury);
            fRapierInjury.set(_fRapierInjury);
            fSmallSwordInjury.set(_fSmallSwordInjury);
            commandsUsedList=competition.invalidateParticipant(Participant.this);
            for(Command c : commandsUsedList)
            {
                c.execute();
            }
            Collections.reverse(commandsUsedList);
        }

        @Override
        public void undo() {
            fSabreInjury.set(oldFSabreInjury);
            fRapierInjury.set(oldFRapierInjury);
            fSmallSwordInjury.set(oldFSmallSwordInjury);
            for (Command c : commandsUsedList) {
                c.undo();
            }
            Collections.reverse(commandsUsedList);
        }

        @Override
        public void redo() {
            oldFSabreInjury=fSabreInjury.get();
            oldFRapierInjury=fRapierInjury.get();
            oldFSmallSwordInjury=fSmallSwordInjury.get();
            fSabreInjury.set(_fSabreInjury);
            fRapierInjury.set(_fRapierInjury);
            fSmallSwordInjury.set(_fSmallSwordInjury);
            for(Command c : commandsUsedList)
            {
                c.redo();
            }
            Collections.reverse(commandsUsedList);
        }

        private boolean oldFSabreInjury=false;
        private boolean oldFRapierInjury=false;
        private boolean oldFSmallSwordInjury=false;
        private boolean _fSabreInjury=false; /** _ To differentie them for fSabreInjury in the outer class**/
        private boolean _fRapierInjury=false;
        private boolean _fSmallSwordInjury=false;
        private WeaponCompetition competition;
        private List<Command> commandsUsedList=null;


        private CommandAddInjury(List<WeaponType> weaponList,WeaponCompetition competition)
        {
            this.competition=competition;
            if(weaponList.contains(WeaponType.SABRE))
                _fSabreInjury=true;
            if(weaponList.contains(WeaponType.RAPIER))
                _fRapierInjury=true;
            if(weaponList.contains(WeaponType.SMALL_SWORD))
                _fSmallSwordInjury=true;
        }

        private CommandAddInjury(WeaponType single,WeaponCompetition competition)
        {
            this.competition=competition;
            if(single==WeaponType.SABRE)
                _fSabreInjury=true;
            else if(single==WeaponType.RAPIER)
                _fRapierInjury=true;
            else if(single==WeaponType.SMALL_SWORD)
                _fSmallSwordInjury=true;
        }




    }

}