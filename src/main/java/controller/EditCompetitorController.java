package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Participant;
import model.enums.JudgeState;
import model.enums.WeaponType;

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
    CheckBox fFemale;
    @FXML
    TextField oldSmallSword;
    @FXML
    TextField oldRapier;
    @FXML
    TextField oldSabre;
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
            fFemale.setSelected(toEdit.isfFemale());
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

        int iOldSmallSword =0;
        int iOldSabre = 0;
        int iOldRapier =0;
        try{ iOldSmallSword=Integer.parseInt(oldSmallSword.getText().trim()); } catch (Exception ex) {iOldSmallSword=0;}
        try{ iOldSabre=Integer.parseInt(oldSabre.getText().trim()); } catch (Exception ex) {iOldSmallSword=0;}
        try{ iOldRapier=Integer.parseInt(oldRapier.getText().trim()); } catch (Exception ex) {iOldSmallSword=0;}
        if( iOldSmallSword > 900) iOldSmallSword = 900;
        if( iOldSmallSword < 0) iOldSmallSword = 0;
        if( iOldSabre > 900) iOldSabre = 900;
        if( iOldSabre < 0) iOldSabre =0;
        if( iOldRapier > 900) iOldRapier = 900;
        if( iOldRapier < 0) iOldRapier = 0;

        if(Participant.checkFCanEdit(this.toEdit,name,surname) && !competitorName.getText().trim().equals("") && !competitorSurname.getText().trim().equals("")){
            toEdit.setName(competitorName.getText().trim());
            toEdit.setSurname(competitorSurname.getText().trim());
            toEdit.setLocation(competitorDivision.getText().trim());
            toEdit.setLocationGroup(competitorGroup.getText().trim());
            toEdit.setfFemale(fFemale.isSelected());
            toEdit.setfSmallSwordParticipant(competitorFSmallSword.isSelected());
            toEdit.setfRapierParticipant(competitorFRapier.isSelected());
            toEdit.setfSabreParticipant(competitorFSabre.isSelected());
            toEdit.setJudgeState(competitorFMainReferee.isSelected() ? JudgeState.MAIN_JUDGE : JudgeState.NON_JUDGE);
            toEdit.setOldSeasonWeapoPointsPropety(WeaponType.SMALL_SWORD,iOldSmallSword);
            toEdit.setOldSeasonWeapoPointsPropety(WeaponType.SABRE,iOldSabre);
            toEdit.setOldSeasonWeapoPointsPropety(WeaponType.RAPIER,iOldRapier);
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
