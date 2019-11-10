package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class CompetitionGroup {

    private ObservableList<Fight> fightsList;
    private ObservableList<Participant> groupParticipants;
    private String groupID;

    public CompetitionGroup(List<Fight> fights)
    {
        this.groupID = "a";
        this.fightsList = FXCollections.observableArrayList(fights);
        this.groupParticipants = FXCollections.observableArrayList();
        for(Fight f: fightsList){
            if(!this.groupParticipants.contains(f.getFirstParticipant())){
                this.groupParticipants.add(f.getFirstParticipant());
            }
            if(!this.groupParticipants.contains(f.getSecondParticipant())){
                this.groupParticipants.add(f.getSecondParticipant());
            }
        }
    }

    public ObservableList<Fight> getFightsList() {
        return fightsList;
    }


    public boolean fInGroup(Participant part)
    {
        return this.groupParticipants.contains(part);
    }

    public ObservableList<Participant> getGroupParticipants(){
        return this.groupParticipants;
    }

    public String getGroupID(){
        return this.groupID;
    }

}
