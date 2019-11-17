package model.command;

import model.Participant;
import model.WeaponCompetition;
import model.enums.WeaponType;

import java.util.Collections;
import java.util.List;

public class CommandAddInjury implements Command {

    public static final class ValidInvocationChecker { private ValidInvocationChecker() {} }
    private static final ValidInvocationChecker validInvocationChecker = new ValidInvocationChecker();

    private Participant participant;

    private boolean _fSabreInjury = false;
    private boolean _fRapierInjury = false;
    private boolean _fSmallSwordInjury = false;
    private WeaponCompetition competition;
    private List<Command> commandsUsedList = null;

    private boolean oldSabreInjury;
    private boolean oldFRapierInjury;
    private boolean oldFSmallSwordInjury;

    public CommandAddInjury(Participant p, List<WeaponType> weaponList, WeaponCompetition competition) {
        this.participant = p;
        this.competition = competition;
        if (weaponList.contains(WeaponType.SABRE)) _fSabreInjury = true;
        if (weaponList.contains(WeaponType.RAPIER)) _fRapierInjury = true;
        if (weaponList.contains(WeaponType.SMALL_SWORD)) _fSmallSwordInjury = true;
    }


    @Override
    public void execute() {
        System.out.println("in execute command add injury");
        oldSabreInjury = participant.isInjured(WeaponType.SABRE);
        oldFRapierInjury = participant.isInjured(WeaponType.RAPIER);
        oldFSmallSwordInjury = participant.isInjured(WeaponType.SMALL_SWORD);
        System.out.println(_fRapierInjury + " " + _fSabreInjury + " " + _fSmallSwordInjury);

        participant.setfRapierInjury(validInvocationChecker, _fRapierInjury);
        participant.setfSabreInjury(validInvocationChecker, _fSabreInjury);
        participant.setfSmallSwordInjury(validInvocationChecker, _fSmallSwordInjury);

        commandsUsedList = competition.invalidateParticipant(validInvocationChecker, participant);
        for(Command c : commandsUsedList) {
            c.execute();
        }
        Collections.reverse(commandsUsedList);
    }

    @Override
    public void undo() {
        participant.setfRapierInjury(validInvocationChecker, oldFRapierInjury);
        participant.setfSabreInjury(validInvocationChecker, oldSabreInjury);
        participant.setfSmallSwordInjury(validInvocationChecker, oldFSmallSwordInjury);

        for (Command c : commandsUsedList) {
            c.undo();
        }
        Collections.reverse(commandsUsedList);
    }

    @Override
    public void redo() {
        execute();
    }
}
