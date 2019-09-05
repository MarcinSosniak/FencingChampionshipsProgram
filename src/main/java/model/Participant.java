package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import model.enums.JudgeState;
import model.enums.WeaponType;
import model.exceptions.NoSuchWeaponException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Participant {

    private StringProperty name;
    private StringProperty surname;
    private StringProperty location;
    private StringProperty locationGroup;
    private ObjectProperty<JudgeState> judgeState;
    private ObjectProperty<Date> licenseExpDate;
    private HashMap<WeaponType,Integer> weaponsPointsMap;

    public Participant(String name, String surname, String location, String locationGroup, JudgeState judgeState, Date licenceExpDate){
        this.name.setValue(name);
        this.surname.setValue(surname);
        this.location.setValue(location);
        this.locationGroup.setValue(locationGroup);
        this.judgeState.setValue(judgeState);
        this.licenseExpDate.setValue(licenceExpDate);
        this.weaponsPointsMap = new HashMap<>();
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
        if (!weaponsPointsMap.keySet().contains(WeaponType.SABRE)) return false;
        return true;
    }

    public boolean isSmallSwordCompetitor(){
        if (!weaponsPointsMap.keySet().contains(WeaponType.SMALL_SWORD)) return false;
        return true;
    }

    public boolean isRapierCompetitor(){
        if (!weaponsPointsMap.keySet().contains(WeaponType.RAPIER)) return false;
        return true;
    }


}