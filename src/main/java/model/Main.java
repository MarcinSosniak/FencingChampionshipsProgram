package model;

import com.google.gson.JsonDeserializer;
import controller.ApplicationController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Scanner;

import model.config.ConfigReader;
import util.HumanReadableFatalError;

public class Main extends Application {
    private Stage primaryStage;
    private ApplicationController appController;


    @Override
    public void start(Stage primaryStage) throws Exception{
       /* this.primaryStage = primaryStage;
        appController = new ApplicationController(primaryStage);

        appController.initRootLayouts();*/

//        Parent root = FXMLLoader.load(getClass().getResource("/competitorsView.fxml"));
//
//        primaryStage.setScene(new Scene(root));
//
//        primaryStage.show();
    }


    public static void main(String[] args)  {
        //JsonDeserializer.seraliseObjectToJson();
        String json = "\t[{\n" +
                "\t\t\"name\": \"Marcin\",\n" +
                "\t\t\"surname\": \"Kowalski\",\n" +
                "\t\t\"location\": \"KRK\",\n" +
                "\t\t\"locationGroup\": \"a\",\n" +
                "\t\t\"judgeState\": \"NON_JUDGE\",\n" +
                "\t\t\"licenceExpDate\": \"24-10-2023\"\n" +
                "\t}, {\n" +
                "\t\t\"name\": \"Marek\",\n" +
                "\t\t\"surname\": \"Nowak\",\n" +
                "\t\t\"location\": \"WAW\",\n" +
                "\t\t\"locationGroup\": \"b\",\n" +
                "\t\t\"judgeState\": \"NON_JUDGE\",\n" +
                "\t\t\"licenceExpDate\": \"13-10-2023\"\n" +
                "\t}, {\n" +
                "\t\t\"name\": \"Kamila\",\n" +
                "\t\t\"surname\": \"Mak\",\n" +
                "\t\t\"location\": \"KRK\",\n" +
                "\t\t\"locationGroup\": \"c\",\n" +
                "\t\t\"judgeState\": \"MAIN_JUDGE\",\n" +
                "\t\t\"licenceExpDate\": \"09-10-2023\"\n" +
                "\t}]";

        List<Participant> participantList =
                ObjectDeserializer.convertFromJsonArray(json, Participant.class);
        for (Participant participant: participantList){
            System.out.println(participant.toString());
        }

        /*try {
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
        }*/
    }
}
