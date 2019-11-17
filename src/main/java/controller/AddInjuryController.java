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

        if (!SabreInjury.isDisable() && SabreInjury.isSelected()){
            weaponTypesToSetInjury.add(WeaponType.SABRE);
            el.sabreRows.forEach(r -> {
                Participant participant1 = (Participant) r.getItem();
                if (participant1 != null && participant1.getName().equals(p.getName())) r.setDisable(true);
            });
        }

        if (!RapierInjury.isDisable() && RapierInjury.isSelected()){
            weaponTypesToSetInjury.add(WeaponType.RAPIER);
            el.rapierRows.forEach(r -> {
                Participant participant1 = (Participant) r.getItem();
                if (participant1 != null && participant1.getName().equals(p.getName())) r.setDisable(true);
            });
        }
        if(!SmallSwordInjury.isDisable() && SmallSwordInjury.isSelected()){
            weaponTypesToSetInjury.add(WeaponType.SMALL_SWORD);
            el.smallSwordRows.forEach(r -> {
                Participant participant1 = (Participant) r.getItem();
                if (participant1 != null && participant1.getName().equals(p.getName())) r.setDisable(true);
            });
        }

        WeaponCompetition wc =  Competition.getInstance().getWeaponCompetition(wt);
        wc.getcStack().executeCommand( new CommandAddInjury(p, weaponTypesToSetInjury, wc));


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
        this.text.setText("Are you sure that you want to remove \n" + p.getName() + " " + p.getSurname() + " from competition?\n");

        if (p.fSabreParticipantProperty().getValue()) SabreInjury.setSelected(false);
        else SabreInjury.setDisable(true);

        if (p.fSmallSwordParticipantProperty().getValue()) SmallSwordInjury.setSelected(false);
        else SmallSwordInjury.setDisable(true);

        if (p.fRapierParticipantProperty().getValue()) RapierInjury.setSelected(false);
        else RapierInjury.setDisable(true);
    }
}
