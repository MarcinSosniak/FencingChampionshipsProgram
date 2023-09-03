package model.resultsOperations;

import model.CompetitionGroup;
import model.Participant;
import model.Round;
import model.WeaponCompetition;
import util.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static model.Competition.getOpenTriathlonParticipantComparator;

public class TieBreakerSolver {

    private List<Participant> participants;
    private GenderBasedSolverOperations genderBasedSolverOperations;
    private List<WeaponCompetition> weaponCompetitions;

    public static TieBreakerSolver createOpenSolver(List<Participant> participants, List<WeaponCompetition> weaponCompetitions) {
        return new TieBreakerSolver(participants, weaponCompetitions, new OpenGenderOperations());
    }

    public static TieBreakerSolver createWomenSolver(List<Participant> participants, List<WeaponCompetition> weaponCompetitions) {
        return new TieBreakerSolver(participants, weaponCompetitions, new WomenGenderOperations());
    }


    public void apply() {
        Map<String, List<Participant>> positionToParticipants = prepareTiebreakerGroups();
        applyTieBreaker(positionToParticipants);
    }

    private interface GenderBasedSolverOperations {
        void insertTriathlonPosition(Participant p, int result);
        boolean shouldSkipParticipant(Participant participant);
        String getGenderTriathlonResults(Participant participant);
    }

    private static class OpenGenderOperations implements GenderBasedSolverOperations {

        @Override
        public void insertTriathlonPosition(Participant participant, int position) {
            participant.getParticipantResult().setTriathlonOpen(Integer.toString(position));
        }

        @Override
        public boolean shouldSkipParticipant(Participant participant) {
            return false;
        }

        @Override
        public String getGenderTriathlonResults(Participant participant) {
            return participant.getParticipantResult().getTriathlonOpen();
        }
    }

    private static class WomenGenderOperations implements GenderBasedSolverOperations {

        @Override
        public void insertTriathlonPosition(Participant participant, int position) {
            participant.getParticipantResult().setTriathlonWomen(Integer.toString(position));
        }

        @Override
        public boolean shouldSkipParticipant(Participant participant) {
            return !participant.isfFemale();
        }

        @Override
        public String getGenderTriathlonResults(Participant participant) {
            return participant.getParticipantResult().getTriathlonWomen();
        }
    }


    private TieBreakerSolver(List<Participant> participants, List<WeaponCompetition> weaponCompetitions, GenderBasedSolverOperations genderSolver) {
        this.participants = participants;
        this.genderBasedSolverOperations = genderSolver;
        this.weaponCompetitions = weaponCompetitions;
    }


    private Map<String, List<Participant>> prepareTiebreakerGroups() {
        participants.sort(getOpenTriathlonParticipantComparator());
        Collections.reverse(participants);

        Map<String, List<Participant>> positionToParticipants = new HashMap<>();
        for (Participant p : participants) {
            if (genderBasedSolverOperations.shouldSkipParticipant(p))
                continue;
            if (!positionToParticipants.containsKey(genderBasedSolverOperations.getGenderTriathlonResults(p))) {
                positionToParticipants.put(genderBasedSolverOperations.getGenderTriathlonResults(p), new ArrayList<Participant>() {{
                    add(p);
                }});
            } else {
                positionToParticipants.get(genderBasedSolverOperations.getGenderTriathlonResults(p)).add(p);
            }
        }
        return positionToParticipants;
    }


    private void applyTieBreaker(Map<String, List<Participant>> positionToParticipants) {
        for (Map.Entry<String, List<Participant>> tyingGroup : positionToParticipants.entrySet()) {
            if (tyingGroup.getKey().equals("--"))
                continue;
            List<Participant> tryingGroupParticipants = tyingGroup.getValue();
            if (tryingGroupParticipants.size() < 2) {
                continue;
            }

            List<Pair<Participant, Float>> participantsWithOpponentsStrength = tryingGroupParticipants
                    .stream().map(p -> new Pair<Participant, Float>(p, enemyAverageStrength(p)))
                    .collect(Collectors.toList());

            Collections.shuffle(participantsWithOpponentsStrength);

            participantsWithOpponentsStrength.sort(
                    (Pair<Participant, Float> p1WithScore, Pair<Participant, Float> p2WithScore) -> {
                        float score1 = p1WithScore.snd();
                        float score2 = p2WithScore.snd();
                        if (score1 < score2) {
                            return 1;
                        } else { // since list is shuffled, we can consider ordering as random, in case of equals,
                            // which is highly unlikely, an effectively random participant will be chosen.
                            return -1;
                        }
                    }
            );

            int position = Integer.parseInt(tyingGroup.getKey());
            for (Pair<Participant, Float> participantWithOpponentsStrength : participantsWithOpponentsStrength) {
                genderBasedSolverOperations.insertTriathlonPosition(participantWithOpponentsStrength.fst(), position);
                position++;
            }
        }
    }


    private float enemyAverageStrength(Participant participant) {
        int count = 0;
        float sum = 0;
        for (WeaponCompetition wc : weaponCompetitions)
            for (Round r : wc.getRoundsCopy())
                for (CompetitionGroup g : r.getGroups()) {
                    if (g.getGroupParticipants().contains(participant)) {
                        for (Participant potentialOpponent : g.getGroupParticipants()) {
                            if (!potentialOpponent.equals(participant)) {
                                sum += Float.valueOf(potentialOpponent.getParticipantResult().getTriathlonOpenPoints());
                                count += 1;
                            }
                        }
                    }
                }
        return sum / count;
    }
}
