package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Competition;
import model.Participant;
import model.WeaponCompetition;
import model.command.CommandAddInjury;
import model.enums.WeaponType;

import java.util.ArrayList;
import java.util.List;


public class AddInjuryController {

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

    private Participant p;
    private WeaponType wt;
    private EliminationController el;

    @FXML
    private void cancel(){
        Stage toClose = (Stage) cancelButton.getScene().getWindow();
        toClose.close();
        System.out.format("cancelAddInjury\n");
    }
    @FXML
    private void confirm(){
        List<WeaponType> weaponTypesToSetInjury = new ArrayList<>();

        if (!SabreInjury.isDisable() && SabreInjury.isSelected())
            weaponTypesToSetInjury.add(WeaponType.SABRE);

        if (!RapierInjury.isDisable() && RapierInjury.isSelected())
            weaponTypesToSetInjury.add(WeaponType.RAPIER);

        if (!SmallSwordInjury.isDisable() && SmallSwordInjury.isSelected())
            weaponTypesToSetInjury.add(WeaponType.SMALL_SWORD);


        if(weaponTypesToSetInjury.size()!=0) {
            WeaponCompetition wc = Competition.getInstance().getWeaponCompetition(wt);
            wc.getcStack().executeCommand(new CommandAddInjury(p, weaponTypesToSetInjury, wc.getWeaponType()));
            el.update();
        }
        Stage toClose = (Stage) confirmButton.getScene().getWindow();
        toClose.close();
        System.out.format("confirmAddInjury\n");
    }

    public void setData(Participant p, WeaponType wt, EliminationController el) {
        this.p = p;
        this.wt = wt;
        this.el = el;
        this.update();
    }

    private void update(){
        this.text.setText("Czy na pewno chcesz usunąć zawodnika \n" + p.getName() + " " + p.getSurname() + " z zawodów?\n");

        if (p.fSabreParticipantProperty().getValue()) {
            SabreInjury.setSelected(false);
            if(p.isInjured(WeaponType.SABRE))
            {
                SabreInjury.setSelected(true);
                SabreInjury.setDisable(true);
            }
        }
        else SabreInjury.setDisable(true);

        if (p.fSmallSwordParticipantProperty().getValue()) {
            SmallSwordInjury.setSelected(false);
            if(p.isInjured(WeaponType.SMALL_SWORD))
            {
                SmallSwordInjury.setSelected(true);
                SmallSwordInjury.setDisable(true);
            }
        }
        else SmallSwordInjury.setDisable(true);

        if (p.fRapierParticipantProperty().getValue()) {
            RapierInjury.setSelected(false);
            if(p.isInjured(WeaponType.RAPIER))
            {
                RapierInjury.setSelected(true);
                RapierInjury.setDisable(true);
            }
        }
        else RapierInjury.setDisable(true);
    }
}
