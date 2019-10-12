package model;

import Util.HumanReadableFatalError;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.config.ConfigReader;
import javafx.application.Application;
import javafx.stage.Stage;

import model.config.ConfigReader;

import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("inzynierka szermieka");
        primaryStage.setScene(new Scene(root, 900, 575));
        primaryStage.show();
    }


    public static void main(String[] args)  {
        try {
            ConfigReader.init("src/main/resources/cfg/default.cfg","src/main/resources/cfg/test.cfg");
            launch(args);
        }
        catch(HumanReadableFatalError ex)
        {
            System.out.println(ex.getMessage());
            System.out.println("press enter to exit");
            Scanner scan= new Scanner(System.in);
            scan.nextLine();
            System.exit(2);
        }
    }
}
