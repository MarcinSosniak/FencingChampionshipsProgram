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
import model.*;
import model.command.AddRoundCommand;
import model.command.Command;
import model.enums.TriRet;
import util.Pointer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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


    WeaponCompetition wc;
    private Stage stage;
    private EliminationController el;

    public void setData(WeaponCompetition wc, EliminationController el){
        this.wc = wc;
        this.el = el;
        System.out.println("in set data: " + wc.getWeaponType());

        modeStatus.setFill(javafx.scene.paint.Color.RED);
        modeStatus.setFont(new Font(18));
        if(AppMode.getMode().fSafe()) {
            adminMode.textProperty().setValue("Tryb administratora");
            modeStatus.setText("");
        }
        else {
            adminMode.textProperty().setValue("Tryb bezpieczny");
            modeStatus.setText("   Tryb administratora");
        }
        exportResults.setDisable(true);
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
        if (wc.getcStack().getCommandStack().size() > 1){
            List<Command> commands = wc.getcStack().getCommandStack();
            Command lastCommand = commands.get(commands.size() - 1);

            wc.getcStack().undo();
            if (lastCommand.getClass().equals(AddRoundCommand.class))
                el.setData();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nie można cofnąć");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(ApplicationController.image);
            alert.show();
        }
    }


    @FXML
    public void redo(){
        System.out.format("redo\n");
        if (wc.getcStack().getUndoStack().size() > 0){
            List<Command> undoCommands = wc.getcStack().getUndoStack();
            Command lastUndoCommand = undoCommands.get(undoCommands.size() - 1);
            wc.getcStack().redo();

            if (lastUndoCommand.getClass().equals(AddRoundCommand.class))
                el.setData();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nie można powtórzyć");
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(ApplicationController.image);
            alert.show();
        }
    }

    @FXML
    public void exportResults(){
        System.out.format("exportResults");
        ResultExporter.exportResults();
    }
    @FXML
    public void adminModeOn(){
        if (AppMode.getMode().fSafe())
        {
            Pointer<TriRet> p = new Pointer<>();
            p.set(TriRet.NO_VALUE);
            Stage stageroni = ApplicationController.getApplicationController().renderPasswordGet("/passwordGet.fxml",
                    "Podaj hasło", true, Competition.getInstance().getPassword(),p);
            stageroni.getIcons().add(ApplicationController.image);
            stageroni.showAndWait();
            if(p.get()==TriRet.FALSE)
            {
                Alert alert= new Alert(Alert.AlertType.ERROR,"Błędne hasło");
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(ApplicationController.image);
                alert.show();
                return;
            }
            if(p.get()==TriRet.NO_VALUE)
                return;

            AppMode.getMode().setMode(AppMode.APP_MODE.ADMIN);
            adminMode.textProperty().setValue("Tryb bezpieczny");
            modeStatus.setText("   Tryb administratora");
        }
        else {
            AppMode.getMode().setMode(AppMode.APP_MODE.SAFE);
            adminMode.textProperty().setValue("Tryb administratora");
            modeStatus.textProperty().setValue("");
        }
    }

    @FXML
    public void changePassword(){
        stage = new Stage();
        stage.setTitle("Zmień hasło");
        stage.getIcons().add(ApplicationController.image);
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

    public void setDisableExport(boolean fDisable)
    {
        exportResults.setDisable(fDisable);
    }
}

