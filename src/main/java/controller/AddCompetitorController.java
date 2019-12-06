package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Competition;
import model.Participant;
import model.enums.JudgeState;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class AddCompetitorController implements Initializable {

    @FXML
    TextField competitorName;
    @FXML
    TextField competitorSurname;
    @FXML
    TextField competitorDivision;
    @FXML
    TextField competitorGroup;
    @FXML
    CheckBox fFemale;
    @FXML
    CheckBox competitorFSmallSword;
    @FXML
    CheckBox competitorFSabre;
    @FXML
    CheckBox competitorFRapier;
    @FXML
    CheckBox competitorFMainReferee;
    @FXML
    Button addButton;
    @FXML
    Button cancelButton;
    @FXML
    DatePicker datePicker;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        datePicker.setValue(LocalDate.now());
    }


    public void cancelAddNewCompetitor(){
        Stage toClose = (Stage) cancelButton.getScene().getWindow();
        toClose.close();
        System.out.format("cancelAddNewCompetitor \n");
    }
    public void addNewCompetitor(){
        /* TODO: remove whitespace at the end and beggining */
        System.out.format("addNewCompetitor\n");
        Participant toAdd = null;

        LocalDate ld = datePicker.getValue();
        Calendar c =  Calendar.getInstance();
        c.set(ld.getYear(), ld.getMonthValue() -1 , ld.getDayOfMonth());
        Date licenceExpDate = c.getTime();

        toAdd = new Participant(competitorName.getText().trim(),competitorSurname.getText().trim(),
                competitorDivision.getText().trim(),competitorGroup.getText().trim(),competitorFMainReferee.isSelected()? JudgeState.MAIN_JUDGE: JudgeState.NON_JUDGE, licenceExpDate);
        if(Participant.checkFNewUnique(toAdd)) {
            toAdd.setfSmallSwordParticipant(competitorFSmallSword.isSelected());
            toAdd.setfRapierParticipant(competitorFRapier.isSelected());
            toAdd.setfSabreParticipant(competitorFSabre.isSelected());
            toAdd.setfFemale(fFemale.isSelected());
            Competition.getInstance().getParticipants().add(toAdd);
            System.out.println("in confirm");
            Stage toClose = (Stage) addButton.getScene().getWindow();
            toClose.close();
        }else{
            Alert alert= new Alert(Alert.AlertType.ERROR,"New surname or name is not unique\n");
            alert.show();
        }
    }

}
