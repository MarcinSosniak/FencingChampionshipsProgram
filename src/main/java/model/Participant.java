package model;

import javafx.beans.property.*;
import model.command.ValidInvocationChecker;
import model.config.ConfigReader;
import model.enums.JudgeState;
import model.enums.WeaponType;
import model.exceptions.NoSuchWeaponException;
import util.ObservableUtils;
import util.RationalNumber;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.*;

public class Participant implements Serializable{

    private static final long serialVersionUID = 3;
    private StringProperty name;
    private StringProperty surname;
    private StringProperty location;
    private StringProperty locationGroup;
    private ObjectProperty<JudgeState> judgeState;
    private ObjectProperty<Date> licenseExpDate;
    /** For final results required */
    private BooleanProperty fFemale = new SimpleBooleanProperty(false);

    private transient int timesKiller = 0;

    /** for table view required */
    private transient BooleanProperty fSmallSwordParticipant;
    private transient BooleanProperty fSabreParticipant;
    private transient BooleanProperty fRapierParticipant;
    public transient StringProperty fSmallSwordParticipantSProperty;
    public transient StringProperty fSabreParticipantSProperty;
    public transient StringProperty fRapierParticipantSProperty;
    private transient Map<WeaponType, ObjectProperty<util.RationalNumber>> weaponPointsProperty;
    private Map<WeaponType,ObjectProperty<RationalNumber>> oldSeasonWeaponPointsProperty = new HashMap<>();

    private transient SimpleObjectProperty<ParticipantResult> participantResult;

    private transient BooleanProperty fSabreInjury = new SimpleBooleanProperty(false);
    private transient BooleanProperty fRapierInjury = new SimpleBooleanProperty(false);
    private transient BooleanProperty fSmallSwordInjury = new SimpleBooleanProperty(false);

    public static String tick = "\u2713";
    public static String cross = "\u2718";

    public Participant(String name, String surname, String location, String locationGroup, JudgeState judgeState, Date licenceExpDate
            ,int oldPointsSmallSword , int oldPointsSabre,int oldPointsRapier){
        this.name            = new SimpleStringProperty(name);
        this.surname         = new SimpleStringProperty(surname);
        this.location        = new SimpleStringProperty(location);
        this.locationGroup   = new SimpleStringProperty(locationGroup);
        this.judgeState      = new SimpleObjectProperty<>(judgeState);
        this.licenseExpDate  = new SimpleObjectProperty<>(licenceExpDate);

        this.fSmallSwordParticipant = new SimpleBooleanProperty(false);
        this.fSmallSwordParticipantSProperty = new SimpleStringProperty(cross);

        this.fSabreParticipant = new SimpleBooleanProperty(false);
        this.fSabreParticipantSProperty = new SimpleStringProperty(cross);


        this.fRapierParticipant = new SimpleBooleanProperty(false);
        this.fRapierParticipantSProperty = new SimpleStringProperty(cross);


        this.weaponPointsProperty = new HashMap<>();
        participantResult = new SimpleObjectProperty<>(new ParticipantResult(this));
        oldSeasonWeaponPointsProperty.put(WeaponType.RAPIER,new SimpleObjectProperty<>(new RationalNumber(oldPointsRapier)));
        oldSeasonWeaponPointsProperty.put(WeaponType.SABRE,new SimpleObjectProperty<>(new RationalNumber(oldPointsSabre)));
        oldSeasonWeaponPointsProperty.put(WeaponType.SMALL_SWORD,new SimpleObjectProperty<>(new RationalNumber(oldPointsSmallSword)));
    }

