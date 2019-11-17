package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Participant;
import model.enums.JudgeState;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class EditCompetitorController implements Initializable {

    @FXML
    private Participant toEdit;

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
    Button editButton;
    @FXML
    Button cancelButton;


    public void setData(Participant p){
        System.out.format("Setting data in edit dialog\n");
        toEdit = p;
        this.updateView();
    }

    private void updateView(){
        if(toEdit != null){
            competitorName.setText(toEdit.nameProperty().getValue());
            competitorSurname.setText(toEdit.surnameProperty().getValue());
            competitorDivision.setText(toEdit.locationProperty().getValue());
            competitorGroup.setText(toEdit.locationGroupProperty().getValue());
            competitorFSmallSword.setSelected(toEdit.fSmallSwordParticipantProperty().getValue());
            competitorFRapier.setSelected(toEdit.fRapierParticipantProperty().getValue());
            competitorFSabre.setSelected(toEdit.fSabreParticipantProperty().getValue());
            competitorFMainReferee.setSelected(toEdit.getJudgeState() == JudgeState.MAIN_JUDGE);
            competitorLicenceDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(toEdit.licenseExpDateProperty().getValue()));
        }
    }

    public void cancelEditCompetitor(){
        Stage toClose = (Stage) cancelButton.getScene().getWindow();
        toClose.close();
        System.out.format("cancelEditCompetitor\n");
    }

    public void editCompetitor(){
        toEdit.setName(competitorName.getText());
        toEdit.setSurname(competitorSurname.getText());
        toEdit.setLocation(competitorDivision.getText());
        toEdit.setLocationGroup(competitorGroup.getText());
        toEdit.setfSmallSwordParticipant(competitorFSmallSword.isSelected());
        toEdit.setfRapierParticipant(competitorFRapier.isSelected());
        toEdit.setfSabreParticipant(competitorFSabre.isSelected());
        toEdit.setJudgeState(competitorFMainReferee.isSelected() ? JudgeState.MAIN_JUDGE : JudgeState.NON_JUDGE);
        try {
            toEdit.setLicenseExpDate(new SimpleDateFormat("dd-MM-yyyy").parse(competitorLicenceDate.getText()));
        }catch (Exception e){
            e.printStackTrace();
            System.out.format("Couldn't fomat data provide in format dd-MM-yyyy");
        }
        Stage toClose = (Stage) editButton.getScene().getWindow();
        toClose.close();

        System.out.format("editCompetitor\n");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

}
