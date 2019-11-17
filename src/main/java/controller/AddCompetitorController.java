package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Competition;
import model.Participant;
import model.enums.JudgeState;

import java.text.SimpleDateFormat;
import java.util.List;

public class AddCompetitorController {

    @FXML
    TextField competitorName;
    @FXML
    TextField competitorSurname;
    @FXML
    TextField competitorDivision;
    @FXML
    TextField competitorGroup;
    @FXML
    CheckBox competitorFSmallSword;
    @FXML
    CheckBox competitorFSabre;
    @FXML
    CheckBox competitorFRapier;
    @FXML
    CheckBox competitorFMainReferee;
    @FXML
    TextField competitorLicenceDate;
    @FXML
    Button addButton;
    @FXML
    Button cancelButton;


    public void cancelAddNewCompetitor(){
        Stage toClose = (Stage) cancelButton.getScene().getWindow();
        toClose.close();
        System.out.format("cancelAddNewCompetitor \n");
    }
    public void addNewCompetitor(){
        System.out.format("addNewCompetitor\n");
        Participant toAdd = null;
        try {
            toAdd = new Participant(competitorName.getText(),competitorSurname.getText(),
                    competitorDivision.getText(),competitorGroup.getText(),competitorFMainReferee.isSelected()? "MAIN_JUDGE": "NON_JUDGE", competitorLicenceDate.getText());
            toAdd.setfSmallSwordParticipant(competitorFSmallSword.isSelected());
            toAdd.setfRapierParticipant(competitorFRapier.isSelected());
            toAdd.setfSabreParticipant(competitorFSabre.isSelected());

        } catch (Exception e){
            e.printStackTrace();
            System.out.format("Something went wrong while adding competitor\n");
            /**TODO: flash this info to client */
        }
        finally {
            System.out.println("in confirm");
            // add new participant to competition
            Competition.getInstance().getParticipants().add(toAdd);
            Stage toClose = (Stage) addButton.getScene().getWindow();
            toClose.close();
        }
    }

}
