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
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        appController = new ApplicationController(primaryStage);

        appController.initRootLayouts();
    }


    public static void main(String[] args)  {
        launch(args);
    }
}