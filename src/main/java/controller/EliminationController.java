package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import model.enums.FightScore;
import model.enums.WeaponType;
import model.exceptions.NoSuchWeaponException;
import util.Pointer;
import util.RationalNumber;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
/** TODO: undo doesn't change color properly
 * TODO: should add function set cell colors properly :) which is invoked by command controller */
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
    /** For final results required */
    private Boolean fRapierCompetitionFinals = false;
    private Boolean fSabreCompetitionFinals = false;
    private Boolean fSmallswordCompetitionFinals = false;
    private List<Button> calculateResultsButtons = new ArrayList<>();
    Tab finalResultTab = null;

    private TableView rapierTableView;
    private TableView sabreTableView;
    private TableView smallSwordTableView;
    public ObservableList<TableRow> rapierRows = FXCollections.observableArrayList();
    public ObservableList<TableRow> sabreRows = FXCollections.observableArrayList();
    public ObservableList<TableRow> smallSwordRows = FXCollections.observableArrayList();

    private static HashMap<Fight, TableCell<Fight, String>> prtipantHashMap = new HashMap<>();
    private static ParticipantViewController participantViewController;


    @FXML
    MenuBarController menuBarController;

    public EliminationController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try{
            fSabreCompetitionFinals = Competition.getInstance().getWeaponCompetition(WeaponType.SABRE).getLastRound().getfFinal();
            fSmallswordCompetitionFinals = Competition.getInstance().getWeaponCompetition(WeaponType.SMALL_SWORD).getLastRound().getfFinal();
            fRapierCompetitionFinals = Competition.getInstance().getWeaponCompetition(WeaponType.RAPIER).getLastRound().getfFinal();
        }catch (Exception e){
            fSabreCompetitionFinals = false;
            fSmallswordCompetitionFinals = false;
            fRapierCompetitionFinals = false;
        }
        /* Rendering ParticipantView */
        Stage outputStage = new Stage();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/participantView.fxml"));
            Scene newScene = new Scene(loader.load());
            outputStage.setScene(newScene);
            outputStage.setTitle("Widok dla uczestnikow");
            outputStage.getIcons().add(ApplicationController.image);
            outputStage.initModality(Modality.NONE);
            participantViewController = (ParticipantViewController) loader.getController();
            participantViewController.setData(null,null);
        }catch (Exception e){
            System.out.format("Error while rendering participantView dialog");
            e.printStackTrace();
        }
        outputStage.setResizable(true);
        outputStage.setMaximized(true);
        outputStage.show();
    }


    public void update() {
        calculateResultsButtons = new ArrayList<>();

        tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int selectedIndex = newValue.intValue();
            System.out.println("SELECTED here: " + selectedIndex);
            if (selectedIndex == 0)
                menuBarController.setData(Competition.getInstance().getSingleWeaponCompetition(WeaponType.RAPIER), this);
            else if (selectedIndex == 1)
                menuBarController.setData(Competition.getInstance().getSingleWeaponCompetition(WeaponType.SABRE), this);
            else if (selectedIndex == 2)
                menuBarController.setData(Competition.getInstance().getSingleWeaponCompetition(WeaponType.SMALL_SWORD), this);
        });

        int tab = 0;
        if(rapierTab != null || sabreTab != null || smallSwordTab != null){
            tab = tabPane.getSelectionModel().getSelectedIndex();
            tabPane.getTabs().remove(0,3);
            rapierTab = initTab(WeaponType.RAPIER);
            sabreTab = initTab(WeaponType.SABRE);
            smallSwordTab = initTab(WeaponType.SMALL_SWORD);
            tabPane.getTabs().addAll(rapierTab,sabreTab,smallSwordTab);
            System.out.println("");
            tabPane.getSelectionModel().select(tab);
            System.out.println(menuBarController.wc.getWeaponType());


        }else{
            sabreTab = initTab(WeaponType.SABRE);
            smallSwordTab = initTab(WeaponType.SMALL_SWORD);
            rapierTab = initTab(WeaponType.RAPIER);
            tabPane.getTabs().addAll(rapierTab,sabreTab,smallSwordTab);
        }



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
        //tv.setSelectionModel(null);

        switch (wt){
            case SABRE: { sabreTableView = tv; break; }
            case SMALL_SWORD: { smallSwordTableView = tv; break; }
            case RAPIER: { rapierTableView = tv; break; }
        }

        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.setItems(weaponCompetitionParticipants.get(wt));

        TableColumn<Participant, String> name = new TableColumn<Participant, String>("imię");
        TableColumn<Participant, String> surname = new TableColumn<Participant, String>("nazwisko");
        TableColumn<Participant, RationalNumber> points = new TableColumn<>("punkty");
        TableColumn<Participant, String> group = new TableColumn<Participant, String>("grupa");

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
                if(AppMode.getMode().fSafe())
                    return;
                if (event.getButton().equals(MouseButton.SECONDARY) && !tableRow.isEmpty()) {
                    //tableRow.fireEvent(myEvent);
                    System.out.format("Right click on add injury " + wt + "\n");
                    Stage childScene = ApplicationController.getApplicationController().renderAddInjury("/addInjury.fxml", "Dodaj obrażenia", true, p, wt, this);
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

    private Participant getCurrentlySelectedParticipant(WeaponType wt){
        Participant p = null;
        switch (wt){
            case RAPIER: { p = (Participant) rapierTableView.getSelectionModel().getSelectedItem(); break; }
            case SMALL_SWORD: { p = (Participant) smallSwordTableView.getSelectionModel().getSelectedItem(); break; }
            case SABRE: { p = (Participant) sabreTableView.getSelectionModel().getSelectedItem(); break; }
        }
        return p;
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

        Button calculateResultButton = new Button();
        calculateResultButton.setMaxSize(1000, 1000);
        calculateResultButton.setText("policz wyniki");
        calculateResultButton.setOnAction(x -> {
            /* TODO: !!! check if all fights has selected score !!! */
            System.out.format("Check implementation\n");
            Competition.getInstance().getWeaponCompetition(WeaponType.RAPIER).setFinalRound();
            Competition.getInstance().getWeaponCompetition(WeaponType.SABRE).setFinalRound();
            Competition.getInstance().getWeaponCompetition(WeaponType.SMALL_SWORD).setFinalRound();

            /** For final results required */
            if(tabPane.getTabs().size() < 4){
                finalResultTab = initResultTab();
                tabPane.getTabs().add(finalResultTab);
                Competition.getInstance().calculateResults();
                //tabPane.getTabs().add(new Tab());
                //calculateResultButton.setDisable(true);
            }else{
                finalResultTab = initResultTab();
                Competition.getInstance().calculateResults();
                tabPane.getTabs().remove(3);
                tabPane.getTabs().add(finalResultTab);
                //calculateResultButton.setDisable(true);
            }
        });
        calculateResultsButtons.add(calculateResultButton);

        GridPane.setConstraints(calculateResultButton, 0, 1);

        Button nextRoundButton = new Button();
        nextRoundButton.setMaxSize(1000, 1000);
        nextRoundButton.setText("następna runda");
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
                stageroni.setResizable(false);
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
                if(rc.getfRoundReady())
                    rc.startRound();
            }
            if(Competition.getInstance().getWeaponCompetition(wt).getLastRound().getfFinal()) {
                switch (wt) {
                    case SABRE:
                        fSabreCompetitionFinals = true;
                        break;
                    case RAPIER:
                        fRapierCompetitionFinals = true;
                        break;
                    case SMALL_SWORD:
                        fSmallswordCompetitionFinals = true;
                        break;
                }
            }
            CheckPointManager.createCheckPoint();
            this.setData();
        });

        switch (wt){
            case SMALL_SWORD:
                if(fSmallswordCompetitionFinals)
                    nextRoundButton.setDisable(true);
                break;
            case RAPIER:
                if(fRapierCompetitionFinals)
                    nextRoundButton.setDisable(true);
                break;
            case SABRE:
                if(fSabreCompetitionFinals)
                    nextRoundButton.setDisable(true);
                break;
        }

        if(!(fRapierCompetitionFinals && fSabreCompetitionFinals && fSmallswordCompetitionFinals)) {
            for(Button c: calculateResultsButtons)
                c.setDisable(true);
        }

        GridPane.setConstraints(nextRoundButton, 0, 0);


        Button addPoints = new Button();
        addPoints.setMaxSize(1000, 1000);
        addPoints.setText("dodaj punkty");
        //addPoints.setStyle("-fx-background-color: green; -fx-padding: 10;");

        addPoints.setOnAction(x -> {
            System.out.println("ON ACTION: " + wt);
           Participant p = getCurrentlySelectedParticipant(wt);
            if(p == null)
                return;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addPoints.fxml"));
            Parent root = null;
            try { root = loader.load(); }
            catch (IOException ex) { System.out.println("error while loading add points");}
            Stage childStage = new Stage();
            childStage.setTitle("Dodaj punkty zawodnikowi");
            childStage.getIcons().add(ApplicationController.image);
            childStage.setResizable(false);
            childStage.setScene(new Scene(root));
            childStage.initModality(Modality.APPLICATION_MODAL);
            childStage.show();

            AddPointsController addPointsController = (AddPointsController) loader.getController();
            addPointsController.setData(p, Competition.getInstance().getWeaponCompetition(wt).getLastRound());
        });
        GridPane.setConstraints(addPoints, 0, 2);

        Button substractPoints = new Button();
        substractPoints.setMaxSize(1000, 1000);
        substractPoints.setText("odejmij punkty");
        //substractPoints.setStyle("-fx-background-color: grey; -fx-padding: 10;");

        substractPoints.setOnAction(x -> {
            Participant p = getCurrentlySelectedParticipant(wt);
            if(p == null)
                return;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/subtractPoints.fxml"));
            Parent root = null;
            try { root = loader.load(); }
            catch (IOException ex) { System.out.println("error while subtracting points");}
            Stage childStage = new Stage();
            childStage.setTitle("Odejmij punkty zawodnikowi");
            childStage.getIcons().add(ApplicationController.image);
            childStage.setResizable(false);
            childStage.setScene(new Scene(root));
            childStage.initModality(Modality.APPLICATION_MODAL);
            childStage.show();

            SubtractPointsController subtractPointsController = (SubtractPointsController) loader.getController();
            subtractPointsController.setData(p, Competition.getInstance().getWeaponCompetition(wt).getLastRound());
        });
        GridPane.setConstraints(substractPoints, 0, 3);

        GridPane.setConstraints(paneForButtons, 1, 0);

        paneForButtons.getChildren().addAll(nextRoundButton, calculateResultButton, addPoints, substractPoints);

        return paneForButtons;
    }

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

            ObservableList<CompetitionGroup> cgl = Competition.getInstance().
                    getSingleWeaponCompetition(wt).
                    getLastRound().
                    getGroups();
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
            Label text = new Label("GRUPY");
            text.setTextAlignment(TextAlignment.CENTER);
            text.setStyle("-fx-alignment: CENTER;");
            text.setFont(new Font(18));
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
                tableForGroup.setSelectionModel(null);
                GridPane.setConstraints(tableForGroup, groupsAdded % columns, currentRow);
                tableForGroup.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                TableColumn<Participant, String> tc = new TableColumn("grupa " + competitionGroup.getGroupID());
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

    private static void changeStyle(Fight fight, TableCell<Fight, String> cell){
        System.out.println(fight.scoreProperty().get());
        Node cell1 = cell.getTableRow().getChildrenUnmodifiable().get(0);
        Node cell2 = cell.getTableRow().getChildrenUnmodifiable().get(2);
        System.out.println("in change style");

        if (fight.scoreProperty().get().equals(FightScore.WON_FIRST)) {
            cell1.setStyle("-fx-alignment: CENTER; -fx-background-color: GREEN;");
            cell2.setStyle("-fx-alignment: CENTER; -fx-background-color: RED;");
        } else if (fight.getScore().equals(FightScore.NULL_STATE)) {
            cell1.setStyle("-fx-alignment: CENTER; -fx-background-color: TRANSPARENT;");
            cell2.setStyle("-fx-alignment: CENTER; -fx-background-color: TRANSPARENT;");
        } else if (fight.scoreProperty().get().equals(FightScore.WON_SECOND)) {
            cell1.setStyle("-fx-alignment: CENTER; -fx-background-color: RED;");
            cell2.setStyle("-fx-alignment: CENTER; -fx-background-color: GREEN;");
        }
        if (fight.scoreProperty().get().equals(FightScore.DOUBLE)){
            cell1.setStyle("-fx-alignment: CENTER; -fx-background-color: RED;");
            cell2.setStyle("-fx-alignment: CENTER; -fx-background-color: RED;");
        }
        participantViewController.setData(fight.getRound(), fight);
    }

    private static ListChangeListener<Fight> createListener(String listId) {
        return (ListChangeListener.Change<? extends Fight> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    System.out.println(listId + " added: " + c.getAddedSubList());
                }
                if (c.wasRemoved()) {
                    System.out.println(listId + " removed: " + c.getRemoved());
                }
                if (c.wasUpdated()) {

                    int index = c.getFrom() ;  // updated only one element
                    Fight fight = c.getList().get(index);
                    if (prtipantHashMap.containsKey(fight)){
                        System.out.println("CONTAINS");
                        changeStyle(fight, prtipantHashMap.get(fight));
                    }
                    else {
                        System.out.println(prtipantHashMap.size());
                        System.out.println("DOES NOT CONTAIN");
                    }

                    System.out.println(listId + " updated");
                }
            }
        };
    }


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
            Label text = new Label("WYNIKI WALK");
            text.setTextAlignment(TextAlignment.CENTER);
            text.setStyle("-fx-alignment: CENTER;");
            text.setFont(new Font(18));
            GridPane.setConstraints(text, 0, 0, columns, 1);
            GridPane.setHalignment(text, HPos.CENTER);
            GridPane.setFillHeight(text, true);

            for (int i = 0; i < groups.size(); i++) {
                int currentRow = i/ columns + 1;
                int currentColumn = i % columns;
                CompetitionGroup cg = groups.get(i);

                TableView tableForGroupFights = new TableView();
                tableForGroupFights.setSelectionModel(null);

                tableForGroupFights.setPadding(new Insets(5, 5, 5, 5));
                GridPane.setConstraints(tableForGroupFights, currentColumn, currentRow);
                tableForGroupFights.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                ObservableList<Fight> fights = FXCollections.observableArrayList(Fight.extractor());
                fights.addListener(createListener("listWithExtractor"));

                fights.addAll(cg.getFightsList());

                //tableForGroupFights.setItems(FXCollections.observableList(cg.getFightsList()));
                tableForGroupFights.setItems(fights);

                TableColumn<Fight, String> firstParticipant = new TableColumn<>();
                TableColumn<Fight, String> doubleColumn = new TableColumn<>();
                TableColumn<Fight, String> secondParticipant = new TableColumn<>();

                doubleColumn.setMinWidth(19);
                doubleColumn.setMaxWidth(20);

                doubleColumn.setStyle("-fx-alignment: CENTER;");
                firstParticipant.setStyle("-fx-alignment: CENTER;");
                secondParticipant.setStyle("-fx-alignment: CENTER;");

                firstParticipant.setText("pierwszy");
                doubleColumn.setText("X");
                secondParticipant.setText("drugi");;

                firstParticipant.setCellValueFactory(f -> f.getValue().firstParticipantStringProperty());
                secondParticipant.setCellValueFactory(f -> f.getValue().secondParticipantStringProperty());
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
                                if (fight.scoreProperty().get().equals(FightScore.WON_FIRST)) {
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
                        prtipantHashMap.put((Fight) cell.getTableRow().getItem(), cell);
                        if (e.getButton().equals(MouseButton.PRIMARY) && !cell.isEmpty()) {
                            Fight f = (Fight) cell.getTableRow().getItem();
                            if (f.getScore().equals(FightScore.WON_FIRST)) {
                                f.commandSetFightScoreDirect(FightScore.NULL_STATE);
                              } else {
                                f.commandSetFightScoreDirect(FightScore.WON_FIRST);
                            }
                            participantViewController.setData(f.getRound(),f);
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
                            System.out.println(getIndex());

                            if(!isEmpty()){

                                /* Person auxPerson = getTableView().getItems().get(getIndex()); */
                                Fight fight = getTableView().getItems().get(getIndex());
                                if (fight.scoreProperty().get().equals(FightScore.WON_SECOND)) {
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
                        prtipantHashMap.put((Fight) cell.getTableRow().getItem(), cell);

                        if (e.getButton().equals(MouseButton.PRIMARY) && !cell.isEmpty()) {
                            Fight f = (Fight) cell.getTableRow().getItem();
                            if (f.getScore().equals(FightScore.WON_SECOND)) {
                                f.commandSetFightScoreDirect(FightScore.NULL_STATE);
                            } else {
                                f.commandSetFightScoreDirect(FightScore.WON_SECOND);
                            }
                            participantViewController.setData(f.getRound(),f);
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
                        prtipantHashMap.put((Fight) cell.getTableRow().getItem(), cell);
                        if (e.getButton().equals(MouseButton.PRIMARY) && !cell.isEmpty()) {
                            Fight f = (Fight) cell.getTableRow().getItem();
                            if (f.getScore().equals(FightScore.DOUBLE)) {
                                f.commandSetFightScoreDirect(FightScore.NULL_STATE);
                            } else {
                                f.commandSetFightScoreDirect(FightScore.DOUBLE);
                            }
                            participantViewController.setData(f.getRound(),f);
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

    private Tab initTab(WeaponType wt) {
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
        switch (wt){
            case SABRE: { tabToRet.setText("szabla"); break; }
            case SMALL_SWORD: { tabToRet.setText("szpada"); break; }
            case RAPIER: { tabToRet.setText("rapier"); break; }
        }

        tabToRet.setContent(mainTabPane);

        return tabToRet;
    }

    /** For final results required */
    private Tab initResultTab() {

        /** Preparing GridPane for results */
        GridPane gridPane = new GridPane();
     //   gridPane.set();

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();

        row1.setPercentHeight(10);
        row2.setPercentHeight(90);

        gridPane.getRowConstraints().addAll(row1,row2);


        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();

        column1.setPercentWidth(5);
        column2.setPercentWidth(90);
        column3.setPercentWidth(5);

        gridPane.getColumnConstraints().addAll(column1,column2,column3);

        /** Preparing title */
        Label text = new Label("Final_Results");
        text.setTextAlignment(TextAlignment.CENTER);
        text.setStyle("-fx-alignment: CENTER;");
        text.setFont(new Font(20));
        GridPane.setConstraints(text, 1, 0);
        GridPane.setHalignment(text, HPos.CENTER);
        GridPane.setFillHeight(text, true);

        gridPane.getChildren().add(text);


        /** Preparing table view with results */
        ObservableList<Participant> participants = FXCollections.observableArrayList(Competition.getInstance().getParticipants());

        TableView tv = new TableView();
        tv.setItems(participants);

        TableColumn<Participant,String> surname = new TableColumn<>();
        TableColumn<Participant,String> name = new TableColumn<>();
        TableColumn<Participant,String> smallsword = new TableColumn<>();
        TableColumn<Participant,String> sabre = new TableColumn<>();
        TableColumn<Participant,String> rapier = new TableColumn<>();
        TableColumn<Participant,String> triathlonOpen = new TableColumn<>();
        TableColumn<Participant,String> triathlonWomen = new TableColumn<>();

        surname.setText("Nazwisko");
        name.setText("Imie");
        smallsword.setText("Szpada");
        sabre.setText("Szabla");
        rapier.setText("Rapier");
        triathlonOpen.setText("Trójbój Open");
        triathlonWomen.setText("Trójbój Kobiet");

        surname.setCellValueFactory( p -> new SimpleStringProperty(p.getValue().getSurname()));
        name.setCellValueFactory( p -> new SimpleStringProperty(p.getValue().getName()));
        smallsword.setCellValueFactory( p -> {
            StringProperty toRet = new SimpleStringProperty("-");
            toRet.setValue(p.getValue().getParticipantResult().getSmallSwordResults().getPlace().toString());
            return toRet;
        });
        sabre.setCellValueFactory( p -> {
            StringProperty toRet = new SimpleStringProperty("-");
            toRet.setValue(p.getValue().getParticipantResult().getSabreResults().getPlace().toString());
            return toRet;
        });
        rapier.setCellValueFactory( p -> {
            StringProperty toRet = new SimpleStringProperty("-");
            toRet.setValue(p.getValue().getParticipantResult().getRapierResults().getPlace().toString());
            return toRet;
        });
        triathlonOpen.setCellValueFactory( p -> {
            return new SimpleStringProperty(p.getValue().getParticipantResult().triathlonOpenProperty().get());
        });
        triathlonWomen.setCellValueFactory( p -> {
            return new SimpleStringProperty(p.getValue().getParticipantResult().triathlonWomenProperty().get());
        });

        GridPane.setConstraints(tv,1,1);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.getColumns().addAll(surname,name,smallsword,sabre,rapier,triathlonOpen,triathlonWomen);

        gridPane.getChildren().add(tv);


        Tab tabToRet = new Tab();
        tabToRet.setContent(gridPane);
        tabToRet.setClosable(false);
        tabToRet.setText("Final_Results");

        return tabToRet;
    }

}