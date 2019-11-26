package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import model.Competition;
import model.Participant;
import model.Round;
import model.WeaponCompetition;
import model.command.ChangePointsCommand;
import model.enums.WeaponType;
import model.exceptions.NoSuchWeaponException;
import util.RationalNumber;


public class AddPointsController {

    @FXML
    Spinner<Double> spinner;

    @FXML
    Button cancelButton;
    @FXML
    Button confirmButton;

    private Participant participant;
    private Round round;

    public AddPointsController(){}

    public void setData(Participant participant, Round round){
        this.participant = participant;
        this.round = round;
    }

    public void cancelAddingPoints(){
        Stage toClose = (Stage) cancelButton.getScene().getWindow();
        toClose.close();
        System.out.format("cancelAddingPoints\n");
    }

    public void addPoints(){
        System.out.println("spinner: " + spinner.getValue());
        RationalNumber pointsToAdd =  new RationalNumber((int)(spinner.getValue() * 10), 10);

        WeaponCompetition wc = round.getMyWeaponCompetition();
        wc.getcStack().executeCommand(new ChangePointsCommand(round, participant, pointsToAdd, true));

        Stage toClose = (Stage) confirmButton.getScene().getWindow();
        try {
            System.out.println(participant.getName() + " " + participant.getPointsForWeaponProperty(round.getMyWeaponCompetition().getWeaponType()).get());
        } catch (NoSuchWeaponException e) { e.printStackTrace(); }
        toClose.close();
    }

}
