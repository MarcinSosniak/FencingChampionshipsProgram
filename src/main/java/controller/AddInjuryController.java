package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Participant;
import model.enums.WeaponType;


public class AddInjuryController {

    Participant p;

    @FXML
    Text text;
    @FXML
    CheckBox SabreInjury;
    @FXML
    CheckBox SmallSwordInjury;
    @FXML
    CheckBox RapierInjury;

    @FXML
    Button cancelButton;
    @FXML
    Button confirmButton;

    @FXML
    private void cancel(){
        Stage toClose = (Stage) cancelButton.getScene().getWindow();
        toClose.close();
        System.out.format("cancelAddInjury\n");
    }
    @FXML
    private void confirm(){
        if(!SabreInjury.isDisable() && SabreInjury.isSelected()){
            p.addInjury(WeaponType.SABRE,null);
        }
        if(!RapierInjury.isDisable() && RapierInjury.isSelected()){
            p.addInjury(WeaponType.RAPIER,null);
        }
        if(!SmallSwordInjury.isDisable() && SmallSwordInjury.isSelected()){
            p.addInjury(WeaponType.SMALL_SWORD,null);
        }

        Stage toClose = (Stage) confirmButton.getScene().getWindow();
        toClose.close();
        System.out.format("confirmAddInjury\n");
    }

    public void setData(Participant p) {
        this.p = p;
        this.update();
    }
    private void update(){
        this.text.setText("Are you sure that you want to remove \n" + p.getName() + " " + p.getSurname() + " from competition?\n");


        if(p.fSabreParticipantProperty().getValue()){
            SabreInjury.setSelected(false);
        }else {
            SabreInjury.setDisable(true);
        }

        if(p.fSmallSwordParticipantProperty().getValue()){
            SmallSwordInjury.setSelected(false);
        }else {
            SmallSwordInjury.setDisable(true);
        }

        if(p.fRapierParticipantProperty().getValue()){
            RapierInjury.setSelected(false);
        }else {
            RapierInjury.setDisable(true);
        }
    }
}
