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
    TextField oldSmallSword;
    @FXML
    TextField oldRapier;
    @FXML
    TextField oldSabre;
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
        int iOldSmallSword =0;
        int iOldSabre = 0;
        int iOldRapier =0;
        try{ iOldSmallSword=Integer.parseInt(oldSmallSword.getText().trim()); } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Small sword points form last seazon are not a number");
            alert.show();
            return;
        }
        try{ iOldSabre=Integer.parseInt(oldSabre.getText().trim()); } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Sabre points form last seazon are not a number");
            alert.show();
            return;
        }
        try{ iOldRapier=Integer.parseInt(oldRapier.getText().trim()); } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Rapier points form last seazon are not a number");
            alert.show();
            return;
        }
        if( iOldSmallSword > 900) iOldSmallSword = 900;
        if( iOldSmallSword < 0) iOldSmallSword = 0;
        if( iOldSabre > 900) iOldSabre = 900;
        if( iOldSabre < 0) iOldSabre =0;
        if( iOldRapier > 900) iOldRapier = 900;
        if( iOldRapier < 0) iOldRapier = 0;


        toAdd = new Participant(competitorName.getText().trim(),competitorSurname.getText().trim(),
                competitorDivision.getText().trim(),competitorGroup.getText().trim(),competitorFMainReferee.isSelected()? JudgeState.MAIN_JUDGE: JudgeState.NON_JUDGE, licenceExpDate,
                iOldSmallSword,iOldSabre,iOldRapier);
        if(Participant.checkFNewUnique(toAdd) && !competitorName.getText().trim().equals("") && !competitorSurname.getText().trim().equals("") ) {
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
