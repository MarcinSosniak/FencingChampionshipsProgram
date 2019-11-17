package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

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
    public void save(){
        System.out.format("save (TODO:implement me)\n");
    }
    @FXML
    public void saveAs(){
        System.out.format("saveAs (TODO:implement me)\n");
    }
    @FXML
    public void undo(){
        System.out.format("undo (TODO:implement me)\n");
    }
    @FXML
    public void redo(){
        System.out.format("redo (TODO:implement me)\n");
    }
    @FXML
    public void exportResults(){
        System.out.format("exportResults (TODO:implement me)\n");
    }
    @FXML
    public void adminModeOn(){
        System.out.format("adminModeOn (TODO:implement me)\n");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }
}
