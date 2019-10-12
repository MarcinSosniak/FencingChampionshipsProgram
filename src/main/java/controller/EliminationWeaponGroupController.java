package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Participant;
import model.enums.WeaponType;

public class EliminationWeaponGroupController {

    /**|-------------------------------------------------------------------------|
     * |         PEOPLE(resizeable)          |   Control button Pane?            |
     * | Surname Name Points Group           |                                   |
     * | (Right click to remove participant  |                                   |
     * |-------------------------------------------------------------------------|
     * |                         GROUP                                           |
     * |                                                                         |
     * | GROUP A         GROUP B .....                                           |
     * |                                                                         |
     * | ------------------------------------------------------------------------|
     * |                         FIGHT RESULTS                                   |
     * |                                                                         |
     * |                 GROUP A                                                 |
     * |    Participant1(red) x Participant2(green)                              |
     * |    color indicates who won                                              |
     * |-------------------------------------------------------------------------| */

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
