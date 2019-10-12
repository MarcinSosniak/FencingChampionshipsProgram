package controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import model.Competition;
import model.Participant;
import model.enums.WeaponType;
import model.exceptions.NoSuchCompetitionException;
import model.exceptions.NoSuchWeaponException;

/**|-------------------------------------------------------------------------|
 * |         PEOPLE(resizeable)          |   Control button Pane?            |
 * | Surname Name Points Group           |                                   |
 * | (Right click to remove participant  |                                   |
 * |-------------------------------------------------------------------------|
 * |                         GROUP                                           |
 * |                                                                         |
 * | GROUP A         GROUP B .....                                           |
 * |                                                                         |
 * | ------------------------------------------------------------------------|
 * |                         FIGHT RESULTS                                   |
 * |                                                                         |
 * |                 GROUP A                                                 |
 * |    Participant1(red) x Participant2(green)                              |
 * |    color indicates who won                                              |
 * |-------------------------------------------------------------------------| */

/** People is table view in some grid pane? or boarder pane?
 * Controll button Pane is only buttons in some border Pane again
 * Groups all groubs in one competition + killers with competitors
 * Fight results is dunno as far
 * ALL OF THIS IS IN ONE TAB TAB SWITCH WHOLE CONTEXT*/




public class EliminationController {


    private Competition competition;

    @FXML
    private ObservableMap<WeaponType,ObservableList<Participant>> weaponCompetitionParticipants;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab rapierTab;
    @FXML
    private Tab sabreTab;
    @FXML
    private Tab smallSwordTab;


    public EliminationController(){

    }



    public void update() {
        rapierTab = initTab(WeaponType.RAPIER);
        sabreTab = initTab(WeaponType.SABRE);
        smallSwordTab = initTab(WeaponType.SMALL_SWORD);
        tabPane.getTabs().add(rapierTab);
        tabPane.getTabs().add(sabreTab);
        tabPane.getTabs().add(smallSwordTab);
    }

    public void setData(Competition competition){
        this.competition = competition;
        this.weaponCompetitionParticipants = FXCollections.observableHashMap();
        for(WeaponType wt: WeaponType.values()){
            try{
                this.weaponCompetitionParticipants.put(wt, FXCollections.observableArrayList(this.competition.getWeaponCompetition(wt).getParticipants()));

            } catch (NoSuchCompetitionException e){
                e.printStackTrace();
                System.out.format("No " + wt.toString() + " competitions\n");
            }
        }
        this.update();
    }

    private GridPane prepareGridPaneForTab(){
        GridPane mainTabPane = new GridPane();

        /* Rows preparation */
            RowConstraints row1 = new RowConstraints(); /* For table and button controller*/
            RowConstraints row2 = new RowConstraints(); /* For groups and fight results */
            row1.setPercentHeight(40);
            row2.setPercentHeight(60);
        mainTabPane.getRowConstraints().addAll(row1,row2);


        /* Columns preparation */
            ColumnConstraints column1 = new ColumnConstraints(); /* For Table with participants */
            ColumnConstraints column2 = new ColumnConstraints(); /* For button controller */
            column1.setPercentWidth(80);
            column2.setPercentWidth(20);
        mainTabPane.getColumnConstraints().addAll(column1,column2);

        return mainTabPane;

    }

