package model.KillerDrawing;

import model.Participant;

import java.util.Comparator;
import java.util.List;

public interface KillerRandomizerStrategy {

    List<List<Participant>>  drawKiller(List<Participant> participants, int opnentLessParticipantsCount, int fightsNeededCount);
}

class KillerComparator implements Comparator<Participant>
{
    public int compare(Participant a, Participant b)
    {
        return a.getTimesKiller() - b.getTimesKiller();
    }
}

class KillerReverseComparator implements Comparator<Participant>
{
    public int compare(Participant a, Participant b)
    {
        return  b.getTimesKiller() - a.getTimesKiller() ;
    }
}