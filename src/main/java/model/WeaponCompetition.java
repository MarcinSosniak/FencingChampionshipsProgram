package model;

import model.enums.WeaponType;

import java.util.ArrayList;
import java.util.List;

public class WeaponCompetition {

    private WeaponType weaponType;
    private List<Participant> participants;
    private List<Round> rounds;

    public WeaponCompetition(WeaponType weaponType, List participants){
        this.weaponType = weaponType;
        this.participants = participants;
        this.rounds = new ArrayList<>();
    }

    public void addParticipantToWeaponCompetition (Participant participant){ participants.add(participant); }

    public void removeParticipantFromWeaponCompetition (Participant participant){ participants.remove(participant); }

    public void addRound(Round round){ rounds.add(round); }
}