    /** Updating only transcient fields because when we read from json we still want to read them */
    public void update(){
        this.timesKiller = 0;
        this.weaponPointsProperty = new HashMap<>();

        if(ConfigReader.getInstance().getBooleanValue("TEST","DEFAULT_SMALL_SWORD_COMPETITOR",false)) {
            this.fSmallSwordParticipant = new SimpleBooleanProperty(true);
            this.fSmallSwordParticipantSProperty = new SimpleStringProperty(tick);
        }
        else
        {
            this.fSmallSwordParticipant = new SimpleBooleanProperty(false);
            this.fSmallSwordParticipantSProperty = new SimpleStringProperty(cross);
        }

        if(ConfigReader.getInstance().getBooleanValue("TEST","DEFAULT_SABRE_COMPETITOR",false)) {
            this.fSabreParticipant = new SimpleBooleanProperty(true);
            this.fSabreParticipantSProperty = new SimpleStringProperty(tick);
        }
        else
        {
            this.fSabreParticipant = new SimpleBooleanProperty(false);
            this.fSabreParticipantSProperty = new SimpleStringProperty(cross);
        }

        if(ConfigReader.getInstance().getBooleanValue("TEST","DEFAULT_RAPIER_COMPETITOR",false)) {
            this.fRapierParticipant = new SimpleBooleanProperty(true);
            this.fRapierParticipantSProperty = new SimpleStringProperty(tick);
        }
        else
        {
            this.fRapierParticipant = new SimpleBooleanProperty(false);
            this.fRapierParticipantSProperty = new SimpleStringProperty(cross);
        }

        this.weaponPointsProperty.put(WeaponType.RAPIER,new SimpleObjectProperty<RationalNumber>(new RationalNumber()));
        this.weaponPointsProperty.put(WeaponType.SABRE,new SimpleObjectProperty<RationalNumber>(new RationalNumber()));
        this.weaponPointsProperty.put(WeaponType.SMALL_SWORD,new SimpleObjectProperty<RationalNumber>(new RationalNumber()));

        this.participantResult = new SimpleObjectProperty<>(new ParticipantResult(this));
        this.fSabreInjury = new SimpleBooleanProperty(false);
        this.fSmallSwordInjury = new SimpleBooleanProperty(false);
        this.fRapierInjury = new SimpleBooleanProperty(false);

    }
    /*
    public Date createDateFromString(String dateS) throws ParseException{
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", new Locale("pl"));
        Date date = format.parse(dateS);
        System.out.println("date: " + date.toString());
        return date;
    }

    public JudgeState setJudgeStateFromString(String judgeS){
        if (judgeS.equals(JudgeState.NON_JUDGE.toString()) || judgeS.equals(JudgeState.MAIN_JUDGE.toString()))
            return JudgeState.valueOf(judgeS);
        throw new IllegalArgumentException("Incorrect judge state string");
    }*/

    public void setParticipantResult(ParticipantResult participantResult) {
        this.participantResult.set(participantResult);
    }

