package model.command;

import model.Round;
import model.WeaponCompetition;

import java.util.List;

public class AddRoundCommand implements Command{

    public static final class ValidInvocationChecker { private ValidInvocationChecker() {} }
    private static final ValidInvocationChecker checker = new ValidInvocationChecker();

    private WeaponCompetition weaponCompetition;
    private Round roundToAdd;

    public AddRoundCommand(WeaponCompetition weaponCompetition, Round roundToAdd){
        this.weaponCompetition = weaponCompetition;
        this.roundToAdd = roundToAdd;
    }

    @Override
    public void execute() {
        weaponCompetition.addRound(checker, roundToAdd);
    }

    @Override
    public void undo() {
        weaponCompetition.removeRound(checker);
    }

    @Override
    public void redo() {
        execute();
    }

    private Round _round;

    public AddRoundCommand(Round round)
    {
        this._round=round;
    }

}