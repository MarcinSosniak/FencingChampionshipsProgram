package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.DataGenerator;
import model.Participant;
import model.enums.JudgeState;


import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class CompetitorsViewController implements Initializable {

    private ObservableList<Participant> participants;

    public void setParticipants(ObservableList<Participant> newParticipants){
        this.participants = newParticipants;
    }


    /**S: Table view fields */
    @FXML
    TableView<Participant> competitorsTable;

    @FXML
    TableColumn<Participant,String> name;
    @FXML
    TableColumn<Participant,String> surname;
    @FXML
    TableColumn<Participant,String> club;
    @FXML
    TableColumn<Participant,String> group;
    @FXML
    TableColumn<Participant,Boolean> fSmallSwordParticipant;
    @FXML
    TableColumn<Participant,Boolean> fSabreParticipant;
    @FXML
    TableColumn<Participant,Boolean> fRapierParticipant;
    @FXML
    TableColumn<Participant, JudgeState> refereeStatus;

    @FXML
    TableColumn<Participant, BigDecimal> smallSwordPoints;
    @FXML
    TableColumn<Participant, BigDecimal> sabrePoints;
    @FXML
    TableColumn<Participant, BigDecimal> rapierPoints;

    @FXML
    TableColumn<Participant, Date> licence;
    /**E: Table view fields */













    @FXML
    public void goBack(){
        System.out.format("goBack (TODO:implement me)\n");
    }

    @FXML
    public void addNewCompetitor(){
        System.out.format("addNewCompetitor (implemented)\n");
        Stage childScene = ApplicationController.getApplicationController().renderStageAndSetOwner("/addCompetitor.fxml","Dodaj nowego zawodnika",true);
        childScene.showAndWait();
    }

    @FXML
    public void startCompetition(){
        System.out.format("startCompetiton (TODO:implement me)\n");
        System.out.format("Size: %d\n",participants.size());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        //System.out.format("Size: %d\n",participants.size());

        DataGenerator DG = new DataGenerator();
        this.setParticipants(DG.generateParticipants());
        competitorsTable.setItems(participants);

        name.setCellValueFactory(dataValue -> dataValue.getValue().nameProperty());
        surname.setCellValueFactory(dataValue -> dataValue.getValue().surnameProperty());
        club.setCellValueFactory(dataValue -> dataValue.getValue().locationProperty());
        group.setCellValueFactory(dataValue -> dataValue.getValue().locationGroupProperty());
        fSmallSwordParticipant.setCellValueFactory(dataValue ->{
            /*TOBE REMOVED :) */
            dataValue.getValue().fSmallSwordParticipantProperty().setValue(true);
            return dataValue.getValue().fSmallSwordParticipantProperty();
        }  );
        fSabreParticipant.setCellValueFactory(dataValue -> dataValue.getValue().fSabreParticipantProperty());
        fRapierParticipant.setCellValueFactory(dataValue -> dataValue.getValue().fRapierParticipantProperty());

        refereeStatus.setCellValueFactory(dataValue -> dataValue.getValue().judgeStateProperty());

        /*TODO: How to display points properly?? */

        //smallSwordPoints.setCellValueFactory( x ->  BooleanProperty(true));
//        sabrePoints.setCellValueFactory(dataValue -> dataValue.getValue().);
//        rapierPoints.setCellValueFactory(dataValue -> dataValue.getValue().);

        licence.setCellValueFactory(dataValue -> dataValue.getValue().licenseExpDateProperty());
        /*TODO: Why there is no participant in the table view */



        /** Right click to columns described here
         * https://stackoverflow.com/questions/26563390/detect-doubleclick-on-row-of-tableview-javafx
         *  */
    }
}
