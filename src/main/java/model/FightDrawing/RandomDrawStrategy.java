package model.FightDrawing;

import model.CompetitionGroup;
import model.Fight;
import model.KillerDrawing.KillerRandomizerStrategy;
import model.Participant;
import model.Round;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static model.FightDrawing.FightDrawingUtils.drawFightsForSortedParticipants;


public class RandomDrawStrategy extends   FightDrawStrategy{
    private KillerRandomizerStrategy  killS;

    RandomDrawStrategy(KillerRandomizerStrategy killS)
    {
        this.killS=killS;
    }


    @Override
    public List<CompetitionGroup> drawFightsForRound(Round round, int groupSize, List<Participant> participants) {
        Collections.shuffle(participants);
        return drawFightsForSortedParticipants(round, groupSize, participants, killS);
    }


}
