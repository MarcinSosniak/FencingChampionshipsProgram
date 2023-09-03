package model.command;

import controller.EliminationController;
import model.Competition;
import model.Main;
import model.Participant;
import model.enums.WeaponType;

import java.util.Collections;
import java.util.List;

public class CommandAddInjury implements Command {

    private static final ValidInvocationChecker validInvocationChecker =  ValidInvocationChecker.getChecker();

    private Participant participant;

    private boolean _fSabreInjury = false;
    private boolean _fRapierInjury = false;
    private boolean _fSmallSwordInjury = false;
    private List<Command> commandsUsedList = null;

    private boolean oldSabreInjury;
    private boolean oldFRapierInjury;
    private boolean oldFSmallSwordInjury;
    private WeaponType stackWeaponType;

    public CommandAddInjury(Participant p, List<WeaponType> weaponList, WeaponType stackWeaponType) {
        this.participant = p;
        this.stackWeaponType = stackWeaponType;
        if (weaponList.contains(WeaponType.SABRE) || p.isInjured(WeaponType.SABRE)) _fSabreInjury = true;
        if (weaponList.contains(WeaponType.RAPIER) || p.isInjured(WeaponType.RAPIER)) _fRapierInjury = true;
        if (weaponList.contains(WeaponType.SMALL_SWORD) || p.isInjured(WeaponType.SMALL_SWORD))
            _fSmallSwordInjury = true;
    }

    public void executeCommand(){
        oldSabreInjury = participant.isInjured(WeaponType.SABRE);
        oldFRapierInjury = participant.isInjured(WeaponType.RAPIER);
        oldFSmallSwordInjury = participant.isInjured(WeaponType.SMALL_SWORD);
        System.out.println(_fRapierInjury + " " + _fSabreInjury + " " + _fSmallSwordInjury);

        participant.setfRapierInjury(validInvocationChecker, _fRapierInjury);
        participant.setfSabreInjury(validInvocationChecker, _fSabreInjury);
        participant.setfSmallSwordInjury(validInvocationChecker, _fSmallSwordInjury);

        commandsUsedList = Competition.getInstance().getWeaponCompetition(stackWeaponType).invalidateParticipant(validInvocationChecker, participant);

        for(Command c : commandsUsedList) {
            c.execute();
        }
        Collections.reverse(commandsUsedList);

        enableOrDisableRows(true);
    }


    @Override
    public void execute() {
        // new injury
        if (!oldFRapierInjury && _fRapierInjury)
            Main.logger.info(stackWeaponType + " Execute command: add rapier injury to " + participant.getName() + " " + participant.getSurname());
        if (_fSabreInjury)
            Main.logger.info(stackWeaponType + " Execute command: add sabre injury to " + participant.getName() + " " + participant.getSurname());
        if (_fSmallSwordInjury)
            Main.logger.info(stackWeaponType + " Execute command: add small sword injury to " + participant.getName() + " " + participant.getSurname());

        executeCommand();
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
        enableOrDisableRows(false);
        if (!oldFRapierInjury && _fRapierInjury)
            Main.logger.info(stackWeaponType + " Undo command: add rapier injury to " + participant.getName() + " " + participant.getSurname());
        if (_fSabreInjury)
            Main.logger.info(stackWeaponType + " Undo command: add sabre injury to " + participant.getName() + " " + participant.getSurname());
        if (_fSmallSwordInjury)
            Main.logger.info(stackWeaponType + " Undo command: add small sword injury to " + participant.getName() + " " + participant.getSurname());

    }

    @Override
    public void redo() {
        executeCommand();
        if (!oldFRapierInjury && _fRapierInjury)
            Main.logger.info(stackWeaponType + " Redo command: add rapier injury to " + participant.getName() + " " + participant.getSurname());
        if (_fSabreInjury)
            Main.logger.info(stackWeaponType + " Redo command: add sabre injury to " + participant.getName() + " " + participant.getSurname());
        if (_fSmallSwordInjury)
            Main.logger.info(stackWeaponType + " Redo command: add small sword injury to " + participant.getName() + " " + participant.getSurname());
    }

    private void enableOrDisableRows(boolean ifDisable){
        if (_fSabreInjury){
            EliminationController.getInstance().sabreRows.forEach(r -> {
                Participant participant1 = (Participant) r.getItem();
                if (participant1 != null && participant1.getName().equals(participant.getName()))
                    r.setDisable(ifDisable);
            });
        }
        if (_fRapierInjury) {
            EliminationController.getInstance().rapierRows.forEach(r -> {
                Participant participant1 = (Participant) r.getItem();
                if (participant1 != null && participant1.getName().equals(participant.getName()))
                    r.setDisable(ifDisable);
            });
        }

        if (_fSmallSwordInjury) {
            EliminationController.getInstance().smallSwordRows.forEach(r -> {
                Participant participant1 = (Participant) r.getItem();
                if (participant1 != null && participant1.getName().equals(participant.getName()))
                    r.setDisable(ifDisable);
            });
        }
    }
}
