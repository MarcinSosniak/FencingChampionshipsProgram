package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.*;
import model.enums.FightScore;
import model.enums.WeaponType;
import model.exceptions.NoSuchWeaponException;
import util.Pointer;
import util.RationalNumber;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/** TODO: undo doesnt change color properly
 * TODO: should add function set cell colors properly :) which is invoked by command controller*/
/**
 * |-------------------------------------------------------------------------|
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
 * |-------------------------------------------------------------------------|
 */

/** People is table view in some grid pane? or boarder pane?
 * Controll button Pane is only buttons in some border Pane again
 * Groups all groubs in one competition + killers with competitors
 * Fight results is dunno as far
 * ALL OF THIS IS IN ONE TAB TAB SWITCH WHOLE CONTEXT*/


/*TODO: add color setting */

public class EliminationController implements Initializable {

    @FXML
    ObservableMap<WeaponType, ObservableList<Participant>> weaponCompetitionParticipants;
    @FXML
    TabPane tabPane;
    Tab rapierTab = null;
    Tab sabreTab = null;
    Tab smallSwordTab = null;

    private TableView rapierTableView;
    private TableView sabreTableView;
    private TableView smallSwordTableView;
    public ObservableList<TableRow> rapierRows = FXCollections.observableArrayList();
    public ObservableList<TableRow> sabreRows = FXCollections.observableArrayList();
    public ObservableList<TableRow> smallSwordRows = FXCollections.observableArrayList();

    @FXML
    MenuBarController menuBarController;

