package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CompetitorsViewController implements Initializable {


    @FXML
    public void goBack(){
        System.out.format("goBack (TODO:implement me)\n");
    }

    @FXML
    public void addNewCompetitor(){
        System.out.format("addNewCompetitor (implemented)\n");
        Stage childScene = ApplicationController.getApplicationController().renderStageAndSetOwner("/addCompetitor.fxml","Dodaj nowego zawodnika",true);
        childScene.showAndWait();
    }

    @FXML
    public void startCompetition(){
        System.out.format("startCompetiton (TODO:implement me)\n");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        /** Right click to columns described here
         * https://stackoverflow.com/questions/26563390/detect-doubleclick-on-row-of-tableview-javafx
         *  */
    }
}
