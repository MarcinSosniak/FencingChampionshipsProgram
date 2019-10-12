package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Competition;
import model.DataGenerator;
import model.Participant;
import model.enums.WeaponType;

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

        /** Competitors View */

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/competitorsView.fxml"));
            Parent root = loader.load();
            /** In case if the controller is needed */
            CompetitorsViewController competitorsViewController = (CompetitorsViewController) loader.getController();
            /** Generating competitiors data, later init controller with empty list */

            primaryStage.setScene(new Scene(root));
            this.currentStage = primaryStage;
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
            System.out.format("Cannot load main FXML\n");
        }

        /** Elimination Controller */
//        try{
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/elimination.fxml"));
//            Parent root = loader.load();
//            /** In case if the controller is needed */
//            Competition c = DataGenerator.generateSampleCompetition();
//            EliminationController ec = (EliminationController) loader.getController();
//            ec.setData(c);
//            /** Generating competitiors data, later init controller with empty list */
//
//            primaryStage.setScene(new Scene(root));
//            this.currentStage = primaryStage;
//            primaryStage.show();
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.format("Cannot load main FXML\n");
//        }

    }

    public Stage renderAddNewCompetitor(String source, String title , boolean fWindowModal, ObservableList<Participant> participants) {
        Stage outputStage = new Stage();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
            Scene newScene = new Scene(loader.load());
            outputStage.setScene(newScene);
            outputStage.setTitle(title);
            outputStage.initOwner(this.currentStage);
            if (fWindowModal)
                outputStage.initModality(Modality.WINDOW_MODAL);
            AddCompetitorController controller = (AddCompetitorController) loader.getController();
            controller.setData(participants);
        }catch (Exception e){
            System.out.format("Error while rendering add dialog");
            e.printStackTrace();
        }
        return outputStage;
    }


    public Stage renderEditAndSetOwner(String source, String title , boolean fWindowModal, Participant p){
        Stage outputStage = new Stage();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
            Scene newScene = new Scene(loader.load());
            outputStage.setScene(newScene);
            outputStage.setTitle(title);
            outputStage.initOwner(this.currentStage);
            if (fWindowModal)
                outputStage.initModality(Modality.WINDOW_MODAL);
            EditCompetitorController controller = (EditCompetitorController) loader.getController();
            controller.setData(p);
        }catch (Exception e){
            System.out.format("Error while rendering edit dialog");
            e.printStackTrace();
        }
            return outputStage;
    }

    public Stage renderAddInjury(String source, String title , boolean fWindowModal, Participant p){
        Stage outputStage = new Stage();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
            Scene newScene = new Scene(loader.load());
            outputStage.setScene(newScene);
            outputStage.setTitle(title);
            outputStage.initOwner(this.currentStage);
            if (fWindowModal)
                outputStage.initModality(Modality.WINDOW_MODAL);
            GeneralPopupControllerInterface controller = (GeneralPopupControllerInterface) loader.getController();
            controller.setData(p);
        }catch (Exception e){
            System.out.format("Error while rendering add injury dialog");
            e.printStackTrace();
        }
        return outputStage;
    }


}
