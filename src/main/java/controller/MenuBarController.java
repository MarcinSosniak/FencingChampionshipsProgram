package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.AppMode;
import model.CheckPointManager;
import model.Competition;
import model.WeaponCompetition;
import model.enums.TriRet;
import util.Pointer;

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
    @FXML
    Text modeStatus;


    private WeaponCompetition wc;
    private Stage stage;

    public void setData(WeaponCompetition wc){
        this.wc = wc;
        modeStatus.setFill(javafx.scene.paint.Color.RED);
        modeStatus.setFont(new Font(20));
        if(AppMode.getMode().fSafe()) {
            adminMode.textProperty().setValue("Admin Mode");
            modeStatus.setText("");
        }
        else {
            adminMode.textProperty().setValue("Safe Mode");
            modeStatus.setText("  Admin Mode");
        }
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
        System.out.println(wc.getWeaponType());
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
        if (AppMode.getMode().fSafe())
        {
            Pointer<TriRet> p = new Pointer<>();
            p.set(TriRet.NO_VALUE);
            Stage stageroni = ApplicationController.getApplicationController().renderPasswordGet("/passwordGet.fxml",
                    "gimme password", true, Competition.getInstance().getPassword(),p);
            stageroni.getIcons().add(ApplicationController.image);
            stageroni.showAndWait();
            if(p.get()==TriRet.FALSE)
            {
                Alert alert= new Alert(Alert.AlertType.ERROR,"Not matching password");
                alert.show();
                return;
            }
            if(p.get()==TriRet.NO_VALUE)
                return;

            AppMode.getMode().setMode(AppMode.APP_MODE.ADMIN);
            adminMode.textProperty().setValue("Safe Mode");
            modeStatus.setText("  Admin Mode");
        }
        else {
            AppMode.getMode().setMode(AppMode.APP_MODE.SAFE);
            adminMode.textProperty().setValue("Admin");
            modeStatus.textProperty().setValue("");
        }
    }

    @FXML
    public void changePassword(){
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/passwordChange.fxml"));

        Parent root = null;
        try { root = loader.load(); }
        catch (IOException e) { e.printStackTrace(); }
        stage.setScene(new Scene(root));
        stage.show();
        ApplicationController.currentStage = stage;
    }

    public void closeChangePasswordScene(){
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }
}
