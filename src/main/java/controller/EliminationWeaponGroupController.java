package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Participant;
import model.enums.WeaponType;

public class EliminationWeaponGroupController {



    @FXML
    private WeaponType type;

    @FXML
    private ObservableList<Participant> participants;

    public void setData(WeaponType WeaponType,ObservableList<Participant> participants){
        this.type = WeaponType;
        this.participants = participants;
        this.updateView();
    }

    public void updateView(){

    }



}
