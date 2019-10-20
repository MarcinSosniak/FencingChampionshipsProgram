package model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PersistenceManagerTest {

    String json;
    Participant p1;
    Participant p2;

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

        p1 = new Participant("Marcin", "Kowalski",
                "KRK", "A", "NON_JUDGE", "23-07-2020");
        p2 = new Participant("Paulina", "Nowak",
                "KRK", "B", "MAIN_JUDGE", "23-07-2020");
    }

    @Test
    public void serializeObjectsArrayToJson() {
        ArrayList<Participant> arrayList = new ArrayList<>();
        arrayList.add(p1);
        arrayList.add(p2);
        String myJson = PersistenceManager.serializeObjectsArrayToJson(arrayList);
        System.out.println(myJson);

        Participant[] participants = PersistenceManager.convertJsonToParticipantArray(myJson);
        assertEquals(p1, participants[0]);
        assertEquals(p2, participants[1]);

        List<Participant> participantList =
                PersistenceManager.deserializeFromJsonArray(myJson, Participant.class);
        assertEquals(p1, participantList.get(0));
        assertEquals(p2, participantList.get(1));
    }

    @Test
    public void convertJsonToParticipantArray() {
        String json =
        "[{\"name\":\"Marcin\",\"surname\":\"Kowalski\"," +
                "\"location\":\"KRK\",\"locationGroup\":\"A\",\"judgeState\":\"NON_JUDGE\"," +
                "\"licenseExpDate\":\"Jul 23, 2020 12:00:00 AM\",\"timesKiller\":0,\"fSmallSwordParticipant\":false," +
                "\"fSabreParticipant\":false,\"fRapierParticipant\":false,\"weaponPointsProperty\":{},\"fSabreInjury\":false," +
                "\"fRapierInjury\":false,\"fSmallSwordInjury\":false},{\"name\":\"Paulina\",\"surname\":\"Nowak\",\"location\":\"KRK\",\"locationGroup\":\"B\",\"judgeState\":\"MAIN_JUDGE\",\"licenseExpDate\":\"Jul 23, 2020 12:00:00 AM\",\"timesKiller\":0,\"fSmallSwordParticipant\":false,\"fSabreParticipant\":false,\"fRapierParticipant\":false,\"weaponPointsProperty\":{},\"fSabreInjury\":false,\"fRapierInjury\":false,\"fSmallSwordInjury\":false}]";

        Participant[] participants = PersistenceManager.convertJsonToParticipantArray(json);
        System.out.println(participants[0].getLicenseExpDate().toString());
        //assertEquals(p1, participants[0]);
        //assertEquals(p2, participants[1]);
    }

    @Test
    public void deserializeFromJsonArray() {
    }
}