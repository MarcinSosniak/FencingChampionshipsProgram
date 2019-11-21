package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import model.config.ConfigReader;
import model.enums.JudgeState;
import model.enums.WeaponType;
import model.exceptions.NoSuchWeaponException;
import util.RationalNumber;

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
    TableColumn<Participant, String> smallSwordPoints;
    @FXML
    TableColumn<Participant, String> sabrePoints;
    @FXML
    TableColumn<Participant, String> rapierPoints;

    @FXML
    TableColumn<Participant, Date> licence;
    /**E: Table view fields */

    @FXML
    MenuBarController menuBarController;

    static String tick = "\u2713";
    static String cross = "\u2718";


    @FXML
    public void goBack(){
        Competition.nullInstance();
        ConfigReader.nullInstance();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/welcomeScreen.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) { e.printStackTrace(); }
        ApplicationController.primaryStage.getScene().setRoot(root);
    }

    @FXML
    public void addNewCompetitor(){
        System.out.format("render addNewCompetitor (implemented)\n");
        Stage childScene = ApplicationController.getApplicationController().renderAddNewCompetitor("/addCompetitor.fxml","Dodaj nowego zawodnika",true);
        childScene.getIcons().add(ApplicationController.image);
        childScene.showAndWait();
    }

    @FXML
    public void startCompetition() {
        for (WeaponCompetition weaponCompetition: Competition.getInstance().getWeaponCompetitions()){
            // start first round
            if (weaponCompetition.getLastRound() == null) weaponCompetition.startFirstRound(5);

        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/elimination.fxml"));
            Parent root = loader.load();

            EliminationController ec = (EliminationController) loader.getController();
            ec.setData();
            ApplicationController.primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.format("Cannot load main FXML\n");
        }
    }


    private void editCompetitor(Participant toEdit){
        System.out.format("edit competitor to do implement\n");
        Stage childScene = ApplicationController.getApplicationController().renderEditAndSetOwner("/editCompetitor.fxml","Edytuj zawodnika",true, toEdit);
        childScene.getIcons().add(ApplicationController.image);
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
        menuBarController.changePassword.setDisable(true);


        competitorsTable.setItems(Competition.getInstance().getParticipantsObservableList());
        setRightClickOnCompetitor(competitorsTable);

        name.setCellValueFactory(dataValue -> dataValue.getValue().nameProperty());
        surname.setCellValueFactory(dataValue -> dataValue.getValue().surnameProperty());
        club.setCellValueFactory(dataValue -> dataValue.getValue().locationProperty());
        group.setCellValueFactory(dataValue -> dataValue.getValue().locationGroupProperty());

        fSmallSwordParticipant.setCellValueFactory(param -> (param.getValue().fSmallSwordParticipantProperty().get() ? new SimpleObjectProperty<>(tick) : new SimpleObjectProperty<>(cross)));
        fRapierParticipant.setCellValueFactory(param -> (param.getValue().fRapierParticipantProperty().get() ? new SimpleObjectProperty<>(tick) : new SimpleObjectProperty<>(cross)));
        fSabreParticipant.setCellValueFactory(param -> (param.getValue().fSabreParticipantProperty().get() ? new SimpleObjectProperty<>(tick) : new SimpleObjectProperty<>(cross)));
        refereeStatus.setCellValueFactory(dataValue -> dataValue.getValue().judgeStateProperty());

        sabrePoints.setCellValueFactory(dataValue -> {
            try {
                return new SimpleStringProperty(dataValue.getValue().getPointsForWeaponProperty(WeaponType.SABRE).toString());
            } catch (NoSuchWeaponException e) {
                return new SimpleStringProperty(cross);
            }
        });
        rapierPoints.setCellValueFactory(dataValue -> {
            try {
                return new SimpleStringProperty(dataValue.getValue().getPointsForWeaponProperty(WeaponType.RAPIER).toString());
            } catch (NoSuchWeaponException e) {
                return new SimpleStringProperty(cross);
            }
        });
        smallSwordPoints.setCellValueFactory(dataValue -> {
            try {
                return new SimpleStringProperty(dataValue.getValue().getPointsForWeaponProperty(WeaponType.SMALL_SWORD).toString());
            } catch (NoSuchWeaponException e) {
                return new SimpleStringProperty(cross);
            }
        });

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