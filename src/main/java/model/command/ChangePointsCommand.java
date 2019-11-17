package model.command;

import model.Participant;
import model.Round;

public class ChangePointsCommand implements Command {

    public static final class ValidInvocationChecker { private ValidInvocationChecker() {} }
    private static final ValidInvocationChecker checker = new ValidInvocationChecker();

    private Participant participant;
    private Round round;
    private util.RationalNumber pointsNumber;
    private boolean ifAdd;

    public ChangePointsCommand(Round round, Participant participant, util.RationalNumber pointsNumber, boolean ifAdd) {
        this.round = round;
        this.participant = participant;
        this.pointsNumber = pointsNumber;
        this.ifAdd = ifAdd;
    }

    @Override
    public void execute() {
        if (ifAdd) {
            System.out.println("here");
            round.addRoundScorePoints(checker, participant, pointsNumber);
        }
        else round.subtractRoundScorePoints(checker, participant, pointsNumber);
    }

    @Override
    public void undo() {
        if (ifAdd) round.subtractRoundScorePoints(checker, participant, pointsNumber);
        else round.addRoundScorePoints(checker, participant, pointsNumber);
    }

    @Override
    public void redo() {
        execute();
    }


}