    public EliminationController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
    }


    public void update() {
        if(rapierTab != null || sabreTab != null || smallSwordTab != null){
            tabPane.getTabs().remove(0,3);
        }
        rapierTab = initTab(WeaponType.RAPIER);
        sabreTab = initTab(WeaponType.SABRE);
        smallSwordTab = initTab(WeaponType.SMALL_SWORD);

        tabPane.getTabs().addAll(rapierTab,sabreTab,smallSwordTab);
        // first value
        menuBarController.setData(Competition.getInstance().getSingleWeaponCompetition(WeaponType.SMALL_SWORD));
        // change tab handler
        tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int selectedIndex = newValue.intValue();
            System.out.println("XXXXD here: " + selectedIndex);
            if (selectedIndex == 0)
                menuBarController.setData(Competition.getInstance().getSingleWeaponCompetition(WeaponType.RAPIER));
            else if (selectedIndex == 1)
                menuBarController.setData(Competition.getInstance().getSingleWeaponCompetition(WeaponType.SABRE));
            else if (selectedIndex == 2)
                menuBarController.setData(Competition.getInstance().getSingleWeaponCompetition(WeaponType.SMALL_SWORD));
        });

    }

    public void setData() {
        this.weaponCompetitionParticipants = FXCollections.observableHashMap();
        for (WeaponType wt : WeaponType.values()) {
            try {
                this.weaponCompetitionParticipants.put(wt, Competition.getInstance().getSingleWeaponCompetition(wt).getParticipantsObservableList());

            } catch (IllegalStateException e) {
                e.printStackTrace();
                System.out.format("No " + wt.toString() + " competitions\n");
            }
        }
        this.update();
    }

    private GridPane prepareGridPaneForTab() {
        GridPane mainTabPane = new GridPane();

        /* Rows preparation */
        RowConstraints row1 = new RowConstraints(); /* For table and button controller*/
        RowConstraints row2 = new RowConstraints(); /* For groups and fight */
        row1.setPercentHeight(25);
        row2.setPercentHeight(75);
        mainTabPane.getRowConstraints().addAll(row1, row2);


        /* Columns preparation */
        ColumnConstraints column1 = new ColumnConstraints(); /* For Table with participants */
        ColumnConstraints column2 = new ColumnConstraints(); /* For button controller */
        column1.setPercentWidth(80);
        column2.setPercentWidth(20);
        mainTabPane.getColumnConstraints().addAll(column1, column2);

        return mainTabPane;
    }

    private GridPane prepareTableViewPane(WeaponType wt) {

        GridPane tableViewGridPane = new GridPane();

        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.ALWAYS);
        tableViewGridPane.getRowConstraints().add(rc);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        tableViewGridPane.getColumnConstraints().add(cc);


        TableView tv = new TableView();

        switch (wt){
            case SABRE: { sabreTableView = tv; break; }
            case SMALL_SWORD: { smallSwordTableView = tv; break; }
            case RAPIER: { rapierTableView = tv; break; }
        }

        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.setItems(weaponCompetitionParticipants.get(wt));

        TableColumn<Participant, String> name = new TableColumn<Participant, String>("Name");
        TableColumn<Participant, String> surname = new TableColumn<Participant, String>("Surname");
        TableColumn<Participant, RationalNumber> points = new TableColumn<>("Points");
        TableColumn<Participant, String> group = new TableColumn<Participant, String>("Group");

        name.setCellValueFactory(x -> x.getValue().nameProperty());
        surname.setCellValueFactory(x -> x.getValue().surnameProperty());
        points.setCellValueFactory(x -> {
            try {
//                return x.getValue().getPointsForWeaponPropertyLastRound(wt);
                return x.getValue().getPointsForWeaponProperty(wt);
            } catch (NoSuchWeaponException e) {
                e.printStackTrace();
                return null;
            }
        });
        group.setCellValueFactory(x -> {
            try {
                String g = Competition.getInstance().getSingleWeaponCompetition(wt).groupForParticipant(x.getValue());
                return new SimpleStringProperty(g);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return new SimpleStringProperty("NoSuchCompetitionException");
            }
        });

        tv.getColumns().addAll(name, surname, group, points);


        tv.setRowFactory(row -> {
            TableRow<Participant> tableRow = new TableRow<>();
            switch (wt){
                case RAPIER: { rapierRows.add(tableRow); break;}
                case SABRE:  { sabreRows.add(tableRow); break;}
                case SMALL_SWORD:  { smallSwordRows.add(tableRow); break;}
            }


            tableRow.setOnMouseClicked(event -> {
                Participant p = (Participant) tv.getSelectionModel().getSelectedItem();

                if (event.getButton().equals(MouseButton.SECONDARY) && !tableRow.isEmpty()) {
                    //tableRow.fireEvent(myEvent);
                    System.out.format("Right click on add injury " + wt + "\n");
                    Stage childScene = ApplicationController.getApplicationController().renderAddInjury("/addInjury.fxml", "Add_Injury", true, p, wt, this);
                    childScene.getIcons().add(ApplicationController.image);
                    childScene.showAndWait();

                    // usuwa po dodaniu obrazenia
                    //Competition.getInstance().getWeaponCompetition(WeaponType.RAPIER).getParticipants().remove(p);
                }
            });
            return tableRow;
        });


        GridPane.setConstraints(tv, 0, 0);
        tableViewGridPane.getChildren().add(tv);
        return tableViewGridPane;

    }

    private GridPane prepareButtonPane(WeaponType wt) {
        GridPane paneForButtons = new GridPane();

        RowConstraints row0 = new RowConstraints();
        row0.setPercentHeight(25);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(25);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(25);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(25);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100);

        paneForButtons.getRowConstraints().addAll(row0, row1, row2, row3);
        paneForButtons.getColumnConstraints().addAll(cc);

        Button nextRoundButton = new Button();
        nextRoundButton.setMaxSize(1000, 1000);
        nextRoundButton.setText("Next Round");
        //nextRoundButton.setStyle("-fx-background-color: pink ; -fx-padding: 10;");
        nextRoundButton.setOnAction(x -> {
            System.out.format("Implement by hand\n");
            WeaponCompetition wc = Competition.getInstance().getWeaponCompetition(wt);
            WeaponCompetition.RoundCreator rc = null;
            if(AppMode.getMode().fSafe()) {
                rc = wc.prepareNewRound();
            }
            else
            {
                Pointer<WeaponCompetition.RoundCreator> p = new Pointer<>();

                Stage stageroni = ApplicationController.getApplicationController().renderNextRound("/nextRound.fxml", "gimme", true, wc,p);
                stageroni.getIcons().add(ApplicationController.image);
                stageroni.showAndWait();
                if (p.get()==null)
                    return;
                rc=p.get();
            }
            if (rc.getfRoundReady()) {
                rc.startRound();
            }else {
                Stage stageroni = ApplicationController.getApplicationController().renderPlayOff("/playOff.fxml", "gimme", true, rc);
                stageroni.getIcons().add(ApplicationController.image);
                stageroni.showAndWait();
                rc.startRound();
            }
//            setData();
            /* TODO: refresh view */
            this.setData();
        });

        GridPane.setConstraints(nextRoundButton, 0, 0);

        Button competitionStatus = new Button();
        competitionStatus.setMaxSize(1000, 1000);
        try {
            competitionStatus.setText(Competition.getInstance().getSingleWeaponCompetition(wt).getWeaponCompetitionState().toString());
        } catch (IllegalStateException e) {
            e.printStackTrace();
            competitionStatus.setText("UnknownCompetitionState");
        }
        //competitionStatus.setStyle("-fx-background-color: yellow; -fx-padding: 10;");
        competitionStatus.setOnAction(x -> System.out.format("Implement me\n"));
        GridPane.setConstraints(competitionStatus, 0, 1);

        Button addPoints = new Button();
        addPoints.setMaxSize(1000, 1000);
        addPoints.setText("Add Points");
        //addPoints.setStyle("-fx-background-color: green; -fx-padding: 10;");

        addPoints.setOnAction(x -> {
            System.out.println("ON ACTION: " + wt);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addPoints.fxml"));
            Parent root = null;
            try { root = loader.load(); }
            catch (IOException ex) { System.out.println("error while loading add points");}
            Stage childStage = new Stage();
            childStage.setScene(new Scene(root));
            childStage.show();

            Participant p = null;
            switch (wt){
                case RAPIER: { p = (Participant) rapierTableView.getSelectionModel().getSelectedItem(); break; }
                case SMALL_SWORD: { p = (Participant) smallSwordTableView.getSelectionModel().getSelectedItem(); break; }
                case SABRE: { p = (Participant) sabreTableView.getSelectionModel().getSelectedItem(); break; }
            }
            AddPointsController addPointsController = (AddPointsController) loader.getController();
            addPointsController.setData(p, Competition.getInstance().getWeaponCompetition(wt).getLastRound());
        });
        GridPane.setConstraints(addPoints, 0, 2);

        Button substractPoints = new Button();
        substractPoints.setMaxSize(1000, 1000);
        substractPoints.setText("SubstractPoints");
        //substractPoints.setStyle("-fx-background-color: grey; -fx-padding: 10;");

        substractPoints.setOnAction(x -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/subtractPoints.fxml"));
            Parent root = null;
            try { root = loader.load(); }
            catch (IOException ex) { System.out.println("error while subtracting points");}
            Stage childStage = new Stage();
            childStage.setScene(new Scene(root));
            childStage.show();

            Participant p = null;
            switch (wt){
                case RAPIER: {p = (Participant) rapierTableView.getSelectionModel().getSelectedItem(); break; }
                case SMALL_SWORD: { p = (Participant) smallSwordTableView.getSelectionModel().getSelectedItem(); break; }
                case SABRE: { p = (Participant) sabreTableView.getSelectionModel().getSelectedItem(); break; }
            }
            SubtractPointsController subtractPointsController = (SubtractPointsController) loader.getController();
            subtractPointsController.setData(p, Competition.getInstance().getWeaponCompetition(wt).getLastRound());
        });
        GridPane.setConstraints(substractPoints, 0, 3);

        GridPane.setConstraints(paneForButtons, 1, 0);

        paneForButtons.getChildren().addAll(nextRoundButton, competitionStatus, addPoints, substractPoints);

        return paneForButtons;
    }

    /* TODO: check and set title in the middle*/
    /* Lets assume that groups will be displayed (3/2) x N */
    private ScrollPane prepareCompetitionGroupPane(WeaponType wt, int columns) {
        ScrollPane scrollPaneForVBOX = new ScrollPane();
        scrollPaneForVBOX.fitToWidthProperty();
        scrollPaneForVBOX.setFitToWidth(true);
        // VBox vBoxPane = new VBox();
        //GridPane.setConstraints(scrollPaneForVBOX,0,1,columns,1);
        GridPane gridPaneForGroups = new GridPane();

        try {
            Round lastRound = Competition.getInstance().getSingleWeaponCompetition(wt).getLastRound();

            ObservableList<CompetitionGroup> cgl = Competition.getInstance().getSingleWeaponCompetition(wt).getLastRound().getGroups();
            int rows = (cgl.size() % columns == 0) ? cgl.size() / columns : cgl.size() / columns + 1;

            /* Create rows and columns for group panel */
            /* row for title */
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(10.0 / rows);
            gridPaneForGroups.getRowConstraints().add(rc);

            for (int i = 0; i < rows; i++) {
                rc = new RowConstraints();
                rc.setPercentHeight(100.0 / rows);
                gridPaneForGroups.getRowConstraints().add(rc);
            }
            for (int i = 0; i < columns; i++) {
                ColumnConstraints cc = new ColumnConstraints();
                cc.setPercentWidth(100.0 / columns);
                cc.setHgrow(Priority.ALWAYS);
                gridPaneForGroups.getColumnConstraints().add(cc);
            }

            /* Add group label */
            Label text = new Label("Groups");
            text.setTextAlignment(TextAlignment.CENTER);
            text.setStyle("-fx-alignment: CENTER;");
            text.setFont(new Font(20));
            GridPane.setConstraints(text, 0, 0, 3, 1);
            //GridPane.setFillWidth(text,true);
            GridPane.setHalignment(text, HPos.CENTER);


            /* Create table view for each group */
            int currentRow = 1;
            int groupsAdded = 0;
            //System.out.format("1 \n");
            for (CompetitionGroup competitionGroup : cgl) {
                System.out.format(competitionGroup.getGroupID() + "1 \n");
                TableView tableForGroup = new TableView();
                GridPane.setConstraints(tableForGroup, groupsAdded % columns, currentRow);
                tableForGroup.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                TableColumn<Participant, String> tc = new TableColumn("GROUP " + competitionGroup.getGroupID());
                tc.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().nameProperty().getValue() + " " + x.getValue().surnameProperty().getValue()));
                tableForGroup.getColumns().addAll(tc);
                tableForGroup.setItems(competitionGroup.getGroupParticipants());//
                groupsAdded++;
                if (groupsAdded % columns == 0) {
                    currentRow++;
                }
                gridPaneForGroups.getChildren().add(tableForGroup);
            }
            gridPaneForGroups.getChildren().add(text);
            // vBoxPane.getChildren().add(gridPaneForGroups);
            scrollPaneForVBOX.setContent(gridPaneForGroups);
            return scrollPaneForVBOX;

        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.format("Error while loading groups\n");
            return new ScrollPane();
        }
    }

    /* TODO: make better title*/
    private ScrollPane prepareResultPane(WeaponType wt, int columns) {
        ScrollPane scrollPaneForVBOX = new ScrollPane();
        scrollPaneForVBOX.fitToWidthProperty();
        scrollPaneForVBOX.setFitToWidth(true);
        GridPane.setConstraints(scrollPaneForVBOX, 0, 2, 2, 1);

        try {
            GridPane gridPaneForFights = new GridPane();
            Round lastRound = Competition.getInstance().getSingleWeaponCompetition(wt).getLastRound();

            ObservableList<CompetitionGroup> groups = lastRound.getGroups();

            int rows = groups.size() % columns == 0 ? (groups.size() / columns) : (groups.size() / columns + 1);

            /* Create rows and columns for result panel */
            /* row for title */
            RowConstraints rc = new RowConstraints();
            //rc.setVgrow(Priority.ALWAYS);
            rc.setPercentHeight(10.0 / rows);
            gridPaneForFights.getRowConstraints().add(rc);

            for (int i = 0; i < rows; i++) {
                rc = new RowConstraints();
                rc.setVgrow(Priority.ALWAYS);
                rc.setPercentHeight(100.0 / rows);
                gridPaneForFights.getRowConstraints().add(rc);
            }
            for (int i = 0; i < columns; i++) {
                ColumnConstraints cc = new ColumnConstraints();
                cc.setHgrow(Priority.ALWAYS);
                cc.setPercentWidth(100.0 / columns);
                gridPaneForFights.getColumnConstraints().add(cc);
            }

            /* Add result label */
            Label text = new Label("Fight Results");
            text.setTextAlignment(TextAlignment.CENTER);
            text.setStyle("-fx-alignment: CENTER;");
            text.setFont(new Font(20));
            GridPane.setConstraints(text, 0, 0, columns, 1);
            GridPane.setHalignment(text, HPos.CENTER);
            GridPane.setFillHeight(text, true);

            for (int i = 0; i < groups.size(); i++) {
                int currentRow = i/ columns + 1;
                int currentColumn = i % columns;
                CompetitionGroup cg = groups.get(i);

                TableView tableForGroupFights = new TableView();

                tableForGroupFights.setPadding(new Insets(5, 5, 5, 5));
                GridPane.setConstraints(tableForGroupFights, currentColumn, currentRow);
                tableForGroupFights.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                tableForGroupFights.setItems(FXCollections.observableList(cg.getFightsList()));

                TableColumn<Fight, String> firstParticipant = new TableColumn<>();
                TableColumn<Fight, String> doubleColumn = new TableColumn<>();
                TableColumn<Fight, String> secondParticipant = new TableColumn<>();

                doubleColumn.setMinWidth(19);
                doubleColumn.setMaxWidth(20);

                doubleColumn.setStyle("-fx-alignment: CENTER;");
                firstParticipant.setStyle("-fx-alignment: CENTER;");
                secondParticipant.setStyle("-fx-alignment: CENTER;");

                /*TODO: Remove header from table */
                firstParticipant.setText("First");
                doubleColumn.setText("X");
                secondParticipant.setText("Second");

                firstParticipant.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getFirstParticipant().nameProperty().getValue() + " " + f.getValue().getFirstParticipant().surnameProperty().getValue()));
                secondParticipant.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getSecondParticipant().nameProperty().getValue() + " " + f.getValue().getSecondParticipant().surnameProperty().getValue()));
                doubleColumn.setCellValueFactory(f -> new SimpleStringProperty("x"));

                firstParticipant.setCellFactory(tc -> {
                    TableCell<Fight, String> cell = new TableCell<Fight, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(empty ? null : item);
                            if(!isEmpty()){
                                /* Person auxPerson = getTableView().getItems().get(getIndex()); */
                                Fight fight = getTableView().getItems().get(getIndex());
                                if (fight.getScore().equals(FightScore.WON_FIRST)) {
                                    setStyle("-fx-alignment: CENTER; -fx-background-color: GREEN;");
                                } else if (fight.getScore().equals(FightScore.NULL_STATE)) {
                                    setStyle("-fx-alignment: CENTER; -fx-background-color: TRANSPARENT;");
                                } else {
                                    setStyle("-fx-alignment: CENTER; -fx-background-color: RED;");
                                }
                            }
                        }
                    };
                    cell.setOnMouseClicked(e -> {
                        if (e.getButton().equals(MouseButton.PRIMARY) && !cell.isEmpty()) {
                            Fight f = (Fight) cell.getTableRow().getItem();
                            if (f.getScore().equals(FightScore.WON_FIRST)) {
                                cell.getTableRow().getChildrenUnmodifiable().get(1).setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                                cell.getTableRow().getChildrenUnmodifiable().get(2).setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                                cell.setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                                f.commandSetFightScoreDirect(FightScore.NULL_STATE);
                            } else {
                                cell.getTableRow().getChildrenUnmodifiable().get(1).setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                                cell.getTableRow().getChildrenUnmodifiable().get(2).setStyle("-fx-alignment: CENTER; -fx-background-color: red;");
                                cell.setStyle("-fx-alignment: CENTER; -fx-background-color: green;");
                                f.commandSetFightScoreDirect(FightScore.WON_FIRST);
                            }

                        }
                    });
                    return cell;
                });

                secondParticipant.setCellFactory(tc -> {
                    TableCell<Fight, String> cell = new TableCell<Fight, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(empty ? null : item);
                            if(!isEmpty()){
                                /* Person auxPerson = getTableView().getItems().get(getIndex()); */
                                Fight fight = getTableView().getItems().get(getIndex());
                                if (fight.getScore().equals(FightScore.WON_SECOND)) {
                                    setStyle("-fx-alignment: CENTER; -fx-background-color: GREEN;");
                                } else if (fight.getScore().equals(FightScore.NULL_STATE)) {
                                    setStyle("-fx-alignment: CENTER; -fx-background-color: TRANSPARENT;");
                                } else {
                                    setStyle("-fx-alignment: CENTER; -fx-background-color: RED;");
                                }
                            } else {
                                setStyle("-fx-alignment: CENTER; -fx-background-color: TRANSPARENT;");
                            }
                        }
                    };
                    cell.setOnMouseClicked(e -> {
                        if (e.getButton().equals(MouseButton.PRIMARY) && !cell.isEmpty()) {
                            Fight f = (Fight) cell.getTableRow().getItem();
                            if (f.getScore().equals(FightScore.WON_SECOND)) {
                                cell.getTableRow().getChildrenUnmodifiable().get(0).setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                                cell.getTableRow().getChildrenUnmodifiable().get(1).setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                                cell.setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                                f.commandSetFightScoreDirect(FightScore.NULL_STATE);
                            } else {
                                cell.getTableRow().getChildrenUnmodifiable().get(0).setStyle("-fx-alignment: CENTER; -fx-background-color: red;");
                                cell.getTableRow().getChildrenUnmodifiable().get(1).setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                                cell.setStyle("-fx-alignment: CENTER; -fx-background-color: green;");
                                f.commandSetFightScoreDirect(FightScore.WON_SECOND);
                            }
                        }
                    });
                    return cell;
                });

                doubleColumn.setCellFactory(tc -> {
                    TableCell<Fight, String> cell = new TableCell<Fight, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(empty ? null : item);
                            if(!isEmpty()){
                                setStyle("-fx-alignment: CENTER; -fx-background-color: TRANSPARENT;");
                            } else {
                                setStyle("-fx-alignment: CENTER; -fx-background-color: TRANSPARENT;");
                            }
                        }
                    };
                    cell.setOnMouseClicked(e -> {
                        if (e.getButton().equals(MouseButton.PRIMARY) && !cell.isEmpty()) {
                            Fight f = (Fight) cell.getTableRow().getItem();
                            if (f.getScore().equals(FightScore.DOUBLE)) {
                                cell.getTableRow().getChildrenUnmodifiable().get(0).setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                                cell.getTableRow().getChildrenUnmodifiable().get(2).setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                                cell.setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                                f.commandSetFightScoreDirect(FightScore.NULL_STATE);
                            } else {
                                cell.getTableRow().getChildrenUnmodifiable().get(0).setStyle("-fx-alignment: CENTER; -fx-background-color: red;");
                                cell.getTableRow().getChildrenUnmodifiable().get(2).setStyle("-fx-alignment: CENTER; -fx-background-color: red;");
                                cell.setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                                f.commandSetFightScoreDirect(FightScore.DOUBLE);
                            }
                        }
                    });
                    return cell;
                });

                tableForGroupFights.getColumns().addAll(firstParticipant, doubleColumn, secondParticipant);
                gridPaneForFights.getChildren().add(tableForGroupFights);
            }
            gridPaneForFights.getChildren().add(text);

            scrollPaneForVBOX.setContent(gridPaneForFights);
            return scrollPaneForVBOX;

        } catch (IllegalStateException e) {
            System.out.format("No weapon competition\n");
            e.printStackTrace();
            return new ScrollPane();
        }
    }

    Tab initTab(WeaponType wt) {
        GridPane mainTabPane = prepareGridPaneForTab();
        VBox v = new VBox();
        GridPane.setConstraints(v, 0, 1, 2, 1);

        /* Add tableView pane */
        GridPane tableViewPane = prepareTableViewPane(wt);

//        tableViewPane.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent e) {
//                System.out.println("kot");
//            }
//        });

        /* Add button panel */
        GridPane buttonPane = prepareButtonPane(wt);

        /* Add Group panel */
        ScrollPane groupPane = prepareCompetitionGroupPane(wt, 3);

        /* Add Result Panel */
        ScrollPane resultPane = prepareResultPane(wt, 3);

        /* Add to main tab pane */

        v.getChildren().addAll(groupPane, resultPane);
        mainTabPane.getChildren().addAll(tableViewPane, buttonPane, v);


        /* Add content to tab*/
        Tab tabToRet = new Tab();
        tabToRet.setClosable(false);
        tabToRet.setText(wt.toString());
        tabToRet.setContent(mainTabPane);

        return tabToRet;
    }

}