package model.FightDrawing;

import model.CompetitionGroup;
import model.KillerDrawing.KillerRandomizerStrategy;
import model.Participant;
import model.Round;
import util.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static model.FightDrawing.FightDrawingUtils.drawFightsForSortedParticipants;

public abstract class BaseMinimizeDrawStrategy extends FightDrawStrategy {

    protected static final int DRAWS = 10000 - 1;

    protected KillerRandomizerStrategy killS;

    @Override
    public List<CompetitionGroup> drawFightsForRound(Round round, int groupSize, List<Participant> participants) {
        int fullGroupsCount = (int) (participants.size() / groupSize);

        Collections.shuffle(participants);
        Pair<List<Participant>, Integer> bestConfiguration = new Pair<>(
                new ArrayList<>(participants),
                calculateScore(participants, round, groupSize)
        );
        long startTime = Instant.now().toEpochMilli();
        for (int i = 0; i < DRAWS; i++) {
            Collections.shuffle(participants);
            int score = calculateScore(participants, round, groupSize);
            if (score < bestConfiguration.snd())
                bestConfiguration = new Pair<>(new ArrayList<>(participants), score);
        }
        long endTime = Instant.now().toEpochMilli();
        System.out.println("drawing round time = " + (endTime-startTime + "ms"));
        return drawFightsForSortedParticipants(round, groupSize, bestConfiguration.fst(), killS);
    }

    abstract protected int calculateScore(List<Participant> groups, Round round, int groupSize);

}
