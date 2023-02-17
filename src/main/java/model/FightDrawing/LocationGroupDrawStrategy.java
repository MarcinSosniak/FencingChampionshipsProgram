package model.FightDrawing;

import model.KillerDrawing.KillerRandomizerStrategy;
import model.Participant;
import model.Round;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static model.FightDrawing.FightDrawingUtils.toAllGroups;

public class LocationGroupDrawStrategy extends BaseMinimizeDrawStrategy {

    public LocationGroupDrawStrategy(KillerRandomizerStrategy kills) {
        killS = kills;
    }

    @Override
    protected int calculateScore(List<Participant> participants, Round round, int groupSize) {
        List<List<Participant>> groups = toAllGroups(participants, groupSize);
        List<Integer> scores = new ArrayList<>();
        int score = 0;
        for (List<Participant> group : groups) {
            int groupScore = scoreFromGroup(group);
            score += groupScore;
            scores.add(groupScore);
        }
//        debugLog(groups, scores, score);
        return score;
    }

    private int scoreFromGroup(List<Participant> group) {
        int groupScore = 0;
        for (int i = 0; i < group.size(); i++) {
            for (int k = i + 1; k < group.size(); k++) {
                if (group.get(i).getLocationGroup().equals(group.get(k).getLocationGroup()))
                    groupScore++;
            }
        }
        return groupScore;
    }

    private void debugLog(List<List<Participant>> groups, List<Integer> scores, int totalScore) {
        List<String> groupLog = new ArrayList<>();
        for (int i=0; i< groups.size() ; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(groups.get(i).stream().map(Participant::getLocationGroup).collect(Collectors.joining(",")));
            sb.append("]=");
            sb.append(scores.get(i));
            groupLog.add(sb.toString());
        }
        System.out.println(String.join(",",groupLog) + "totalScore = " + String.valueOf(totalScore));
    }
}
