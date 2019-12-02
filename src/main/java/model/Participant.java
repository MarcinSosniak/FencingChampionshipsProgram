package model;

import com.google.gson.annotations.Expose;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.command.ChangePointsCommand;
import model.command.CommandAddInjury;
import model.enums.JudgeState;
import model.enums.WeaponType;
import model.exceptions.NoSuchWeaponException;
import util.RationalNumber;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import model.command.ValidInvocationChecker;
import java.util.*;

public class Participant implements Serializable{

    private static final long serialVersionUID = 3;
    private StringProperty name;
    private StringProperty surname;
    private StringProperty location;
    private StringProperty locationGroup;
    private ObjectProperty<JudgeState> judgeState;
    private ObjectProperty<Date> licenseExpDate;
    @Expose(serialize = false)
    private transient int timesKiller = 0;

    /** for table view required */
    @Expose(serialize = false)
    private transient BooleanProperty fSmallSwordParticipant;
    @Expose(serialize = false)
    private transient BooleanProperty fSabreParticipant;
    @Expose(serialize = false)
    private transient BooleanProperty fRapierParticipant;
    @Expose(serialize = false)
    private transient Map<WeaponType, ObjectProperty<util.RationalNumber>> weaponPointsProperty;
    @Expose(serialize = false)
    private transient ObjectProperty<RationalNumber> rapierPoints;

     /** For final results required */
    private BooleanProperty fFemale = new SimpleBooleanProperty(false);
    @Expose(serialize = false)
    private transient SimpleObjectProperty<ParticipantResult> participantResult = new SimpleObjectProperty<>(new ParticipantResult(this));

    @Expose(serialize = false)
    private transient BooleanProperty fSabreInjury = new SimpleBooleanProperty(false);
    @Expose(serialize = false)
    private transient BooleanProperty fRapierInjury = new SimpleBooleanProperty(false);
    @Expose(serialize = false)
    private transient BooleanProperty fSmallSwordInjury = new SimpleBooleanProperty(false);

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
        this.weaponPointsProperty = new HashMap<>();
        this.rapierPoints = new SimpleObjectProperty<RationalNumber>(new RationalNumber(0));
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
        if (!this.fSmallSwordParticipant.getValue().equals(fSmallSwordParticipant)) {
            if (this.weaponPointsProperty.containsKey(WeaponType.SMALL_SWORD) && !fSmallSwordParticipant)
                this.weaponPointsProperty.remove(WeaponType.SMALL_SWORD);
            else this.weaponPointsProperty.put(WeaponType.SMALL_SWORD, new SimpleObjectProperty<>(new RationalNumber(0)));

            this.fSmallSwordParticipant.setValue(fSmallSwordParticipant);
        }
    }

    public void setfSabreParticipant(boolean fSabreParticipant) {
        if (!this.fSabreParticipant.getValue().equals(fSabreParticipant)) {
            if (this.weaponPointsProperty.containsKey(WeaponType.SABRE) && !fSabreParticipant)
                this.weaponPointsProperty.remove(WeaponType.SABRE);
            else this.weaponPointsProperty.put(WeaponType.SABRE, new SimpleObjectProperty<>(new RationalNumber(0)));
            this.fSabreParticipant.setValue(fSabreParticipant);
        }
    }

    public void setfRapierParticipant(boolean fRapierParticipant) {
        if (!this.fRapierParticipant.getValue().equals(fRapierParticipant)){
            if (this.weaponPointsProperty.containsKey(WeaponType.RAPIER) && !fRapierParticipant)
                this.weaponPointsProperty.remove(WeaponType.RAPIER);
            else this.weaponPointsProperty.put(WeaponType.RAPIER, new SimpleObjectProperty<>(new RationalNumber(0)));

            this.fRapierParticipant.setValue(fRapierParticipant);
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

    /** For final results required */
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



    public void addPointsForWeapon(ValidInvocationChecker checker, WeaponType type, RationalNumber points){
        Objects.requireNonNull(checker);
        if (weaponPointsProperty.containsKey(type)) {
            System.out.println("adding points in partcipant");
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

    public void setTimesKiller(int timesKiller) { this.timesKiller = timesKiller; }

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
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Participant)
            return (this.name.toString().equals(((Participant) obj).name.toString())
                    && this.surname.toString().equals(((Participant) obj).surname.toString())
                    && this.location.toString().equals(((Participant) obj).location.toString())
                    && this.locationGroup.toString().equals(((Participant) obj).locationGroup.toString())
                    && this.judgeState.toString().equals(((Participant) obj).judgeState.toString())
                    && this.licenseExpDate.toString().equals(((Participant) obj).licenseExpDate.toString()));
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() * 4 + this.surname.hashCode() * 17 +
            this.locationGroup.hashCode() + this.location.hashCode() * 2 +
            this.judgeState.hashCode() + this.licenseExpDate.hashCode() * 3;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(rapierPoints.get());
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
        Map<WeaponType, util.RationalNumber> weaponPoints = new HashMap<>();
        weaponPointsProperty.forEach((wt, wp) -> weaponPoints.put(wt, wp.get()));
        stream.writeObject(weaponPoints);
        /** For final results required */
        stream.writeObject(fFemale.get());
        stream.writeObject(participantResult.get());
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        rapierPoints = new SimpleObjectProperty<>((RationalNumber) stream.readObject());
        name = new SimpleStringProperty((String) stream.readObject());
        surname = new SimpleStringProperty((String) stream.readObject());
        location = new SimpleStringProperty((String) stream.readObject());
        locationGroup = new SimpleStringProperty((String) stream.readObject());
        judgeState = new SimpleObjectProperty<>((JudgeState) stream.readObject());
        licenseExpDate = new SimpleObjectProperty<>((Date) stream.readObject());
        fSmallSwordParticipant = new SimpleBooleanProperty(stream.readBoolean());
        fRapierParticipant = new SimpleBooleanProperty(stream.readBoolean());
        fSabreParticipant = new SimpleBooleanProperty(stream.readBoolean());
        fRapierInjury = new SimpleBooleanProperty(stream.readBoolean());
        fSabreInjury = new SimpleBooleanProperty(stream.readBoolean());
        fSmallSwordInjury = new SimpleBooleanProperty(stream.readBoolean());
        timesKiller = stream.readInt();
        weaponPointsProperty = FXCollections.observableHashMap();
        Map<WeaponType, RationalNumber> m = (Map<WeaponType, RationalNumber>) stream.readObject();
        m.forEach((wt, wp) -> weaponPointsProperty.put(wt, new SimpleObjectProperty<>(wp)));
        /** For final results required */
        fFemale = new SimpleBooleanProperty((Boolean) stream.readObject());
        participantResult = new SimpleObjectProperty<>((ParticipantResult) stream.readObject());
    }
}