    private GridPane prepareTableViewPane(WeaponType wt){
        GridPane tableViewGridPane = new GridPane();

        TableView tv = new TableView();
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.setItems(weaponCompetitionParticipants.get(wt));

        TableColumn<Participant,String> name = new TableColumn<Participant,String>("Name");
        TableColumn<Participant,String> surname = new TableColumn<Participant,String>("Surname");
        TableColumn<Participant,Integer> points = new TableColumn<Participant,Integer>("Points");
        TableColumn<Participant,String> group = new TableColumn<Participant,String>("Group");

        name.setCellValueFactory(x -> x.getValue().nameProperty());
        surname.setCellValueFactory(x -> x.getValue().surnameProperty());
        points.setCellValueFactory(x -> {
            try {
                return x.getValue().getPointsForWeaponProperty(wt).asObject();
            } catch (NoSuchWeaponException e) {
                e.printStackTrace();
                return new SimpleObjectProperty<>(new Integer(0));
            }
        });
        group.setCellValueFactory(x -> {
            try{
                String g = competition.getWeaponCompetition(wt).groupForParticipant(x.getValue());
                return new SimpleStringProperty(g);
            } catch (NoSuchCompetitionException e){
                e.printStackTrace();
                return new SimpleStringProperty("NoSuchCompetitionException");
            }
        });

        tv.getColumns().addAll(name,surname,group,points);

        tv.setRowFactory( row ->{
            TableRow<Participant> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked( event -> {
                if(event.getButton().equals(MouseButton.SECONDARY) && !tableRow.isEmpty()){
                    System.out.format("Right click on add injury\n");
                    Participant p = tableRow.getItem();
                    Stage childScene = ApplicationController.getApplicationController().renderAddInjury("/addInjury.fxml","Add_Injury",true,p);
                    childScene.showAndWait();
                }
            });
            return tableRow;
        });


        GridPane.setConstraints(tv,0,0);
        tableViewGridPane.getChildren().add(tv);
        return tableViewGridPane;

    }

    /* TODO: Do i need to call update after data set? */

    private GridPane prepareButtonPane(){
        GridPane paneForButtons = new GridPane();

        RowConstraints row0 = new RowConstraints(); row0.setPercentHeight(25);
        RowConstraints row1 = new RowConstraints(); row1.setPercentHeight(25);
        RowConstraints row2 = new RowConstraints(); row2.setPercentHeight(25);
        RowConstraints row3 = new RowConstraints(); row3.setPercentHeight(25);

        paneForButtons.getRowConstraints().addAll(row0,row1,row2,row3);

        Button nextRoundButton = new Button();
        nextRoundButton.setText("Next Round");
        nextRoundButton.setStyle("-fx-background-color: pink");
        nextRoundButton.setOnAction( x -> System.out.format("Implement me\n"));
        GridPane.setConstraints(nextRoundButton,0,0);

        Button competitionStatus = new Button();
        competitionStatus.setText("AddMeTextFromEnum");
        competitionStatus.setStyle("-fx-background-color: yellow");
        competitionStatus.setOnAction( x -> System.out.format("Implement me\n"));
        GridPane.setConstraints(competitionStatus,0,1);

        Button addPoints = new Button();
        addPoints.setText("Add Points");
        addPoints.setStyle("-fx-background-color: green");
        addPoints.setOnAction( x -> System.out.format("Implement me\n"));
        GridPane.setConstraints(addPoints,0,2);

        Button substractPoints = new Button();
        substractPoints.setText("SubstractPoints");
        substractPoints.setStyle("-fx-background-color: blue");
        substractPoints.setOnAction( x -> System.out.format("Implement me\n"));
        GridPane.setConstraints(substractPoints,0,3);

        GridPane.setConstraints(paneForButtons,1,0);

        paneForButtons.getChildren().addAll(nextRoundButton,competitionStatus,addPoints,substractPoints);

        return paneForButtons;
    }

    private Tab initTab( WeaponType wt){
        GridPane mainTabPane = prepareGridPaneForTab();

        /* Add tableView pane */
            GridPane tableViewPane = prepareTableViewPane(wt);

        /* Add button panel */
            GridPane buttonPane = prepareButtonPane();

        /* Add Group panel */


        /* Add Result Panel */


        /* Add to main tab pane */
        mainTabPane.getChildren().addAll(tableViewPane,buttonPane);








        Tab tabToRet = new Tab();
        tabToRet.setText(wt.toString());
        tabToRet.setContent(mainTabPane);




        return tabToRet;
    }

}
