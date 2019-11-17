package model;

import controller.ApplicationController;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Scanner;

import model.config.ConfigReader;
import util.HumanReadableFatalError;

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


    public static void main(String[] args)  {
        launch(args);
    }
}