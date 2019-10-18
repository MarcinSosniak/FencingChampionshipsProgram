package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.*;
import model.enums.FightScore;
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
            RowConstraints row2 = new RowConstraints(); /* For groups  */
            RowConstraints row3 = new RowConstraints(); /* Fight results */
            row1.setPercentHeight(40);
            row2.setPercentHeight(30);
            row2.setPercentHeight(30);
        mainTabPane.getRowConstraints().addAll(row1,row2,row3);


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

        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.ALWAYS);
        tableViewGridPane.getRowConstraints().add(rc);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        tableViewGridPane.getColumnConstraints().add(cc);


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

    private GridPane prepareButtonPane(WeaponType wt){
        GridPane paneForButtons = new GridPane();

        RowConstraints row0 = new RowConstraints(); row0.setPercentHeight(25);
        RowConstraints row1 = new RowConstraints(); row1.setPercentHeight(25);
        RowConstraints row2 = new RowConstraints(); row2.setPercentHeight(25);
        RowConstraints row3 = new RowConstraints(); row3.setPercentHeight(25);

        paneForButtons.getRowConstraints().addAll(row0,row1,row2,row3);

        Button nextRoundButton = new Button();
        nextRoundButton.setText("Next Round");
        nextRoundButton.setStyle("-fx-background-color: pink ; -fx-padding: 10; -fx-alignment: CENTER;");
        nextRoundButton.setOnAction( x -> System.out.format("Implement me\n"));
        GridPane.setConstraints(nextRoundButton,0,0);

        Button competitionStatus = new Button();
        try{
            competitionStatus.setText(this.competition.getWeaponCompetition(wt).getWeaponCompetitionState().toString());
        } catch (NoSuchCompetitionException e){
            e.printStackTrace();
            competitionStatus.setText("UnknownCompetitionState");
        }
        competitionStatus.setStyle("-fx-background-color: yellow; -fx-padding: 10;");
        competitionStatus.setOnAction( x -> System.out.format("Implement me\n"));
        GridPane.setConstraints(competitionStatus,0,1);

        Button addPoints = new Button();
        addPoints.setText("Add Points");
        addPoints.setStyle("-fx-background-color: green; -fx-padding: 10;");
        addPoints.setOnAction( x -> System.out.format("Implement me\n"));
        GridPane.setConstraints(addPoints,0,2);

        Button substractPoints = new Button();
        substractPoints.setText("SubstractPoints");
        substractPoints.setStyle("-fx-background-color: blue; -fx-padding: 10;");
        substractPoints.setOnAction( x -> System.out.format("Implement me\n"));
        GridPane.setConstraints(substractPoints,0,3);

        GridPane.setConstraints(paneForButtons,1,0);

        paneForButtons.getChildren().addAll(nextRoundButton,competitionStatus,addPoints,substractPoints);

        return paneForButtons;
    }

    /* TODO: check and set title in the middle*/
    /* Lets assume that groups will be displayed (3/2) x N */
    private ScrollPane prepareCompetitionGroupPane(WeaponType wt,int columns){
        ScrollPane groupScrollPane = new ScrollPane();
        GridPane.setConstraints(groupScrollPane,0,1,columns,1);
        GridPane gridPaneForGroups = new GridPane();

        try{
             ObservableList<CompetitionGroup> cgl = this.competition.getWeaponCompetition(wt).getCompetitionGroups();
             int rows = (cgl.size() % columns == 0) ?  cgl.size()/columns + 1:  cgl.size()/columns + 2;

            /* Create rows and columns for group panel */
                /* row for title */
                RowConstraints rc = new RowConstraints();
                rc.setPercentHeight(10.0/rows);
                gridPaneForGroups.getRowConstraints().add(rc);

                 for(int i=0;i<rows;i++){
                     rc = new RowConstraints();
                     rc.setPercentHeight(100.0/rows);
                     gridPaneForGroups.getRowConstraints().add(rc);
                 }
                 for(int i =0;i<columns;i++){
                     ColumnConstraints cc = new ColumnConstraints();
                     cc.setPercentWidth(1.0/columns);
                     gridPaneForGroups.getColumnConstraints().add(cc);
                 }

             /* Add group label */
                Text text = new Text();
                text.setText("Groups");
                text.setTextAlignment(TextAlignment.CENTER);
                GridPane.setConstraints(text,0,0,2,1);


             /* Create table view for each group */
             int currentRow = 1;
             int groupsAdded = 0;
             //System.out.format("1 \n");
             for(CompetitionGroup competitionGroup: cgl){
                 System.out.format(competitionGroup.getGroupID() + "1 \n");
                 TableView tableForGroup = new TableView();
                 GridPane.setConstraints(tableForGroup,groupsAdded%columns,currentRow);
                 tableForGroup.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                 TableColumn<Participant,String> tc = new TableColumn("GROUP " + competitionGroup.getGroupID());
                 tc.setCellValueFactory( x -> new SimpleStringProperty(x.getValue().nameProperty() + " " + x.getValue().surnameProperty()));
                 tableForGroup.getColumns().addAll(tc);
                 tableForGroup.setItems(competitionGroup.getGroupParticipants());//
                 groupsAdded ++;
                 if(groupsAdded % columns == 0){
                     currentRow ++;
                 }
                 gridPaneForGroups.getChildren().add(tableForGroup);
             }
            gridPaneForGroups.getChildren().add(text);
            groupScrollPane.setContent(gridPaneForGroups);
            return groupScrollPane;
        } catch (NoSuchCompetitionException e){
            e.printStackTrace();
            System.out.format("Error while loading groups\n");
            return new ScrollPane();
        }
    }

    /* TODO: make better title*/
    private ScrollPane prepareResultPane(WeaponType wt,int columns){
        ScrollPane resultScrollPane = new ScrollPane();
        GridPane.setConstraints(resultScrollPane,0,2,2,1);

        try {
            GridPane gridPaneForFights = new GridPane();
            ObservableList<CompetitionGroup> groups = this.competition.getWeaponCompetition(wt).getCompetitionGroups();


            int rows = groups.size() % columns == 0 ? (groups.size()/columns) + 1: (groups.size()/columns + 2);

            /* Create rows and columns for result panel */
                /* row for title */
                RowConstraints rc = new RowConstraints();
                rc.setPercentHeight(10.0/rows);
                gridPaneForFights.getRowConstraints().add(rc);

                for(int i=0;i<rows;i++){
                    rc = new RowConstraints();
                    rc.setPercentHeight(100.0/rows);
                    gridPaneForFights.getRowConstraints().add(rc);
                }
                for(int i =0;i<columns;i++){
                    ColumnConstraints cc = new ColumnConstraints();
                    cc.setPercentWidth(1.0/columns);
                    gridPaneForFights.getColumnConstraints().add(cc);
                }

            /* Add group label */
                Text text = new Text();
                text.setText("Fight Results");
                text.setTextAlignment(TextAlignment.CENTER);
                GridPane.setConstraints(text,0,0,columns,1);



            for(int i=0;i<groups.size();i++){
                int currentRow = (i+1)/columns + 1;
                int currentColumn = i % columns;
                CompetitionGroup cg = groups.get(i);
                TableView tableForGroupFights = new TableView();
                GridPane.setConstraints(tableForGroupFights,currentColumn,currentRow);
                tableForGroupFights.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                tableForGroupFights.setItems(cg.getFightsList());

                TableColumn<Fight,String> firstParticipant = new TableColumn<>();
                TableColumn<Fight,String> doubleColumn = new TableColumn<>();
                TableColumn<Fight,String> secondParticipant = new TableColumn<>();

                firstParticipant.setText("First");
                doubleColumn.setText("X");
                firstParticipant.setText("Second");

                firstParticipant.setCellValueFactory( f -> new SimpleStringProperty(f.getValue().getFirstParticipant().nameProperty() + " " + f.getValue().getFirstParticipant().surnameProperty()));
                secondParticipant.setCellValueFactory( f -> new SimpleStringProperty(f.getValue().getSecondParticipant().nameProperty() + " " + f.getValue().getSecondParticipant().surnameProperty()));
                doubleColumn.setCellValueFactory( f -> new SimpleStringProperty("x"));


                firstParticipant.setCellFactory( tc -> {
                    TableCell<Fight,String> cell = new TableCell<>();
                    Fight f = (Fight) cell.getTableRow().getItem();
                    cell.setOnMouseClicked( e -> {
                        if(e.getButton().equals(MouseButton.PRIMARY) && !cell.isEmpty() ){
                            f.commandSetFightScoreDirect(FightScore.WON_FIRST);
                        }
                    });
                    return cell;
                });

                secondParticipant.setCellFactory( tc -> {
                    TableCell<Fight,String> cell = new TableCell<>();
                    Fight f = (Fight) cell.getTableRow().getItem();
                    cell.setOnMouseClicked( e -> {
                        if(e.getButton().equals(MouseButton.PRIMARY) && !cell.isEmpty() ){
                            f.commandSetFightScoreDirect(FightScore.WON_SECOND);
                        }
                    });
                    return cell;
                });

                doubleColumn.setCellFactory( tc -> {
                    TableCell<Fight,String> cell = new TableCell<>();
                    Fight f = (Fight) cell.getTableRow().getItem();
                    cell.setOnMouseClicked( e -> {
                        if(e.getButton().equals(MouseButton.PRIMARY) && !cell.isEmpty() ){
                            f.commandSetFightScoreDirect(FightScore.DOUBLE);
                        }
                    });
                    return cell;
                });

                tableForGroupFights.getColumns().addAll(firstParticipant,doubleColumn,secondParticipant);
                gridPaneForFights.getChildren().add(tableForGroupFights);
            }
            gridPaneForFights.getChildren().add(text);
            resultScrollPane.setContent(gridPaneForFights);

            return resultScrollPane;

        } catch (NoSuchCompetitionException e) {
            System.out.format("No weapon competition\n");
            e.printStackTrace();
            return new ScrollPane();
        }
    }

    private Tab initTab( WeaponType wt){
        GridPane mainTabPane = prepareGridPaneForTab();

        /* Add tableView pane */
            GridPane tableViewPane = prepareTableViewPane(wt);

        /* Add button panel */
            GridPane buttonPane = prepareButtonPane(wt);

        /* Add Group panel */
           ScrollPane groupPane = prepareCompetitionGroupPane(wt,3);

        /* Add Result Panel */
            ScrollPane resultPane = prepareResultPane(wt,3);

        /* Add to main tab pane */
            mainTabPane.getChildren().addAll(tableViewPane,buttonPane,groupPane,resultPane);



        /* Add content to tab*/
            Tab tabToRet = new Tab();
            tabToRet.setText(wt.toString());
            tabToRet.setContent(mainTabPane);


        return tabToRet;
    }

}
