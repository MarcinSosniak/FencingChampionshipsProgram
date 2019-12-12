package model.command;

import model.Main;
import model.Round;
import model.WeaponCompetition;
import model.enums.WeaponType;

import java.util.List;
import java.util.logging.Level;

public class AddRoundCommand implements Command{

    private static final ValidInvocationChecker checker = ValidInvocationChecker.getChecker();

    private WeaponCompetition weaponCompetition;
    private Round roundToAdd;
    private WeaponType wt;

    public AddRoundCommand(WeaponCompetition weaponCompetition, Round roundToAdd){
        this.weaponCompetition = weaponCompetition;
        this.roundToAdd = roundToAdd;
        this.wt = roundToAdd.getMyWeaponCompetition().getWeaponType();
    }

    @Override
    public void execute() {
        weaponCompetition.addRound(checker, roundToAdd);
        Main.logger.info(wt + " Execute command: add round " + roundToAdd.getRoundNumber() + " command on weapon " +
                roundToAdd.getMyWeaponCompetition().getWeaponType());
    }

    @Override
    public void undo() {
        Main.logger.info(wt + " Undo command: add round " + roundToAdd.getRoundNumber() + " command on weapon " +
                roundToAdd.getMyWeaponCompetition().getWeaponType());
        weaponCompetition.removeRound(checker);
    }

    @Override
    public void redo() {
        execute();
        Main.logger.info(wt + " Redo command: " + roundToAdd.getRoundNumber() + " command on weapon " +
                roundToAdd.getMyWeaponCompetition().getWeaponType());
    }

}
