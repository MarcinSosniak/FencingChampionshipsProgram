package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CheckPointManager;
import model.Competition;
import model.WeaponCompetition;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuBarController implements Initializable {

    @FXML
    Button save;
    @FXML
    Button saveAs;
    @FXML
    Button redo;
    @FXML
    Button undo;
    @FXML
    Button exportResults;
    @FXML
    Button adminMode;
    @FXML
    Button changePassword;

    private WeaponCompetition wc;

    public void setData(WeaponCompetition wc){
        this.wc = wc;
    }

    @FXML
    public void save(){
        CheckPointManager.createCheckPoint();
    }
    @FXML
    public void saveAs(){
        Stage stage = new Stage();
        File file = new File("saves");
        file.mkdir();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(file);
        fileChooser.setTitle("Zapisywanie jako");
        File selectedPlace = fileChooser.showSaveDialog(stage);
        if (selectedPlace != null)
            CheckPointManager.createCheckPoint(selectedPlace.getPath());
    }

    @FXML
    public void undo(){
        System.out.format("undo\n");
        wc.getcStack().undo();
    }
    @FXML
    public void redo(){
        System.out.format("redo\n");
        wc.getcStack().redo();
    }
    @FXML
    public void exportResults(){
        System.out.format("exportResults (TODO:implement me)\n");
    }
    @FXML
    public void adminModeOn(){
        System.out.format("adminModeOn (TODO:implement me)\n");
    }

    @FXML
    public void changePassword(){
        System.out.format("changePassword (TODO:implement me)\n");
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/passwordChange.fxml"));

        Parent root = null;
        try { root = loader.load(); }
        catch (IOException e) { e.printStackTrace(); }
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }
}