    public void setName(String name) { this.name.set(name); }

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
        if (!this.fSmallSwordParticipant.getValue().equals(fSmallSwordParticipant)) {
            if (this.weaponPointsProperty.containsKey(WeaponType.SMALL_SWORD) && !fSmallSwordParticipant)
                this.weaponPointsProperty.remove(WeaponType.SMALL_SWORD);
            else this.weaponPointsProperty.put(WeaponType.SMALL_SWORD, new SimpleObjectProperty<>(new RationalNumber(0)));

            this.fSmallSwordParticipant.setValue(fSmallSwordParticipant);
            if (fSmallSwordParticipant) this.fSmallSwordParticipantSProperty.setValue(tick);
            else this.fSmallSwordParticipantSProperty.setValue(cross);
        }
    }

    public void setfSabreParticipant(boolean fSabreParticipant) {
        if (!this.fSabreParticipant.getValue().equals(fSabreParticipant)) {
            if (this.weaponPointsProperty.containsKey(WeaponType.SABRE) && !fSabreParticipant)
                this.weaponPointsProperty.remove(WeaponType.SABRE);
            else this.weaponPointsProperty.put(WeaponType.SABRE, new SimpleObjectProperty<>(new RationalNumber(0)));

            this.fSabreParticipant.setValue(fSabreParticipant);

            if (fSabreParticipant) this.fSabreParticipantSProperty.setValue(tick);
            else this.fSabreParticipantSProperty.setValue(cross);
        }
    }

    public void setfRapierParticipant(boolean fRapierParticipant) {
        if (!this.fRapierParticipant.getValue().equals(fRapierParticipant)){
            if (this.weaponPointsProperty.containsKey(WeaponType.RAPIER) && !fRapierParticipant)
                this.weaponPointsProperty.remove(WeaponType.RAPIER);
            else this.weaponPointsProperty.put(WeaponType.RAPIER, new SimpleObjectProperty<>(new RationalNumber(0)));

            this.fRapierParticipant.setValue(fRapierParticipant);
            if (fRapierParticipant) this.fRapierParticipantSProperty.setValue(tick);
            else this.fRapierParticipantSProperty.setValue(cross);
        }
    }

    public void setfWeaponParticipant(WeaponType wt, boolean fparticipant){
        switch (wt){
            case SABRE: setfSabreParticipant(fparticipant);break;
            case RAPIER: setfRapierParticipant(fparticipant); break;
            case SMALL_SWORD: setfSmallSwordParticipant(fparticipant);break;
            default: throw new InvalidParameterException();
        }
    }

    public void setOldSeasonWeapoPointsPropety(WeaponType wt,int points)
    {
        oldSeasonWeaponPointsProperty.get(wt).setValue(new RationalNumber(points));

    }

    /** For final results required */
    public void setfFemale(boolean fFemale) {
        this.fFemale.set(fFemale);
    }

    public boolean isfFemale() {
        return fFemale.get();
    }

    public BooleanProperty fFemaleProperty() {
        return fFemale;
    }

    public ParticipantResult getParticipantResult() {
        return participantResult.get();
    }

    public SimpleObjectProperty<ParticipantResult> participantResultProperty() {
        return participantResult;
    }


    public String getName() { return name.get(); }

    public StringProperty nameProperty() { return name; }

    public String getSurname() { return surname.get(); }

    public StringProperty surnameProperty() { return surname; }

    public String getLocation() { return location.get(); }

    public StringProperty locationProperty() { return location; }

    public String getLocationGroup() { return locationGroup.get(); }

    public StringProperty locationGroupProperty() { return locationGroup; }

    public ObjectProperty<JudgeState> judgeStateProperty() { return judgeState; }

    public ObjectProperty<Date> licenseExpDateProperty() { return licenseExpDate; }

    public BooleanProperty fSmallSwordParticipantProperty() { return fSmallSwordParticipant; }

    public BooleanProperty fSabreParticipantProperty() { return fSabreParticipant; }

    public BooleanProperty fRapierParticipantProperty() { return fRapierParticipant; }

    public void setLicenseExpDate(Date licenceExpDate){ this.licenseExpDate.setValue(licenceExpDate); }

    public Date getLicenseExpDate() {return licenseExpDate.get(); }

    public void setJudgeState(JudgeState judgeState){ this.judgeState.setValue(judgeState); }

    public JudgeState getJudgeState(){ return judgeState.get(); }

    public ObjectProperty<RationalNumber> getPointsForWeaponProperty(WeaponType type) throws NoSuchWeaponException {
        if (weaponPointsProperty.containsKey(type))
            return weaponPointsProperty.get(type);
        else throw new NoSuchWeaponException();
    }

    public ObjectProperty<RationalNumber> getOldSeasonPointsForWeaponProperty(WeaponType type){
        return oldSeasonWeaponPointsProperty.get(type);
    }

    public void addPointsForWeapon(ValidInvocationChecker checker, WeaponType type, RationalNumber points){
        Objects.requireNonNull(checker);
        if (weaponPointsProperty.containsKey(type)) {
           // System.out.println("adding points in partcipant: " + this);
            weaponPointsProperty.get(type).setValue(weaponPointsProperty.get(type).get().add(points));
        }
    }

    public void subtractPointsFromWeapon(ValidInvocationChecker checker, WeaponType type, RationalNumber points){
        Objects.requireNonNull(checker);
        if (weaponPointsProperty.containsKey(type)) {
            System.out.println("subtract points in partcipant");
            weaponPointsProperty.get(type).setValue(weaponPointsProperty.get(type).get().substract(points));
        }
    }

    public int getTimesKiller() { return timesKiller; }

    public void incTimesKiller() { this.timesKiller++; }

    public boolean isInjured(WeaponType wt) {
        if (wt == WeaponType.RAPIER) return fRapierInjury.get();
        if (wt == WeaponType.SABRE) return fSabreInjury.get();
        if (wt == WeaponType.SMALL_SWORD) return fSmallSwordInjury.get();
        throw new IllegalStateException("Invalid state");
    }

    // SHOULD BE SET ONLY THROUGH COMMAND
    public void setfRapierInjury(ValidInvocationChecker checker, boolean fRapierInjury) {
        Objects.requireNonNull(checker);
        this.fRapierInjury.setValue(fRapierInjury);
    }

    public void setfSabreInjury(ValidInvocationChecker checker, boolean fSabreInjury) {
        Objects.requireNonNull(checker);
        this.fSabreInjury.setValue(fSabreInjury);
    }

    public void setfSmallSwordInjury(ValidInvocationChecker checker, boolean fSmallSwordInjury) {
        Objects.requireNonNull(checker);
        this.fSmallSwordInjury.setValue(fSmallSwordInjury);
    }

    public ObjectProperty<RationalNumber> getPointsForWeaponPropertyLastRound(WeaponType type) throws NoSuchWeaponException {
        try {
            //return rapierPoints;
            return this.weaponPointsProperty.get(type);
        }
        catch (Exception ex) {
            throw new NoSuchWeaponException();
        }
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() * 4 + this.surname.hashCode() * 17 +
            this.locationGroup.hashCode() + this.location.hashCode() * 2 +
            this.judgeState.hashCode() + this.licenseExpDate.hashCode() * 3;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(name.get());
        stream.writeObject(surname.get());
        stream.writeObject(location.get());
        stream.writeObject(locationGroup.get());
        stream.writeObject(judgeState.get());
        stream.writeObject(licenseExpDate.get());
        stream.writeBoolean(fSmallSwordParticipant.get());
        stream.writeBoolean(fRapierParticipant.get());
        stream.writeBoolean(fSabreParticipant.get());
        stream.writeBoolean(fRapierInjury.get());
        stream.writeBoolean(fSabreInjury.get());
        stream.writeBoolean(fSmallSwordInjury.get());
        stream.writeInt(timesKiller);
        stream.writeObject(ObservableUtils.toMap(weaponPointsProperty));
        stream.writeObject(ObservableUtils.toMap(oldSeasonWeaponPointsProperty));
        /** For final results required */
        stream.writeObject(fFemale.get());
        stream.writeObject(participantResult.get());
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        name = new SimpleStringProperty((String) stream.readObject());
        surname = new SimpleStringProperty((String) stream.readObject());
        location = new SimpleStringProperty((String) stream.readObject());
        locationGroup = new SimpleStringProperty((String) stream.readObject());
        judgeState = new SimpleObjectProperty<>((JudgeState) stream.readObject());
        licenseExpDate = new SimpleObjectProperty<>((Date) stream.readObject());
        fSmallSwordParticipant = new SimpleBooleanProperty(stream.readBoolean());
        fSmallSwordParticipantSProperty = ObservableUtils.toSimpleStringProperty(fSmallSwordParticipant);
        fRapierParticipant = new SimpleBooleanProperty(stream.readBoolean());
        fRapierParticipantSProperty = ObservableUtils.toSimpleStringProperty(fRapierParticipant);
        fSabreParticipant = new SimpleBooleanProperty(stream.readBoolean());
        fSabreParticipantSProperty = ObservableUtils.toSimpleStringProperty(fSabreParticipant);
        fRapierInjury = new SimpleBooleanProperty(stream.readBoolean());
        fSabreInjury = new SimpleBooleanProperty(stream.readBoolean());
        fSmallSwordInjury = new SimpleBooleanProperty(stream.readBoolean());
        timesKiller = stream.readInt();
        weaponPointsProperty = ObservableUtils.toObservableMap((Map<WeaponType, RationalNumber>) stream.readObject());
        oldSeasonWeaponPointsProperty = ObservableUtils.toObservableMap((Map<WeaponType, RationalNumber>) stream.readObject());
        /** For final results required */
        fFemale = new SimpleBooleanProperty((Boolean) stream.readObject());
        participantResult = new SimpleObjectProperty<>((ParticipantResult) stream.readObject());
    }

    private boolean equals2(Participant p){
        if(this.getSurname().equals(p.getSurname()) && this.getName().equals(p.getName())){
            return true;
        }
        return false;
    }

    public static boolean checkFNewUnique(Participant newParticipant){
        List<Participant> participants = Competition.getInstance().getParticipants();
        for(Participant p: participants){
            if(p.equals2(newParticipant))
                return false;
        }
        return true;
    }

    public static boolean checkFCanEdit(Participant old,String editedName,String editedSurname){
        List<Participant> participants = Competition.getInstance().getParticipants();
        if(old.getName().equals(editedName) && old.getSurname().equals(editedSurname)){
            return true;
        }
        for(Participant p: participants){
            if(p.getName().equals(editedName) && p.getSurname().equals(editedSurname))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "name=" + name +
                ", surname=" + surname +
                ", location=" + location +
                ", locationGroup=" + locationGroup +
                ", judgeState=" + judgeState +
                ", licenseExpDate=" + licenseExpDate +
                ", fFemale=" + fFemale +
                ", timesKiller=" + timesKiller +
                ", fSmallSwordParticipant=" + fSmallSwordParticipant +
                ", fSabreParticipant=" + fSabreParticipant +
                ", fRapierParticipant=" + fRapierParticipant +
                ", fSmallSwordParticipantSProperty=" + fSmallSwordParticipantSProperty +
                ", fSabreParticipantSProperty=" + fSabreParticipantSProperty +
                ", fRapierParticipantSProperty=" + fRapierParticipantSProperty +
                ", weaponPointsProperty=" + weaponPointsProperty +
                ", oldSeasonWeaponPointsPropety=" + oldSeasonWeaponPointsProperty +
                ", participantResult=" + participantResult +
                ", fSabreInjury=" + fSabreInjury +
                ", fRapierInjury=" + fRapierInjury +
                ", fSmallSwordInjury=" + fSmallSwordInjury +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;
        Participant that = (Participant) o;
        return timesKiller == that.timesKiller
            && ObservableUtils.equals(name, that.name)
            && ObservableUtils.equals(surname, that.surname)
            && ObservableUtils.equals(location, that.location)
            && ObservableUtils.equals(locationGroup, that.locationGroup)
            && ObservableUtils.equals(judgeState, that.judgeState)
            && ObservableUtils.equals(licenseExpDate, that.licenseExpDate)
            && ObservableUtils.equals(fFemale, that.fFemale)
            && ObservableUtils.equals(fSmallSwordParticipant, that.fSmallSwordParticipant)
            && ObservableUtils.equals(fSabreParticipant, that.fSabreParticipant)
            && ObservableUtils.equals(fRapierParticipant, that.fRapierParticipant)
            && ObservableUtils.equals(fSmallSwordParticipantSProperty, that.fSmallSwordParticipantSProperty)
            && ObservableUtils.equals(fSabreParticipantSProperty, that.fSabreParticipantSProperty)
            && ObservableUtils.equals(fRapierParticipantSProperty, that.fRapierParticipantSProperty)
            && ObservableUtils.equals(weaponPointsProperty, that.weaponPointsProperty)
            && ObservableUtils.equals(oldSeasonWeaponPointsProperty, that.oldSeasonWeaponPointsProperty)
            && ObservableUtils.equals(participantResult, that.participantResult)
            && ObservableUtils.equals(fSabreInjury, that.fSabreInjury)
            && ObservableUtils.equals(fRapierInjury, that.fRapierInjury)
            && ObservableUtils.equals(fSmallSwordInjury, that.fSmallSwordInjury);
    }
}
