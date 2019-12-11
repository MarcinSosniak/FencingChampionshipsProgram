package model;

import javafx.beans.property.*;
import model.enums.WeaponType;
import util.RationalNumber;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * There is a crash because during deserialization there is no value
 * for some properties and we try to recreate object property with null value which is not allowed
 */
public class ParticipantResult implements Serializable {
    public class WeaponCompetitionResult implements Serializable {
        private static final long serialVersionUID = 1008;

        private WeaponType type;
        private IntegerProperty place;
        private SimpleObjectProperty<RationalNumber> points;

        public void setPlace(int place) {
            this.place.set(place);
        }

        public void setPoints(RationalNumber points) {
            this.points.set(points);
        }

        public Integer getPlace() {
            return place.get();
        }

        public IntegerProperty placeProperty() {
            return place;
        }

        public RationalNumber getPoints() {
            return points.get();
        }

        public Integer getPointsBasedOnPlace(){
            return (101 -this.place.get());
        }

        public SimpleObjectProperty<RationalNumber> pointsProperty() {
            return points;
        }

        public WeaponCompetitionResult(WeaponType wt, Integer place, RationalNumber points) {
            this.type = wt;
            this.place = new SimpleIntegerProperty(place);
            this.points = new SimpleObjectProperty<RationalNumber>(points);
        }

        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.writeObject(type);
            stream.writeObject(place.get());
            stream.writeObject(points.get());
        }

        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            type = (WeaponType) stream.readObject();
            place = new SimpleIntegerProperty((Integer) stream.readObject());
            points = new SimpleObjectProperty<RationalNumber>((RationalNumber) stream.readObject());
        }

        public void toExport(){
            /** TODO: implement me! */
        }
    }

    private static final long serialVersionUID = 1007;
    private SimpleObjectProperty<Participant> participant;
    private SimpleIntegerProperty triathlonOpenPoints = new SimpleIntegerProperty(0);

    private SimpleIntegerProperty triathlonWomenPoints = new SimpleIntegerProperty(0);

    private SimpleStringProperty triathlonOpen = new SimpleStringProperty("Uninitialized");
    private SimpleStringProperty triathlonWomen = new SimpleStringProperty("Uninitialized");

    private SimpleObjectProperty<WeaponCompetitionResult> smallSwordResults = new SimpleObjectProperty<>(new WeaponCompetitionResult(WeaponType.SMALL_SWORD, -1, new RationalNumber(-100000, 1)));
    private SimpleObjectProperty<WeaponCompetitionResult> rapierResults = new SimpleObjectProperty<>(new WeaponCompetitionResult(WeaponType.RAPIER, -1, new RationalNumber(-100000, 1)));
    private SimpleObjectProperty<WeaponCompetitionResult> sabreResults = new SimpleObjectProperty<>(new WeaponCompetitionResult(WeaponType.SABRE, -1, new RationalNumber(-100000, 1)));

    public ParticipantResult(Participant p){
        this.participant = new SimpleObjectProperty<Participant>(p);
    }

    /** Call ONLY if points for each weapon are calculated*/
    public void calculateTriathlonPoints() {
        int current = this.triathlonOpenPoints.get();
        int fromSabre = this.sabreResults.get().getPointsBasedOnPlace();
        int fromRapier = this.rapierResults.get().getPointsBasedOnPlace();
        int fromSmallSword = this.smallSwordResults.get().getPointsBasedOnPlace();
        int toSet = 0;
        if(fromSabre < 101){
            toSet += fromSabre;
        }
        if(fromRapier < 101){
            toSet += fromRapier;
            System.out.println("uuuuuuuuuu: " + fromRapier);
        }
        if(fromSmallSword < 101){
            toSet += fromSmallSword;
        }
        System.out.println(toSet);
        this.triathlonOpenPoints.set(toSet);

        if(participant.get().isfFemale())
            triathlonWomenPoints.set(toSet);
        else
            triathlonWomenPoints.set(-1);
    }

    public Integer getTriathlonOpenPoints() {
        return triathlonOpenPoints.getValue();
    }

    public SimpleIntegerProperty triathlonOpenPointsProperty() {
        return triathlonOpenPoints;
    }

    public int getTriathlonWomenPoints() {
        return triathlonWomenPoints.get();
    }

    public SimpleIntegerProperty triathlonWomenPointsProperty() {
        return triathlonWomenPoints;
    }


    public WeaponCompetitionResult getSmallSwordResults() {
        return smallSwordResults.get();
    }

    public SimpleObjectProperty<WeaponCompetitionResult> smallSwordResultsProperty() {
        return smallSwordResults;
    }

    public WeaponCompetitionResult getRapierResults() {
        return rapierResults.get();
    }

    public SimpleObjectProperty<WeaponCompetitionResult> rapierResultsProperty() {
        return rapierResults;
    }

    public WeaponCompetitionResult getSabreResults() {
        return sabreResults.get();
    }

    public SimpleObjectProperty<WeaponCompetitionResult> sabreResultsProperty() {
        return sabreResults;
    }

    public WeaponCompetitionResult getWeaponCompetitionResult(WeaponType wt) {
        switch (wt) {
            case SMALL_SWORD:
                return this.getSmallSwordResults();
            case RAPIER:
                return this.getRapierResults();
            case SABRE:
                return this.getSabreResults();
        }
        System.out.format("Something went wrong while trying to get WeaponCompetitionResult");
        return null;
    }

    public void setTriathlonOpen(String triathlonOpen) {
        this.triathlonOpen.set(triathlonOpen);
    }

    public void setTriathlonWomen(String triathlonWomen) {
        this.triathlonWomen.set(triathlonWomen);
    }


    public String getTriathlonOpen() {
        return triathlonOpen.get();
    }

    public SimpleStringProperty triathlonOpenProperty() {
        return triathlonOpen;
    }

    public String getTriathlonWomen() {
        return triathlonWomen.get();
    }

    public SimpleStringProperty triathlonWomenProperty() {
        return triathlonWomen;
    }


    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(participant.get());
        stream.writeObject(triathlonOpen.get());
        stream.writeObject(triathlonWomen.get());
        stream.writeObject(smallSwordResults.get());
        stream.writeObject(rapierResults.get());
        stream.writeObject(sabreResults.get());
        stream.writeObject(triathlonOpenPoints.get());
        stream.writeObject(triathlonWomenPoints.get());
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        participant = new SimpleObjectProperty<>((Participant) stream.readObject());
        triathlonOpen = new SimpleStringProperty((String) stream.readObject());
        triathlonWomen = new SimpleStringProperty((String) stream.readObject());
        smallSwordResults = new SimpleObjectProperty<WeaponCompetitionResult>((WeaponCompetitionResult) stream.readObject());
        rapierResults = new SimpleObjectProperty<WeaponCompetitionResult>((WeaponCompetitionResult) stream.readObject());
        sabreResults = new SimpleObjectProperty<WeaponCompetitionResult>((WeaponCompetitionResult) stream.readObject());
        triathlonOpenPoints = new SimpleIntegerProperty((Integer) stream.readObject());
        triathlonWomenPoints = new SimpleIntegerProperty((Integer) stream.readObject());
    }

}
