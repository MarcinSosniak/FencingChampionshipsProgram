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
    private EliminationController el;

    public AddPointsController(){}

    public void setData(Participant participant, Round round, EliminationController el){
        this.participant = participant;
        this.round = round;
        this.el = el;
    }

    public void cancelAddingPoints(){
        Stage toClose = (Stage) cancelButton.getScene().getWindow();
        toClose.close();
        System.out.format("cancelAddingPoints\n");
    }

    public void addPoints(){
        System.out.println("spinner: " + spinner.getValue());
        System.out.println(round.getMyWeaponCompetition().getWeaponType());

        round.addPointsToParticipant(participant, new RationalNumber((int)(spinner.getValue() * 10), 10));
        Stage toClose = (Stage) confirmButton.getScene().getWindow();
        try {
            System.out.println(participant.getName() + " " + participant.getPointsForWeaponProperty(round.getMyWeaponCompetition().getWeaponType()));
        } catch (NoSuchWeaponException e) { e.printStackTrace(); }
        toClose.close();
    }

}
