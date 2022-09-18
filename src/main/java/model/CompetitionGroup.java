package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompetitionGroup implements Serializable {

    private static int created = 0;
    private ObservableList<Fight> fightsList;
    private ObservableList<Participant> groupParticipants;
    private String groupID;
    private static final long serialVersionUID = 1;

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public CompetitionGroup(List<Fight> fights) {
        this.groupID = Integer.toString(created);
        created ++;
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

    public static void resetGroupId() { created = 0;}

    public String getGroupID(){
        return this.groupID;
    }

    public ObservableList<Participant> getGroupParticipants(){
        return this.groupParticipants;
    }

    public boolean fInGroup(Participant part)
    {
        return this.groupParticipants.contains(part);
    }

    public ObservableList<Fight> getFightsList() {
        return fightsList;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(groupID);

        ArrayList<Participant> participantArrayList = new ArrayList<>();
        groupParticipants.forEach(p -> participantArrayList.add(p));
        stream.writeObject(participantArrayList);

        ArrayList<Fight> fights = new ArrayList<>();
        fightsList.forEach(r -> fights.add(r));
        stream.writeObject(fights);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        groupID = (String) stream.readObject();
        groupParticipants = FXCollections.observableArrayList((ArrayList<Participant>) stream.readObject());
        fightsList = FXCollections.observableArrayList((ArrayList<Fight>) stream.readObject());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof CompetitionGroup){
            for (int i=0; i<((CompetitionGroup) obj).fightsList.size(); i++){
                Fight fight = ((CompetitionGroup) obj).fightsList.get(i);
                boolean found = false;
                for (int j=0; j<this.fightsList.size(); i++) {
                    if (this.fightsList.get(j).equals(fight)){
                        found = true;
                        break;
                    }
                }
                if (!found) return false;
            }
            for (int i=0; i<((CompetitionGroup) obj).groupParticipants.size(); i++){
                Participant p = ((CompetitionGroup) obj).groupParticipants.get(i);
                boolean found = false;
                for (int j=0; j<this.fightsList.size(); i++) {
                    if (this.groupParticipants.get(j).equals(p)){
                        found = true;
                        break;
                    }
                }
                if (!found) return false;
            }

            return this.groupID.equals(((CompetitionGroup) obj).groupID);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.groupID.hashCode() * 8 + this.fightsList.hashCode() * 17 +
                this.groupParticipants.hashCode();
    }

}
