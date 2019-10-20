package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Participant;
import model.enums.JudgeState;

import java.text.SimpleDateFormat;

public class AddCompetitorController {

    private ObservableList<Participant> participants;

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

    public void setData(ObservableList<Participant> participants){
        this.participants = participants;
    }

    public void cancelAddNewCompetitor(){
        Stage toClose = (Stage) cancelButton.getScene().getWindow();
        toClose.close();
        System.out.format("cancelAddNewCompetitor \n");
    }
    public void addNewCompetitor(){
        System.out.format("addNewCompetitor\n");
        try{
            Participant toAdd = new Participant(competitorName.getText(),competitorSurname.getText(),
                    competitorDivision.getText(),competitorGroup.getText(),competitorFMainReferee.isSelected()? "MAIN_JUDGE": "NON_JUDGE", competitorLicenceDate.getText());
            toAdd.setfSmallSwordParticipant(competitorFSmallSword.isSelected());
            toAdd.setfRapierParticipant(competitorFRapier.isSelected());
            toAdd.setfSabreParticipant(competitorFSabre.isSelected());
            participants.add(toAdd);
        }catch (Exception e){
            e.printStackTrace();
            System.out.format("Something went wrong while adding competitor\n");
            /**TODO: flash this info to client */
        }finally {
            Stage toClose = (Stage) addButton.getScene().getWindow();
            toClose.close();
        }

    }
}
