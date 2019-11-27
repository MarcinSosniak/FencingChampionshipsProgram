package model;

import javafx.beans.property.*;
import model.enums.WeaponType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/** There is a crash because during deserialization there is no value
 * for some properties and we try to recreate object property with null value which is not allowed */
public class ParticipantResult implements Serializable {
    public class WeaponCompetitionResults implements Serializable{
        private static final long serialVersionUID = 1008;

        private WeaponType type;
        private IntegerProperty place;
        private IntegerProperty points;

        public Integer getPlace() {
            return place.get();
        }

        public IntegerProperty placeProperty() {
            return place;
        }

        public Integer getPoints() {
            return points.get();
        }

        public IntegerProperty pointsProperty() {
            return points;
        }

        public WeaponCompetitionResults (WeaponType wt,Integer place,Integer points){
            this.type = wt;
            this.place = new SimpleIntegerProperty(place);
            this.points = new SimpleIntegerProperty(points);
        }

        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.writeObject(type);
            stream.writeObject(place.get());
            stream.writeObject(points.get());
        }

        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            type = (WeaponType) stream.readObject();
            place = new SimpleIntegerProperty((Integer) stream.readObject());
            points = new SimpleIntegerProperty((Integer) stream.readObject());
        }
    }

    private static final long serialVersionUID = 1007;

    private SimpleStringProperty triathlonOpen = new SimpleStringProperty("Uninitialized");
    private SimpleStringProperty triathlonWomen = new SimpleStringProperty("Uninitialized");

    private SimpleObjectProperty<WeaponCompetitionResults> smallSwordResults = new SimpleObjectProperty<>(new WeaponCompetitionResults(WeaponType.SMALL_SWORD,-1,-1000));

    public WeaponCompetitionResults getSmallSwordResults() {
        return smallSwordResults.get();
    }

    public SimpleObjectProperty<WeaponCompetitionResults> smallSwordResultsProperty() {
        return smallSwordResults;
    }

    public WeaponCompetitionResults getRapierResults() {
        return rapierResults.get();
    }

    public SimpleObjectProperty<WeaponCompetitionResults> rapierResultsProperty() {
        return rapierResults;
    }

    public WeaponCompetitionResults getSabreResults() {
        return sabreResults.get();
    }

    public SimpleObjectProperty<WeaponCompetitionResults> sabreResultsProperty() {
        return sabreResults;
    }

    private SimpleObjectProperty<WeaponCompetitionResults> rapierResults = new SimpleObjectProperty<>(new WeaponCompetitionResults(WeaponType.RAPIER,-1,-1000));
    private SimpleObjectProperty<WeaponCompetitionResults> sabreResults = new SimpleObjectProperty<>(new WeaponCompetitionResults(WeaponType.SABRE,-1,-1000));

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

    private void writeObject(ObjectOutputStream stream) throws IOException{
        stream.writeObject(triathlonOpen.get());
        stream.writeObject(triathlonWomen.get());
        stream.writeObject(smallSwordResults.get());
        stream.writeObject(rapierResults.get());
        stream.writeObject(sabreResults.get());

    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        triathlonOpen = new SimpleStringProperty((String) stream.readObject());
        triathlonWomen = new SimpleStringProperty((String) stream.readObject());
        smallSwordResults = new SimpleObjectProperty<WeaponCompetitionResults>((WeaponCompetitionResults) stream.readObject());
        rapierResults = new SimpleObjectProperty<WeaponCompetitionResults>((WeaponCompetitionResults) stream.readObject());
        sabreResults = new SimpleObjectProperty<WeaponCompetitionResults>((WeaponCompetitionResults) stream.readObject());
    }

}
