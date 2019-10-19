package model.command;

import model.Participant;
import model.WeaponCompetition;
import model.enums.WeaponType;

import java.util.Collections;
import java.util.List;

public class CommandAddInjury implements Command{

    public static final class ValidInvocationChecker { private ValidInvocationChecker() {} }
    private static final ValidInvocationChecker validInvocationChecker = new ValidInvocationChecker();

    private Participant participant;
    private boolean oldFSabreInjury=false;
    private boolean oldFRapierInjury=false;
    private boolean oldFSmallSwordInjury=false;
    private boolean _fSabreInjury=false; /** _ To differentie them for fSabreInjury in the outer class**/
    private boolean _fRapierInjury=false;
    private boolean _fSmallSwordInjury=false;
    private WeaponCompetition competition;
    private List<Command> commandsUsedList=null;

    public CommandAddInjury(Participant p, List<WeaponType> weaponList, WeaponCompetition competition)
    {
        this.participant = p;
        this.competition=competition;
        if(weaponList.contains(WeaponType.SABRE))
            _fSabreInjury=true;
        if(weaponList.contains(WeaponType.RAPIER))
            _fRapierInjury=true;
        if(weaponList.contains(WeaponType.SMALL_SWORD))
            _fSmallSwordInjury=true;
    }

    public CommandAddInjury(Participant p, WeaponType single,WeaponCompetition competition)
    {
        this.participant = p;
        this.competition=competition;
        if(single==WeaponType.SABRE)
            _fSabreInjury=true;
        else if(single==WeaponType.RAPIER)
            _fRapierInjury=true;
        else if(single==WeaponType.SMALL_SWORD)
            _fSmallSwordInjury=true;
    }


    @Override
    public void execute() {
        oldFSabreInjury = participant.getfSabreInjury();
        oldFRapierInjury = participant.getfRapierInjury();
        oldFSmallSwordInjury = participant.getfRapierInjury();
        fSabreInjury.set(_fSabreInjury);
        fRapierInjury.set(_fRapierInjury);
        fSmallSwordInjury.set(_fSmallSwordInjury);
        commandsUsedList=competition.invalidateParticipant(participant);
        for(Command c : commandsUsedList)
        {
            c.execute();
        }
        Collections.reverse(commandsUsedList);
    }

    @Override
    public void undo() {
        fSabreInjury.set(oldFSabreInjury);
        fRapierInjury.set(oldFRapierInjury);
        fSmallSwordInjury.set(oldFSmallSwordInjury);
        for (Command c : commandsUsedList) {
            c.undo();
        }
        Collections.reverse(commandsUsedList);
    }

    @Override
    public void redo() {
        oldFSabreInjury=fSabreInjury.get();
        oldFRapierInjury=fRapierInjury.get();
        oldFSmallSwordInjury=fSmallSwordInjury.get();
        fSabreInjury.set(_fSabreInjury);
        fRapierInjury.set(_fRapierInjury);
        fSmallSwordInjury.set(_fSmallSwordInjury);
        for(Command c : commandsUsedList)
        {
            c.redo();
        }
        Collections.reverse(commandsUsedList);
    }
}
