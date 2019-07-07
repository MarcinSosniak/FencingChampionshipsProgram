package model;
import java.util.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import model.enums.JudgeState;

public class CompetitorBase {
    private StringProperty name;
    private StringProperty surname;
    private StringProperty location;
    private StringProperty location_group;
    private ObjectProperty<JudgeState> judgeState;
    private ObjectProperty<Date> license_exp_date;
    private BooleanProperty spade_competitor;
    private BooleanProperty sabre_competitor;
    private BooleanProperty rapier_competitor;


    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getLocation_group() {
        return location_group.get();
    }

    public StringProperty location_groupProperty() {
        return location_group;
    }

    public void setLocation_group(String location_group) {
        this.location_group.set(location_group);
    }

    public JudgeState getJudgeState() {
        return judgeState.get();
    }

    public ObjectProperty<JudgeState> judgeStateProperty() {
        return judgeState;
    }

    public void setJudgeState(JudgeState judgeState) {
        this.judgeState.set(judgeState);
    }

    public Date getLicense_exp_date() {
        return license_exp_date.get();
    }

    public ObjectProperty<Date> license_exp_dateProperty() {
        return license_exp_date;
    }

    public void setLicense_exp_date(Date license_exp_date) {
        this.license_exp_date.set(license_exp_date);
    }

    public boolean isSpade_competitor() {
        return spade_competitor.get();
    }

    public BooleanProperty spade_competitorProperty() {
        return spade_competitor;
    }

    public void setSpade_competitor(boolean spade_competitor) {
        this.spade_competitor.set(spade_competitor);
    }

    public boolean isSabre_competitor() {
        return sabre_competitor.get();
    }

    public BooleanProperty sabre_competitorProperty() {
        return sabre_competitor;
    }

    public void setSabre_competitor(boolean sabre_competitor) {
        this.sabre_competitor.set(sabre_competitor);
    }

    public boolean isRapier_competitor() {
        return rapier_competitor.get();
    }

    public BooleanProperty rapier_competitorProperty() {
        return rapier_competitor;
    }

    public void setRapier_competitor(boolean rapier_competitor) {
        this.rapier_competitor.set(rapier_competitor);
    }
}
