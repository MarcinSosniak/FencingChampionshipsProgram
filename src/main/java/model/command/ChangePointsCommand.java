package model.command;

import model.Main;
import model.Participant;
import model.Round;

import java.util.logging.Level;

public class ChangePointsCommand implements Command {

    private static final ValidInvocationChecker checker = ValidInvocationChecker.getChecker();

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

    private void changePoints(){
        if (ifAdd) {
            round.addRoundScorePoints(checker, participant, pointsNumber);
            participant.addPointsForWeapon(checker, round.getMyWeaponCompetition().getWeaponType(), pointsNumber);
        }
        else {
            round.subtractRoundScorePoints(checker, participant, pointsNumber);
            participant.subtractPointsFromWeapon(checker, round.getMyWeaponCompetition().getWeaponType(), pointsNumber);
        }
    }

    @Override
    public void execute() {
        changePoints();
        if (ifAdd)
            Main.logger.log(Level.INFO, "Execute add " + pointsNumber + " to participant " + participant.getName() + " " + participant.getSurname());
        else
            Main.logger.log(Level.INFO, "Execute subtract " + pointsNumber + " from participant " + participant.getName() + " " + participant.getSurname());


    }

    @Override
    public void undo() {
        if (ifAdd) {
            round.subtractRoundScorePoints(checker, participant, pointsNumber);
            participant.subtractPointsFromWeapon(checker, round.getMyWeaponCompetition().getWeaponType(), pointsNumber);
            Main.logger.log(Level.INFO, "Undo add " + pointsNumber + " to participant " + participant.getName() + " " + participant.getSurname());
        }
        else {
            round.addRoundScorePoints(checker, participant, pointsNumber);
            participant.addPointsForWeapon(checker, round.getMyWeaponCompetition().getWeaponType(), pointsNumber);
            Main.logger.log(Level.INFO, "Undo subtract " + pointsNumber + " to participant " + participant.getName() + " " + participant.getSurname());
        }
    }

    @Override
    public void redo() {
        changePoints();
        if (ifAdd)
            Main.logger.log(Level.INFO, "Redo add " + pointsNumber + " to participant " + participant.getName() + " " + participant.getSurname());
        else
            Main.logger.log(Level.INFO, "Redo subtract " + pointsNumber + " from participant " + participant.getName() + " " + participant.getSurname());
    }

}
