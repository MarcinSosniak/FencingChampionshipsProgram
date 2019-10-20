package model;

import controller.ApplicationController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.config.ConfigReader;

import java.util.Scanner;

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

        try {
            ConfigReader.init("src/main/resources/cfg/default.cfg","src/main/resources/cfg/test.cfg");
            launch(args);
        }
        catch(util.HumanReadableFatalError ex)
        {
            System.out.println(ex.getMessage());
            System.out.println("press enter to exit");
            Scanner scan= new Scanner(System.in);
            scan.nextLine();
            System.exit(2);
        }
    }
}
