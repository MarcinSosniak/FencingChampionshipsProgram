package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import model.Competition;
import model.DataGenerator;
import model.Participant;
import model.WeaponCompetition;
import model.enums.JudgeState;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


public class CompetitorsViewController implements Initializable {

    /**S: Table view fields */
    @FXML
    TableView<Participant> competitorsTable;

    @FXML
    TableColumn<Participant,String> name;
    @FXML
    TableColumn<Participant,String> surname;
    @FXML
    TableColumn<Participant,String> club;
    @FXML
    TableColumn<Participant,String> group;
    @FXML
    TableColumn<Participant, String> fSmallSwordParticipant;
    @FXML
    TableColumn<Participant,String> fSabreParticipant;
    @FXML
    TableColumn<Participant,String> fRapierParticipant;
    @FXML
    TableColumn<Participant, JudgeState> refereeStatus;

    @FXML
    TableColumn<Participant, BigDecimal> smallSwordPoints;
    @FXML
    TableColumn<Participant, BigDecimal> sabrePoints;
    @FXML
    TableColumn<Participant, BigDecimal> rapierPoints;

    @FXML
    TableColumn<Participant, Date> licence;
    /**E: Table view fields */

    @FXML
    MenuBarController menuBarController;


    @FXML
    public void goBack(){
        Competition.nullInstance();
        ApplicationController.getApplicationController().initRootLayouts();
    }

    @FXML
    public void addNewCompetitor(){
        System.out.format("render addNewCompetitor (implemented)\n");
        Stage childScene = ApplicationController.getApplicationController().renderAddNewCompetitor("/addCompetitor.fxml","Dodaj nowego zawodnika",true);
        childScene.showAndWait();
    }

    @FXML
    public void startCompetition() {
        for (WeaponCompetition weaponCompetition: Competition.getInstance().getWeaponCompetitions()){
            weaponCompetition.getParticipantsObservableList().
                    addAll(DataGenerator.generateWeaponParticipants(weaponCompetition.getWeaponType(), 7));
            weaponCompetition.startFirstRound(5);

        }
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/elimination.fxml"));
            Parent root = loader.load();

            EliminationController ec = (EliminationController) loader.getController();
            ec.setData();
            ApplicationController.primaryStage.setScene(new Scene(root));
            ApplicationController.primaryStage.show();

        }catch (Exception e) {
            e.printStackTrace();
            System.out.format("Cannot load main FXML\n");
        }
//        try{
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/participantView.fxml"));
//            Parent root = loader.load();
//            ParticipantViewController controller = (ParticipantViewController) loader.getController();
//            controller.setData(Competition.getInstance().getSingleWeaponCompetition(WeaponType.SABRE).getLastRound());
//            ApplicationController.primaryStage.setScene(new Scene(root));
//            ApplicationController.primaryStage.show();
//        }catch (Exception e){
//            e.printStackTrace();
//            System.out.format("Cannot load main FXML\n");
//        }
    }


    private void editCompetitor(Participant toEdit){
        System.out.format("edit competitor to do implement\n");
        Stage childScene = ApplicationController.getApplicationController().renderEditAndSetOwner("/editCompetitor.fxml","Edytuj zawodnika",true, toEdit);
        childScene.showAndWait();
    }

    /** Right click to columns described here
     * https://stackoverflow.com/questions/26563390/detect-doubleclick-on-row-of-tableview-javafx */
    private void setRightClickOnCompetitor(TableView<Participant> competitorsTable){
        competitorsTable.setRowFactory( x -> {
            TableRow<Participant> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked( e -> {
                if(e.getButton().equals(MouseButton.SECONDARY) && !tableRow.isEmpty()){
                    Participant p = tableRow.getItem();
                    editCompetitor(p);
                    System.out.format("Right click on %s\n",p.getName());
                }
            });
            return tableRow;
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        System.out.println("in initialize competitorsViewController");
        menuBarController.redo.setDisable(true);
        menuBarController.undo.setDisable(true);
        menuBarController.save.setDisable(true);
        menuBarController.saveAs.setDisable(true);
        menuBarController.exportResults.setDisable(true);
        menuBarController.adminMode.setDisable(true);

        competitorsTable.setItems(Competition.getInstance().getParticipantsObservableList());
        setRightClickOnCompetitor(competitorsTable);

        name.setCellValueFactory(dataValue -> dataValue.getValue().nameProperty());
        surname.setCellValueFactory(dataValue -> dataValue.getValue().surnameProperty());
        club.setCellValueFactory(dataValue -> dataValue.getValue().locationProperty());
        group.setCellValueFactory(dataValue -> dataValue.getValue().locationGroupProperty());

        fSmallSwordParticipant.setCellValueFactory(param -> (param.getValue().fSmallSwordParticipantProperty().get() ? new SimpleObjectProperty<>("\u2713") : new SimpleObjectProperty<>("\u2718")));
        fRapierParticipant.setCellValueFactory(param -> (param.getValue().fRapierParticipantProperty().get() ? new SimpleObjectProperty<>("\u2713") : new SimpleObjectProperty<>("\u2718")));
        fSabreParticipant.setCellValueFactory(param -> (param.getValue().fSabreParticipantProperty().get() ? new SimpleObjectProperty<>("\u2713") : new SimpleObjectProperty<>("\u2718")));
        refereeStatus.setCellValueFactory(dataValue -> dataValue.getValue().judgeStateProperty());

        /*TODO: How to display points properly?? */
        //smallSwordPoints.setCellValueFactory( x ->  BooleanProperty(true));
        //sabrePoints.setCellValueFactory(dataValue -> dataValue.getValue().);
        //rapierPoints.setCellValueFactory(dataValue -> dataValue.getValue().);

        licence.setCellValueFactory(dataValue -> dataValue.getValue().licenseExpDateProperty());
        licence.setCellFactory(column -> {
            return new TableCell<Participant, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format date.
                        setText(new SimpleDateFormat("dd-MM-yyyy").format(item));
                    }
                }
            };
        });
    }
}