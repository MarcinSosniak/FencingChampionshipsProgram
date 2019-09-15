package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DataGenerator;

public class ApplicationController {
    private Stage primaryStage;
    private Stage currentStage;
    private static ApplicationController singletonApplicationController;
    public ApplicationController(Stage primaryStage){
        this.primaryStage = primaryStage;
        singletonApplicationController = this;
    }

    static ApplicationController getApplicationController(){
        return singletonApplicationController;
    }

    public void initRootLayouts(){
        this.primaryStage.setTitle("Inzynierka Szermierka");

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/competitorsView.fxml"));
            Parent root = loader.load();
            CompetitorsViewController competitorsViewController = (CompetitorsViewController) loader.getController();
            /** Generating competitiors data, later init controller with empty list */

            primaryStage.setScene(new Scene(root));
            this.currentStage = primaryStage;
            primaryStage.show();
        }catch (Exception e){
            //e.printStackTrace();
            System.out.format("Cannot load main FXML\n");
        }

    }

    public Stage renderStageAndSetOwner(String source, String title ,boolean fWindowModal){
        Stage inputStage = new Stage();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
            Scene newScene;
            newScene = new Scene(loader.load());
            inputStage.setScene(newScene);
            inputStage.setTitle(title);
            inputStage.initOwner(this.currentStage);
            if (fWindowModal)
                inputStage.initModality(Modality.WINDOW_MODAL);
        }catch(Exception e){
            e.printStackTrace();
        }
        return inputStage;
    }

}
