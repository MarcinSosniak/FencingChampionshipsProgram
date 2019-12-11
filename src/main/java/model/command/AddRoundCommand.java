package model.command;

import model.Main;
import model.Round;
import model.WeaponCompetition;

import java.util.List;
import java.util.logging.Level;

public class AddRoundCommand implements Command{

    private static final ValidInvocationChecker checker = ValidInvocationChecker.getChecker();

    private WeaponCompetition weaponCompetition;
    private Round roundToAdd;

    public AddRoundCommand(WeaponCompetition weaponCompetition, Round roundToAdd){
        this.weaponCompetition = weaponCompetition;
        this.roundToAdd = roundToAdd;
    }

    @Override
    public void execute() {
        weaponCompetition.addRound(checker, roundToAdd);
        Main.logger.log(Level.INFO, "Executing add round " + roundToAdd.getRoundNumber() + " command on weapon " +
                roundToAdd.getMyWeaponCompetition().getWeaponType());
    }

    @Override
    public void undo() {
        Main.logger.log(Level.INFO, "Executing undo add round " + roundToAdd.getRoundNumber() + " command on weapon " +
                roundToAdd.getMyWeaponCompetition().getWeaponType());
        weaponCompetition.removeRound(checker);
    }

    @Override
    public void redo() {
        execute();
        Main.logger.log(Level.INFO, "Executing redo add round " + roundToAdd.getRoundNumber() + " command on weapon " +
                roundToAdd.getMyWeaponCompetition().getWeaponType());
    }

}
