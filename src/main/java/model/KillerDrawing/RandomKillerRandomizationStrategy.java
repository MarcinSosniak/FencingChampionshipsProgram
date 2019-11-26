package model.KillerDrawing;

import javafx.scene.control.cell.PropertyValueFactoryBuilder;
import model.KillerDrawing.KillerRandomizerStrategy;
import model.Participant;
import model.enums.JudgeState;

import java.util.*;
import java.util.stream.Collectors;

public class RandomKillerRandomizationStrategy implements KillerRandomizerStrategy {

    public RandomKillerRandomizationStrategy(){}
    //return List of Killers for every one that needs it
    public List<List<Participant>>  drawKiller(List<Participant> participantsNotInKillerGroup,int oponentLessParticipantsCount, int fightsNeededPerPerson)
    {
        List<Participant> nonJudges= participantsNotInKillerGroup.stream().filter(p-> p.getJudgeState()== JudgeState.NON_JUDGE).collect(Collectors.toList());
        List<Participant> killers = new ArrayList<>();
        int totalFights = oponentLessParticipantsCount*fightsNeededPerPerson;
        if(nonJudges.size() < oponentLessParticipantsCount* fightsNeededPerPerson)
            return killersWithMultipleFights(nonJudges,oponentLessParticipantsCount,fightsNeededPerPerson);
        //we made sure taht there will be enough of killers so they don't have multiple fights;

        nonJudges.sort(new KillerComparator());
        Map<Integer,ArrayList<Participant>> timesKillerMap = new HashMap<>();

        for(Participant part : nonJudges)
        {
            int timesKiller = part.getTimesKiller();
            if ( ! timesKillerMap.containsKey(timesKiller))
                timesKillerMap.put(timesKiller,new ArrayList<Participant>());
            ArrayList<Participant> parts  = timesKillerMap.get(timesKiller);
            parts.add(part);
        }
        List<Integer> sortedKeys = new ArrayList<>(timesKillerMap.keySet());
        Collections.sort(sortedKeys);
        for(Integer key: sortedKeys)
        {
            Collections.shuffle(timesKillerMap.get(key));
        }

        int totalFightsNeededCounter = totalFights;
        for(Integer key : sortedKeys)
        {
            for(Participant part : timesKillerMap.get(key))
            {
                if(totalFightsNeededCounter <= 0)
                    break;
                killers.add(part);
                totalFightsNeededCounter --;
            }
        }


//        int killerCountMaximum = nonJudges.get(oponentLessParticipantsCount*fightsNeededPerPerson-1).getTimesKiller();
//        int toRandomMinIndex=-1; //all with low enough will be killers 100% other not so much
//        int toRandomMaxIndex=-1;
//        for(int i=0;i<nonJudges.size();i++)
//        {
//            if(toRandomMinIndex == -1 ||
//                    nonJudges.get(i).getTimesKiller()
//                            == killerCountMaximum)
//            {
//                toRandomMinIndex=i;
//            }
//            if(nonJudges.get(i).getTimesKiller() > killerCountMaximum)
//            {
//                toRandomMaxIndex=i;
//                break; // no need to search further
//            }
//        }
//        killers.addAll(nonJudges.subList(0,toRandomMinIndex));
//        Collections.shuffle(nonJudges.subList(toRandomMinIndex,toRandomMaxIndex));
//        killers.addAll(nonJudges.subList(toRandomMinIndex,toRandomMaxIndex).subList(0,oponentLessParticipantsCount*fightsNeededPerPerson - killers.size()));
        Collections.shuffle(killers);

        if(killers.size() != oponentLessParticipantsCount*fightsNeededPerPerson)
            throw new IllegalStateException("bug killers list should have exactly needed amout of killers");


        Iterator<Participant> iter= killers.iterator();
        List<List<Participant>> out = new ArrayList<>();
        for(int participantNumber=0;participantNumber<oponentLessParticipantsCount;participantNumber++)
        {
            List<Participant> thisParticipantKillers= new ArrayList<>();
            for(int fightNo=0;fightNo<fightsNeededPerPerson;fightNo++)
            {
                Participant killer= iter.next();
                killer.incTimesKiller();
                thisParticipantKillers.add(killer);
            }
            out.add(thisParticipantKillers);
        }

        return out;
    }

    private List<List<Participant>> killersWithMultipleFights(List<Participant> killers, int oponentLessParticipantsCount, int fightsNeededPerPerson )
    {
        if (killers.size() < fightsNeededPerPerson)
        { //
            throw new IllegalStateException("There is not enought killers. Single killer would have to fight with the same opnent more than once");
        }
        Collections.shuffle(killers);
        Iterator<Participant> iter= killers.iterator();
        List<List<Participant>> out = new ArrayList<>();
        for(int participantNumber=0;participantNumber<oponentLessParticipantsCount;participantNumber++)
        {
            List<Participant> thisParticipantKillers= new ArrayList<>();
            for(int fightNo=0;fightNo<fightsNeededPerPerson;fightNo++)
            {
                if(!iter.hasNext())
                {
                    iter=killers.iterator();
                }
                Participant killer= iter.next();
                killer.incTimesKiller();
                thisParticipantKillers.add(killer);
            }
            out.add(thisParticipantKillers);
        }
        return out;
    }
}
