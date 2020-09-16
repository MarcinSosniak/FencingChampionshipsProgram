package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.*;
import model.KillerDrawing.RandomKillerRandomizationStrategy;
import model.config.ConfigReader;
import model.enums.WeaponType;
import util.HumanReadableFatalError;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Pattern;

public class WelcomeController implements Initializable {

    @FXML
    private TextField newCompetitionName;

    public void selectCompetition(){
        Stage stage = new Stage();
        File file = new File("saves");
        file.mkdir();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(file);
        directoryChooser.setTitle("Wybierz katalog z zawodami");
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            String[] parts = selectedDirectory.getPath().split(Pattern.quote("\\"));
            if (parts[parts.length -2].equals("saves")) {
                String targetDirectoryName = parts[parts.length - 1];
                System.out.println(targetDirectoryName);
                CheckPointManager.readFromCheckPoint("saves/"+targetDirectoryName);

                // No need to show or read particpants.
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/elimination.fxml"));
                    Parent root = loader.load();

                    EliminationController ec = (EliminationController) loader.getController();
                    ec.setData();
                    ApplicationController.primaryStage.getScene().setRoot(root);
                }
                catch (Exception ex){
                    throw new HumanReadableFatalError("Failed to load competitors view",ex);
                }
//                loadCompetitorsView();
//                CompetitorsViewController.startCompetitionStatic(getClass().getResource("/elimination.fxml"));
            }
        }
    }

    public void goNext(){
        ObservableList rapierParticipants = FXCollections.observableArrayList();
        ObservableList sabreParticipants = FXCollections.observableArrayList();
        ObservableList smallSwordParticipants = FXCollections.observableArrayList();

        try {
            ConfigReader.init("cfg/default.cfg","cfg/override.cfg");
        }
        catch (HumanReadableFatalError humanReadableFatalError) { humanReadableFatalError.printStackTrace(); }

        String password = "";
        password = ConfigReader.getInstance().getStringValue("SECURITY", "PASSWORD","admin");



        Competition.init(
                new util.Pair<ObservableList<Participant>,WeaponType>(rapierParticipants, WeaponType.RAPIER),
                new util.Pair<ObservableList<Participant>,WeaponType>(sabreParticipants, WeaponType.SABRE),
                new util.Pair<ObservableList<Participant>,WeaponType>(smallSwordParticipants, WeaponType.SMALL_SWORD),
                new RandomKillerRandomizationStrategy(), password
        );

        if (!newCompetitionName.getText().equals("nazwa zawodów (yyyy-mm-dd_miasto)"))
            Competition.getInstance().setCompetitionName(newCompetitionName.getText());

        loadCompetitorsView();
    }

    private void loadCompetitorsView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/competitorsView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        }
        catch (IOException e) { e.printStackTrace(); }

        ApplicationController.primaryStage.getScene().setRoot(root);
        ApplicationController.primaryStage.setTitle("Wybór zawodników startujących w zawodach");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

}
