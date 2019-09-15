package model;

import controller.ApplicationController;
import controller.CompetitorsViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage primaryStage;
    private ApplicationController appController;


    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        appController = new ApplicationController(primaryStage);

        appController.initRootLayouts();

//        Parent root = FXMLLoader.load(getClass().getResource("/competitorsView.fxml"));
//
//        primaryStage.setScene(new Scene(root));
//
//        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
