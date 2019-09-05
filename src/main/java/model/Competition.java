package model;

import java.util.List;

public class Competition {

    private List<WeaponCompetition> weaponCompetitions;
    private List<Participant> participants;
    private KillerRandomizerStrategy killerRandomizerStrategy;

    public Competition(List<WeaponCompetition> competitions, List<Participant> participants, KillerRandomizerStrategy killerRandomizerStrategy){
        this.weaponCompetitions = competitions;
        this.participants = participants;
        this.killerRandomizerStrategy = killerRandomizerStrategy;
    }

    public void changeKillerRandomizerStrategy(KillerRandomizerStrategy killerRandomizerStrategy){
        this.killerRandomizerStrategy = killerRandomizerStrategy;
    }

    public KillerRandomizerStrategy getKillerRandomizerStrategy() { return killerRandomizerStrategy; }

    public List<Participant> getParticipants() { return participants; }

    public List<WeaponCompetition> getWeaponCompetitions() { return weaponCompetitions; }

}
