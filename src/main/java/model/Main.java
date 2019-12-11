package model;

import controller.ApplicationController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import model.config.ConfigReader;
import util.HumanReadableFatalError;

public class Main extends Application {
    public static Logger logger = Logger.getLogger("MyLog");
    private Stage primaryStage;
    private ApplicationController appController;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        appController = new ApplicationController(primaryStage);
        appController.initRootLayouts();
    }

    private static void setUpLoggingToFile(){
        new File("logs").mkdir();
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {
            fh = new FileHandler("logs/log1");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args)  {
        setUpLoggingToFile();
        launch(args);
    }
}