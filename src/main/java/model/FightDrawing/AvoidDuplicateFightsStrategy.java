package model.FightDrawing;


import model.*;
import model.KillerDrawing.KillerRandomizerStrategy;
import model.config.ConfigReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.FightDrawing.FightDrawingUtils.toAllGroups;

public class AvoidDuplicateFightsStrategy extends BaseMinimizeDrawStrategy {

    private final static int DRAWS = 10000 - 1;

    transient private ParticipantPastOpponentsMapper mapper = null;

    public AvoidDuplicateFightsStrategy(KillerRandomizerStrategy kills) {
        killS = kills;
    }

    @Override
    protected int calculateScore(List<Participant> participants, Round round, int groupSize) {
        initializeMapper(participants, round);
        List<List<Participant>> groups = toAllGroups(participants, groupSize);
        List<Integer> scores = new ArrayList<>();
        int score = 0;
        for (List<Participant> group : groups) {
            int groupScore = getScoreFromGroup(group);
            scores.add(groupScore);
            score += groupScore;
        }
        return score;
    }

    private int getScoreFromGroup(List<Participant> group) {
        validateMapper();
        int groupScore = 0;
        for (int i = 0; i < group.size(); i++) {
            for (int k = i + 1; k < group.size(); k++) {
                groupScore += mapper.howManyFights(group.get(i), group.get(k));
            }
        }
        return groupScore;
    }

    private void validateMapper() {
        if (mapper == null) {
            throw new IllegalStateException("Tried using uninitialized mapper");
        }
    }

    private void initializeMapper(List<Participant> participants, Round round) {
        if (mapper == null) {
            mapper = new ParticipantPastOpponentsMapper(participants, round);
        }
    }


    protected class ParticipantPastOpponentsMapper {
        Map<Participant, Map<Participant, Integer>> participantsFights = new HashMap<>();

        protected ParticipantPastOpponentsMapper(List<Participant> participants, Round round) {
            List<Fight> fights = new ArrayList<>();
            addAllFights(fights, round);

            participants.forEach(participant -> participantsFights.put(participant, new HashMap<>()));
            fights.forEach(this::addFight);
        }

        private void addAllFights(List<Fight> fights, Round round) {
            if (ConfigReader.getInstance().getBooleanValue("DRAWING", "AVOID_DUPLICATE_FROM_ALL_WEAPONS", false)) {
                Competition.getInstance().getWeaponCompetitions().forEach(wc -> addAllFightsForWC(fights, wc));
            } else {
                addAllFightsForWC(fights, round.getMyWeaponCompetition());
            }
        }

        private void addAllFightsForWC(List<Fight> fights, WeaponCompetition wc) {
            wc.getRoundsCopy().forEach(
                    r -> r.getGroups().forEach(g -> fights.addAll(g.getFightsList()))
            );
        }

        public int howManyFights(Participant p1, Participant p2) {
            return participantsFights.get(p1).getOrDefault(p2, 0);
        }

        private void addFight(Fight fight) {
            addFightOrdered(fight.getFirstParticipant(), fight.getSecondParticipant());
            addFightOrdered(fight.getSecondParticipant(), fight.getFirstParticipant());
        }

        private void addFightOrdered(Participant p1, Participant p2) {
            if (!participantsFights.containsKey(p1)) {
                participantsFights.put(p1, new HashMap<>());
            }
            Map<Participant, Integer> p1Opponents = participantsFights.get(p1);
            p1Opponents.put(p2, p1Opponents.getOrDefault(p2, 0) + 1);
        }

    }

    @Override
    protected int getDrawsCount() {
        return DRAWS;
    }
}
