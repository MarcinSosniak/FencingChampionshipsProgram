package controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import model.Competition;
import model.Participant;
import model.WeaponCompetition;
import model.enums.WeaponType;
import model.exceptions.NoSuchCompetitionException;
import model.exceptions.NoSuchWeaponException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EliminationController {


    private Competition competition;

    @FXML
    private ObservableMap<WeaponType,ObservableList<Participant>> weaponCompetitionParticipants;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab rapierTab;
    @FXML
    private Tab sabreTab;
    @FXML
    private Tab smallSwordTab;


    public EliminationController(){

    }



    public void update() {
        rapierTab = initTab(WeaponType.RAPIER);
        sabreTab = initTab(WeaponType.SABRE);
        smallSwordTab = initTab(WeaponType.SMALL_SWORD);
        tabPane.getTabs().add(rapierTab);
        tabPane.getTabs().add(sabreTab);
        tabPane.getTabs().add(smallSwordTab);
    }

    public void setData(Competition competition){
        this.competition = competition;
        this.weaponCompetitionParticipants = FXCollections.observableHashMap();
        for(WeaponType wt: WeaponType.values()){
            try{
                this.weaponCompetitionParticipants.put(wt, FXCollections.observableArrayList(this.competition.getWeaponCompetition(wt).getParticipants()));

            } catch (NoSuchCompetitionException e){
                e.printStackTrace();
                System.out.format("No " + wt.toString() + " competitions\n");
            }
        }
        this.update();
    }

    /* TODO: Do i need to call update after data set? */

    public Tab initTab( WeaponType wt){
        Tab tabToRet = new Tab();
        tabToRet.setText(wt.toString());
        TableView tv = new TableView();
        tv.setItems(weaponCompetitionParticipants.get(wt));

        TableColumn<Participant,String> name = new TableColumn<Participant,String>("Name");
        TableColumn<Participant,String> surname = new TableColumn<Participant,String>("Surname");
        TableColumn<Participant,Integer> points = new TableColumn<Participant,Integer>("Points");
        TableColumn<Participant,String> group = new TableColumn<Participant,String>("Group");

        name.setCellValueFactory(x -> x.getValue().nameProperty());
        surname.setCellValueFactory(x -> x.getValue().surnameProperty());
        points.setCellValueFactory(x -> {
                try {
                    return x.getValue().getPointsForWeaponProperty(wt).asObject();
                } catch (NoSuchWeaponException e) {
                    e.printStackTrace();
                    return new SimpleObjectProperty<>(new Integer(0));
                }
        });
        group.setCellValueFactory(x -> {
            try{
                String g = competition.getWeaponCompetition(wt).groupForParticipant(x.getValue());
                return new SimpleStringProperty(g);
            } catch (NoSuchCompetitionException e){
                e.printStackTrace();
                return new SimpleStringProperty("NoSuchCompetitionException");
            }
        });

        tv.getColumns().addAll(name,surname,group,points);

//        tv.setRowFactory( row ->{
//            TableRow<Participant> tableRow = new TableRow<>();
//            tableRow.setOnMouseClicked( event -> {
//                if(event.getButton().equals(MouseButton.SECONDARY) && !tableRow.isEmpty()){
//                    Participant p = tableRow.getItem();
//                    Stage childScene = ApplicationController.getApplicationController()
//                }
//            });
//        });



        /*TODO: add right click */

        tabToRet.setContent(tv);
        return tabToRet;
    }

}
