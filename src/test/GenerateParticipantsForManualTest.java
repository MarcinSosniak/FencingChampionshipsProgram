import model.Competition;
import model.Participant;
import model.PersistenceManager;
import model.config.ConfigReader;
import model.enums.JudgeState;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenerateParticipantsForManualTest {


    @Test
    public void createParticipantsListForManualTests() throws Exception {
        int PARTICIPANTS_COUNT = 16;
        int GROUP_SIZE = 5;
        int JUDGES = 3;

        List<Participant> participantList = new ArrayList<>();

        for (int i=0; i< PARTICIPANTS_COUNT; i++) {

            boolean isJudge = i < JUDGES;

            Participant participant = new Participant(
                    "name+" + i,
                    "surname" + i,
                    "location",
                    "locationGroup",
                    isJudge? JudgeState.MAIN_JUDGE : JudgeState.NON_JUDGE,
                    Date.from(LocalDate.now().plusYears(10).atStartOfDay().toInstant(ZoneOffset.UTC)),
                    PARTICIPANTS_COUNT - i,
                    PARTICIPANTS_COUNT - i,
                    PARTICIPANTS_COUNT - i
            );
            participantList.add(participant);
        }
        String json = PersistenceManager.serializeObjectsArrayToJson(participantList);
        String fileName = "testParticipants.json";
        try (PrintStream out = new PrintStream(new FileOutputStream(fileName))) {
            out.print(json);
        }

    }
}
