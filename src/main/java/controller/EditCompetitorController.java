package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Participant;
import model.enums.JudgeState;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
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
    Button editButton;
    @FXML
    Button cancelButton;
    @FXML
    DatePicker datePicker;


    public void setData(Participant p){
        System.out.format("Setting data in edit dialog\n");
        toEdit = p;
        this.updateView();
        datePicker.setValue(convertToLocalDateViaInstant(p.getLicenseExpDate()));
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
        }
    }

    public void cancelEditCompetitor(){
        Stage toClose = (Stage) cancelButton.getScene().getWindow();
        toClose.close();
        System.out.format("cancelEditCompetitor\n");
    }

    public void editCompetitor(){
        /* TODO: remove whitespace at the end and beggining */
        String name = competitorName.getText().trim();
        String surname = competitorSurname.getText().trim();

        if(Participant.checkFCanEdit(this.toEdit,name,surname)){
            toEdit.setName(competitorName.getText().trim());
            toEdit.setSurname(competitorSurname.getText().trim());
            toEdit.setLocation(competitorDivision.getText().trim());
            toEdit.setLocationGroup(competitorGroup.getText().trim());
            toEdit.setfSmallSwordParticipant(competitorFSmallSword.isSelected());
            toEdit.setfRapierParticipant(competitorFRapier.isSelected());
            toEdit.setfSabreParticipant(competitorFSabre.isSelected());
            toEdit.setJudgeState(competitorFMainReferee.isSelected() ? JudgeState.MAIN_JUDGE : JudgeState.NON_JUDGE);
            LocalDate ld = datePicker.getValue();
            Calendar c =  Calendar.getInstance();
            c.set(ld.getYear(), ld.getMonthValue() -1 , ld.getDayOfMonth());
            Date date = c.getTime();
            toEdit.setLicenseExpDate(date);

            Stage toClose = (Stage) editButton.getScene().getWindow();
            toClose.close();

            System.out.format("editCompetitor success\n");
        }else{
            Alert alert= new Alert(Alert.AlertType.ERROR,"New surname or name is not unique\n");
            alert.show();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        datePicker.setValue(LocalDate.now());
    }

}
