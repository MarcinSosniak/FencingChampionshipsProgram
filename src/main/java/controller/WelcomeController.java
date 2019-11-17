package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Competition;
import model.DataGenerator;
import model.KillerDrawing.RandomKillerRandomizationStrategy;
import model.Participant;
import model.enums.WeaponType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    @FXML
    private TextField newCompetitionName;

    public void selectCompetition(){
        System.out.format("selectCompetition (TODO:implement me)\n");
    }

    public void goNext(){
        ObservableList rapierParticipants = FXCollections.observableArrayList();
        ObservableList sabreParticipants = FXCollections.observableArrayList();
        ObservableList smallSwordParticipants = FXCollections.observableArrayList();

        Competition.init(
                new util.Pair<ObservableList<Participant>,WeaponType>(rapierParticipants, WeaponType.RAPIER),
                new util.Pair<ObservableList<Participant>,WeaponType>(sabreParticipants, WeaponType.SABRE),
                new util.Pair<ObservableList<Participant>,WeaponType>(smallSwordParticipants, WeaponType.SMALL_SWORD),
                new RandomKillerRandomizationStrategy()
        );

        if (!newCompetitionName.getText().equals("nazwa zawodów (yyyy-mm-dd_miasto)"))
            Competition.getInstance().setCompetitionName(newCompetitionName.getText());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/competitorsView.fxml"));
        new Button();
        Parent root = null;
        try {
            root = loader.load();
        }
        catch (IOException e) { e.printStackTrace(); }

        ApplicationController.primaryStage.setScene(new Scene(root));
        ApplicationController.primaryStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

}
