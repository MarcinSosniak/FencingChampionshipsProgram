package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;
import model.config.ConfigReader;
import model.enums.JudgeState;
import model.enums.WeaponType;
import model.exceptions.NoSuchWeaponException;
import util.RationalNumber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


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
    TableColumn<Participant, RationalNumber> smallSwordPoints;
    @FXML
    TableColumn<Participant, RationalNumber> sabrePoints;
    @FXML
    TableColumn<Participant, RationalNumber> rapierPoints;

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
        if (Competition.getInstance().getWeaponCompetition(WeaponType.SMALL_SWORD).getLastRound() == null) {
            List<Participant> competitors_list = Competition.getInstance().getParticipants();
            List<Participant>  small_sword_list = new ArrayList<>();
            List<Participant>  sabre_list = new ArrayList<>();
            List<Participant>  rapier_list = new ArrayList<>();
            for (Participant part : competitors_list) {
                if(part.fSmallSwordParticipantProperty().getValue())
                    small_sword_list.add(part);
                if(part.fSabreParticipantProperty().getValue())
                    sabre_list.add(part);
                if(part.fRapierParticipantProperty().getValue())
                    rapier_list.add(part);
            }
            Competition comp = Competition.getInstance();
            comp.getWeaponCompetition(WeaponType.SMALL_SWORD).addParticipants(small_sword_list);
            comp.getWeaponCompetition(WeaponType.SABRE).addParticipants(sabre_list);
            comp.getWeaponCompetition(WeaponType.RAPIER).addParticipants(rapier_list);
        }

        for (WeaponCompetition weaponCompetition: Competition.getInstance().getWeaponCompetitions()){
            if (weaponCompetition.getLastRound() == null) weaponCompetition.startFirstRound();

        }
        try {
            String json = PersistenceManager.serializeObjectsArrayToJson(new ArrayList<Participant>(Competition.getInstance().getParticipants()));
            String fileName = ConfigReader.getInstance().getStringValue("PATHS","PARTICIPANTS_LIST","participants.json");
            try (PrintStream out = new PrintStream(new FileOutputStream(fileName))) {
                out.print(json);
            }
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

        String fileName = ConfigReader.getInstance().getStringValue("PATHS","PARTICIPANTS_LIST","participants.json");
        try(Scanner scanner = new Scanner(new File(fileName))){
            String json_arr = scanner.useDelimiter("\\A").next();
            Competition.getInstance().getParticipantsObservableList().addAll(PersistenceManager.deserializeFromJsonArray(json_arr,Participant.class,false));
        }
        catch (Exception ex)
        {
            ;
        }
        for (Participant p : Competition.getInstance().getParticipants()) { p.update(); }

        competitorsTable.setItems(Competition.getInstance().getParticipantsObservableList());
        setRightClickOnCompetitor(competitorsTable);

        name.setCellValueFactory(dataValue -> dataValue.getValue().nameProperty());
        surname.setCellValueFactory(dataValue -> dataValue.getValue().surnameProperty());
        club.setCellValueFactory(dataValue -> dataValue.getValue().locationProperty());
        group.setCellValueFactory(dataValue -> dataValue.getValue().locationGroupProperty());



        fSmallSwordParticipant.setCellValueFactory(param -> (param.getValue().fSmallSwordParticipantSProperty));
        fRapierParticipant.setCellValueFactory(param -> (param.getValue().fRapierParticipantSProperty));
        fSabreParticipant.setCellValueFactory(param -> (param.getValue().fSabreParticipantSProperty));
        refereeStatus.setCellValueFactory(dataValue -> dataValue.getValue().judgeStateProperty());


        sabrePoints.setCellValueFactory(dataValue -> dataValue.getValue().getOldSeasonPointsForWeaponProperty(WeaponType.SABRE));
        rapierPoints.setCellValueFactory(dataValue -> dataValue.getValue().getOldSeasonPointsForWeaponProperty(WeaponType.RAPIER));
        smallSwordPoints.setCellValueFactory(dataValue -> dataValue.getValue().getOldSeasonPointsForWeaponProperty(WeaponType.SMALL_SWORD));



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
                        Date currentDate = new Date();
                        if(item.compareTo(currentDate) < 0){
                            setStyle("-fx-background-color: CORAL");
                        }else{
                            setStyle("");
                        }
                    }
                }
            };
        });
    }
}