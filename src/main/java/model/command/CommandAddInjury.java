package model.command;

import controller.EliminationController;
import model.Participant;
import model.WeaponCompetition;
import model.enums.WeaponType;

import java.util.Collections;
import java.util.List;

public class CommandAddInjury implements Command {

    private static final ValidInvocationChecker validInvocationChecker =  ValidInvocationChecker.getChecker();

    private Participant participant;
    private EliminationController controller;

    private boolean _fSabreInjury = false;
    private boolean _fRapierInjury = false;
    private boolean _fSmallSwordInjury = false;
    private WeaponCompetition competition;
    private List<Command> commandsUsedList = null;

    private boolean oldSabreInjury;
    private boolean oldFRapierInjury;
    private boolean oldFSmallSwordInjury;

    public CommandAddInjury(Participant p, List<WeaponType> weaponList, WeaponCompetition competition, EliminationController el) {
        this.participant = p;
        this.competition = competition;
        this.controller = el;
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

        enableOrDisableRows(true);
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
    }

    @Override
    public void redo() {
        execute();
    }

    private void enableOrDisableRows(boolean ifDisable){
        if (_fSabreInjury){
            controller.sabreRows.forEach(r -> {
                Participant participant1 = (Participant) r.getItem();
                if (participant1 != null && participant1.getName().equals(participant.getName())) r.setDisable(ifDisable);
            });
        }
        if (_fRapierInjury) {
            controller.rapierRows.forEach(r -> {
                Participant participant1 = (Participant) r.getItem();
                if (participant1 != null && participant1.getName().equals(participant.getName())) r.setDisable(ifDisable);
            });
        }

        if (_fSmallSwordInjury) {
            controller.smallSwordRows.forEach(r -> {
                Participant participant1 = (Participant) r.getItem();
                if (participant1 != null && participant1.getName().equals(participant.getName())) r.setDisable(ifDisable);
            });
        }
    }


}
