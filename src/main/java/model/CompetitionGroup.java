package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class CompetitionGroup {

    private ObservableList<Fight> fightsList;
    public ObservableList<Fight> getFightsList() {
        return fightsList;
    }

    public CompetitionGroup(List<Fight> fights)
    {
        fightsList = FXCollections.observableArrayList(fights);
    }


    public boolean fInGroup(Participant part)
    {
        for(Fight fight : fightsList)
        {
            if(fight.fIn(part))
                return true;
        }
        return false;
    }

}
