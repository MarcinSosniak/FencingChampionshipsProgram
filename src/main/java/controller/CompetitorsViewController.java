package controller;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import model.DataGenerator;
import model.Participant;
import model.enums.JudgeState;


import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/** TODO:
 *      - date problem
 *  */
public class CompetitorsViewController implements Initializable {

    private ObservableList<Participant> participants;

    public void setParticipants(ObservableList<Participant> newParticipants){
        this.participants = newParticipants;
    }


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
    TableColumn<Participant,Boolean> fSmallSwordParticipant;
    @FXML
    TableColumn<Participant,Boolean> fSabreParticipant;
    @FXML
    TableColumn<Participant,Boolean> fRapierParticipant;
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
    public void goBack(){
        System.out.format("goBack (TODO:implement me)\n");
    }

    @FXML
    public void addNewCompetitor(){
        System.out.format("render addNewCompetitor (implemented)\n");
        Stage childScene = ApplicationController.getApplicationController().renderAddNewCompetitor("/addCompetitor.fxml","Dodaj nowego zawodnika",true,participants);
        childScene.showAndWait();
    }

    @FXML
    public void startCompetition(){
        System.out.format("startCompetiton (TODO:implement me)\n");
        System.out.format("Size: %d\n",participants.size());
    }


    private void editCompetitor(Participant toEdit){
        System.out.format("edit competitor to do implement\n");
        Stage childScene = ApplicationController.getApplicationController().renderEditAndSetOwner("/editCompetitor.fxml","Edytuj zawodnika",true,toEdit);
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

        try {
            this.setParticipants(DataGenerator.generateParticipants(1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        competitorsTable.setItems(participants);
        setRightClickOnCompetitor(competitorsTable);

        name.setCellValueFactory(dataValue -> dataValue.getValue().nameProperty());
        surname.setCellValueFactory(dataValue -> dataValue.getValue().surnameProperty());
        club.setCellValueFactory(dataValue -> dataValue.getValue().locationProperty());
        group.setCellValueFactory(dataValue -> dataValue.getValue().locationGroupProperty());
        fSmallSwordParticipant.setCellValueFactory(dataValue ->dataValue.getValue().fSmallSwordParticipantProperty());
        fSabreParticipant.setCellValueFactory(dataValue -> dataValue.getValue().fSabreParticipantProperty());
        fRapierParticipant.setCellValueFactory(dataValue -> dataValue.getValue().fRapierParticipantProperty());
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
