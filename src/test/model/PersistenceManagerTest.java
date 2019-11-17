package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FightDrawing.FightDrawStrategyPicker;
import model.KillerDrawing.RandomKillerRandomizationStrategy;
import model.config.ConfigReader;
import model.enums.JudgeState;
import model.enums.WeaponType;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PersistenceManagerTest {

    String json;
    Participant p1; Participant p2; Participant p3; Participant p4;
    ConfigReader configReader;
    Competition competition;

    @Before
    public void setUp() throws Exception {
        json = "[ \n" +
                "   { \n" +
                "      \"name\":\"Marcin\",\n" +
                "      \"surname\":\"Kowalski\",\n" +
                "      \"location\":\"KRK\",\n" +
                "      \"locationGroup\":\"A\",\n" +
                "      \"judgeState\":\"NON_JUDGE\",\n" +
                "      \"licenseExpDate\":\"23-07-2020\"\n" +
                "   },\n" +
                "   { \n" +
                "      \"name\":\"Paulina\",\n" +
                "      \"surname\":\"Nowak\",\n" +
                "      \"location\":\"KRK\",\n" +
                "      \"locationGroup\":\"B\",\n" +
                "      \"judgeState\":\"MAIN_JUDGE\",\n" +
                "      \"licenseExpDate\":\"23-07-2020\"\n" +
                "   }\n" +
                "]\n";

        p1 = new Participant("Marcin", "Kowalski", "KRK", "A", "NON_JUDGE", "23-07-2020");
        p2 = new Participant("Paulina", "Nowak", "KRK", "B", "MAIN_JUDGE", "23-07-2020");
        p3 = new Participant("Name3","Surname3","Location1","lacationGroup1", JudgeState.NON_JUDGE,new Date());
        p4 = new Participant("Name4","Surname4","Location2","lacationGroup2", JudgeState.NON_JUDGE,new Date());

        ObservableList<Participant> participants = FXCollections.observableArrayList();
        participants.add(p1); participants.add(p2); participants.add(p3); participants.add(p4);

        ObservableList rapierParticipants = FXCollections.observableArrayList();
        rapierParticipants.add(p1); rapierParticipants.add(p2); rapierParticipants.add(p4);

        ObservableList sabreParticipants = FXCollections.observableArrayList();
        sabreParticipants.add(p2); sabreParticipants.add(p4);

        ObservableList smallSwordParticipants = FXCollections.observableArrayList();
        smallSwordParticipants.add(p1); smallSwordParticipants.add(p3); smallSwordParticipants.add(p2);

        competition = new Competition(
                new util.Pair<ObservableList<Participant>, WeaponType>(rapierParticipants, WeaponType.RAPIER),
                new util.Pair<ObservableList<Participant>,WeaponType>(sabreParticipants,WeaponType.SABRE),
                new util.Pair<ObservableList<Participant>,WeaponType>(smallSwordParticipants,WeaponType.SMALL_SWORD),
                new RandomKillerRandomizationStrategy());
    }

    @Test
    public void serializeAndDeserializeParametrizedObjectTest(){
        String json = PersistenceManager.serializeParametrizedObject(p1);
        Participant p = PersistenceManager.deserializeParametrizedObject(json, Participant.class);
        assertEquals(p1, p);
    }

    @Test
    public void serializeObjectsArrayToJson() {
        ArrayList<Participant> arrayList = new ArrayList<>();
        arrayList.add(p1);
        arrayList.add(p2);
        String myJson = PersistenceManager.serializeObjectsArrayToJson(arrayList);

        List<Participant> participantList =
                PersistenceManager.deserializeFromJsonArray(myJson, Participant.class, false);
        assertEquals(p1, participantList.get(0));
        assertEquals(p2, participantList.get(1));
    }

    @Test
    public void deserializeFromJsonArray() {
        List<Participant> participantList =
                PersistenceManager.deserializeFromJsonArray(json, Participant.class, true);
        assertEquals(p1, participantList.get(0));
        assertEquals(p2, participantList.get(1));
    }

    @Test
    public void test(){
        String filename = "file.bin";

        // Serialization
        try {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(competition);
            out.close();
            file.close();
        }
        catch(IOException ex) { System.out.println("IOException while serialization " + ex.getMessage());}

        // Deserialization
        Competition c = null;
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            c= (Competition) in.readObject();
            in.close();
            file.close();
        }
        catch(IOException ex) { System.out.println("IOException while deserialization " + ex.toString()); }
        catch(ClassNotFoundException ex) { System.out.println("ClassNotFoundException is caught"); }
        assertEquals(competition, c);
        new File(filename).delete();
    }

}