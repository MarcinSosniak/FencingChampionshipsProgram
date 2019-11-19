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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CheckPointManager;
import model.Competition;
import model.DataGenerator;
import model.KillerDrawing.RandomKillerRandomizationStrategy;
import model.Participant;
import model.config.ConfigReader;
import model.enums.WeaponType;
import util.HumanReadableFatalError;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
                loadCompetitorsView();
            }
        }
    }

    public void goNext(){
        ObservableList rapierParticipants = FXCollections.observableArrayList();
        ObservableList sabreParticipants = FXCollections.observableArrayList();
        ObservableList smallSwordParticipants = FXCollections.observableArrayList();

        try {
            ConfigReader.init("src/main/resources/cfg/default.cfg","src/main/resources/cfg/test.cfg");
        }
        catch (HumanReadableFatalError humanReadableFatalError) { humanReadableFatalError.printStackTrace(); }

        Competition.init(
                new util.Pair<ObservableList<Participant>,WeaponType>(rapierParticipants, WeaponType.RAPIER),
                new util.Pair<ObservableList<Participant>,WeaponType>(sabreParticipants, WeaponType.SABRE),
                new util.Pair<ObservableList<Participant>,WeaponType>(smallSwordParticipants, WeaponType.SMALL_SWORD),
                new RandomKillerRandomizationStrategy()
        );

        if (!newCompetitionName.getText().equals("nazwa zawodów (yyyy-mm-dd_miasto)"))
            Competition.getInstance().setCompetitionName(newCompetitionName.getText());
        loadCompetitorsView();
    }

    private void loadCompetitorsView(){
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
