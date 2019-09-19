package model;

import javafx.beans.property.*;
import model.enums.JudgeState;
import model.enums.WeaponType;
import model.exceptions.NoSuchWeaponException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Participant {

    private StringProperty name;

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
        this.fSmallSwordParticipant.set(fSmallSwordParticipant);
    }

    public void setfSabreParticipant(boolean fSabreParticipant) {
        this.fSabreParticipant.set(fSabreParticipant);
    }

    public void setfRapierParticipant(boolean fRapierParticipant) {
        this.fRapierParticipant.set(fRapierParticipant);
    }

    public void setWeaponsPointsMap(HashMap<WeaponType, Integer> weaponsPointsMap) {
        this.weaponsPointsMap = weaponsPointsMap;
    }

    private StringProperty surname;
    private StringProperty location;
    private StringProperty locationGroup;
    private ObjectProperty<JudgeState> judgeState;
    private ObjectProperty<Date> licenseExpDate;


    /** for table view required */
    private BooleanProperty fSmallSwordParticipant;
    private BooleanProperty fSabreParticipant;
    private BooleanProperty fRapierParticipant;

    private HashMap<WeaponType,Integer> weaponsPointsMap;

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

        this.weaponsPointsMap= new HashMap<>();
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
        System.out.format("xDDD" + fSabreParticipant + "XDDDD\n");
        return fSabreParticipant;
    }

    public BooleanProperty fRapierParticipantProperty() {
        return fRapierParticipant;
    }



    public void setLicenseExpDate(Date licenceExpDate){ this.licenseExpDate.setValue(licenceExpDate); }

    public Date getLicenseExpDate() {return licenseExpDate.get(); }

    public void setJudgeState(JudgeState judgeState){ this.judgeState.setValue(judgeState); }

    public JudgeState getJudgeState(){ return judgeState.get(); }

    public void addWeaponsToWeaponsPointsMap(List<WeaponType> weaponTypeList){
        for (WeaponType weaponType: weaponTypeList){
            weaponsPointsMap.putIfAbsent(weaponType, 0);
        }
    }

    public int getPointsForWeapon(WeaponType type) throws NoSuchWeaponException {
        if (weaponsPointsMap.containsKey(type)) return weaponsPointsMap.get(type);
        else throw new NoSuchWeaponException();
    }

    public void addPointsForWeapon(WeaponType weaponType, int points) throws NoSuchWeaponException {
        if (weaponsPointsMap.containsKey(weaponType)) weaponsPointsMap.replace(weaponType, weaponsPointsMap.get(weaponType) + points);
        else throw new NoSuchWeaponException();
    }

    public void takePointsForWeapon(WeaponType weaponType, int points) throws NoSuchWeaponException{
        if (weaponsPointsMap.containsKey(weaponType)) weaponsPointsMap.replace(weaponType, weaponsPointsMap.get(weaponType) - points);
        else throw new NoSuchWeaponException();
    }


    public boolean isSabreCompetitor(){
        fSabreParticipant.setValue(!weaponsPointsMap.keySet().contains(WeaponType.SABRE));
        if (!weaponsPointsMap.keySet().contains(WeaponType.SABRE)) return false;
        return true;
    }

    public boolean isSmallSwordCompetitor(){
        fSmallSwordParticipant.setValue(!weaponsPointsMap.keySet().contains(WeaponType.SMALL_SWORD));
        if (!weaponsPointsMap.keySet().contains(WeaponType.SMALL_SWORD)) return false;
        return true;
    }

    public boolean isRapierCompetitor(){
        fRapierParticipant.setValue(!weaponsPointsMap.keySet().contains(WeaponType.RAPIER));
        if (!weaponsPointsMap.keySet().contains(WeaponType.RAPIER)) return false;
        return true;
    }


}