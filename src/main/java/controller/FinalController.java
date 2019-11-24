package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.*;
import model.enums.FightScore;
import model.enums.WeaponType;
import model.exceptions.NoSuchWeaponException;
import util.RationalNumber;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FinalController implements Initializable {

    @FXML
    ObservableMap<WeaponType, ObservableList<Participant>> weaponCompetitionParticipants;
    @FXML
    TabPane tabPane;
    @FXML
    Tab rapierTab;
    @FXML
    Tab sabreTab;
    @FXML
    Tab smallSwordTab;


    private TableView rapierTableView;
    private TableView sabreTableView;
    private TableView smallSwordTableView;
    public ObservableList<TableRow> rapierRows = FXCollections.observableArrayList();
    public ObservableList<TableRow> sabreRows = FXCollections.observableArrayList();
    public ObservableList<TableRow> smallSwordRows = FXCollections.observableArrayList();

    @FXML
    MenuBarController menuBarController;



    public FinalController(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
    }

    public void update() {
        rapierTab = initTab(WeaponType.RAPIER);
        sabreTab = initTab(WeaponType.SABRE);
        smallSwordTab = initTab(WeaponType.SMALL_SWORD);
        tabPane.getTabs().add(rapierTab);
        tabPane.getTabs().add(sabreTab);
        tabPane.getTabs().add(smallSwordTab);
        // first value
        menuBarController.setData(Competition.getInstance().getSingleWeaponCompetition(WeaponType.RAPIER));
        // change tab handler
        tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int selectedIndex = newValue.intValue();
            if (selectedIndex == 0)
                menuBarController.setData(Competition.getInstance().getSingleWeaponCompetition(WeaponType.RAPIER));
            else if (selectedIndex == 1)
                menuBarController.setData(Competition.getInstance().getSingleWeaponCompetition(WeaponType.SABRE));
            else if (selectedIndex == 2)
                menuBarController.setData(Competition.getInstance().getSingleWeaponCompetition(WeaponType.SMALL_SWORD));
        });

    }


    Tab initTab(WeaponType wt) {
        GridPane mainTabPane = prepareGridPaneForTab();
        VBox v = new VBox();
        GridPane.setConstraints(v, 0, 1, 2, 1);

        /* Add tableView pane */
        GridPane tableViewPane = prepareTableViewPane(wt);

        /* Add button panel */
        GridPane buttonPane = prepareButtonPane(wt);

        /* Add Final ladder */
        GridPane finalLadderPane = prepareFinalLadderPane(wt);


        /* Add to main tab pane */
        mainTabPane.getChildren().addAll(finalLadderPane,tableViewPane, buttonPane, v);


        /* Add content to tab*/
        Tab tabToRet = new Tab();
        tabToRet.setClosable(false);
        tabToRet.setText(wt.toString());
        tabToRet.setContent(mainTabPane);

        return tabToRet;
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
                return x.getValue().getPointsForWeaponPropertyLastRound(wt);
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
            return tableRow;
        });


        GridPane.setConstraints(tv, 0, 0);
        tableViewGridPane.getChildren().add(tv);
        return tableViewGridPane;

    }

    private GridPane prepareGridPaneForTab() {
        GridPane mainTabPane = new GridPane();

        /* Rows preparation */
        RowConstraints row1 = new RowConstraints(); /* For table and button controller*/
        RowConstraints row2 = new RowConstraints(); /* For groups and fight */
        row1.setPercentHeight(24);
        row2.setPercentHeight(76);
        mainTabPane.getRowConstraints().addAll(row1, row2);


        /* Columns preparation */
        ColumnConstraints column1 = new ColumnConstraints(); /* For Table with participants */
        ColumnConstraints column2 = new ColumnConstraints(); /* For button controller */
        column1.setPercentWidth(80);
        column2.setPercentWidth(20);
        mainTabPane.getColumnConstraints().addAll(column1, column2);

        return mainTabPane;
    }

    private GridPane prepareButtonPane(WeaponType wt) {
        GridPane paneForButtons = new GridPane();

        RowConstraints row0 = new RowConstraints();
        row0.setPercentHeight(50);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100);

        paneForButtons.getRowConstraints().addAll(row0, row1);
        paneForButtons.getColumnConstraints().addAll(cc);


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
        GridPane.setConstraints(addPoints, 0, 0);

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
        GridPane.setConstraints(substractPoints, 0, 1);

        GridPane.setConstraints(paneForButtons, 1, 0);

        paneForButtons.getChildren().addAll( addPoints, substractPoints);

        return paneForButtons;
    }



    private class FightFactory{
        ObservableList<Fight> fight;
        HBox boxForButtons = new HBox();

        private TableView generate_tv(){
            TableView tv = new TableView();
            tv.setItems(FXCollections.observableList(this.fight));
            TableColumn<Fight,String> firstParticipantColumn = new TableColumn<>();
            TableColumn<Fight,String> secondParticipantColumn = new TableColumn<>();
            TableColumn<Fight,String> doubleColumn = new TableColumn<>();

            firstParticipantColumn.setText("First");
            secondParticipantColumn.setText("Second");
            doubleColumn.setText(" X ");
            doubleColumn.setMinWidth(19);
            doubleColumn.setMaxWidth(20);

            firstParticipantColumn.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getFirstParticipant().nameProperty().getValue() + " " + f.getValue().getFirstParticipant().surnameProperty().getValue()));
            secondParticipantColumn.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getSecondParticipant().nameProperty().getValue() + " " + f.getValue().getSecondParticipant().surnameProperty().getValue()));
            doubleColumn.setCellValueFactory(f -> new SimpleStringProperty("x"));

            firstParticipantColumn.setCellFactory(tc -> {
                TableCell<Fight, String> cell = new TableCell<Fight, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : item);
                    }
                };
                Fight f = (Fight) cell.getTableRow().getItem();
                if(f.getScore().equals(FightScore.WON_FIRST)){
                    cell.getTableRow().getChildrenUnmodifiable().get(1).setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                    cell.getTableRow().getChildrenUnmodifiable().get(2).setStyle("-fx-alignment: CENTER; -fx-background-color: RED;");
                    cell.setStyle("-fx-alignment: CENTER; -fx-background-color: GREEN;");
                }else if(f.getScore().equals(FightScore.WON_SECOND)) {
                    cell.getTableRow().getChildrenUnmodifiable().get(1).setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                    cell.getTableRow().getChildrenUnmodifiable().get(2).setStyle("-fx-alignment: CENTER; -fx-background-color: GREEN;");
                    cell.setStyle("-fx-alignment: CENTER; -fx-background-color: RED;");

                }else if(f.getScore().equals(FightScore.DOUBLE)) {
                    cell.getTableRow().getChildrenUnmodifiable().get(1).setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                    cell.getTableRow().getChildrenUnmodifiable().get(2).setStyle("-fx-alignment: CENTER; -fx-background-color: RED;");
                    cell.setStyle("-fx-alignment: CENTER; -fx-background-color: RED;");

                }else if(f.getScore().equals(FightScore.NULL_STATE)) {
                    cell.getTableRow().getChildrenUnmodifiable().get(1).setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                    cell.getTableRow().getChildrenUnmodifiable().get(2).setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                    cell.setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                }
                cell.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.PRIMARY) && !cell.isEmpty()) {
                        if(f.getScore().equals(FightScore.NULL_STATE)) {
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
                    }
                });
                return cell;
            });

            secondParticipantColumn.setCellFactory(tc -> {
                TableCell<Fight, String> cell = new TableCell<Fight, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null : item);
                    }
                };
                cell.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.PRIMARY) && !cell.isEmpty()) {
                        Fight f = (Fight) cell.getTableRow().getItem();
                        if (f.getScore().equals(FightScore.NULL_STATE)) {
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
                    }
                };
                cell.setOnMouseClicked(e -> {
                    if (e.getButton().equals(MouseButton.PRIMARY) && !cell.isEmpty()) {
                        Fight f = (Fight) cell.getTableRow().getItem();
                        if (f.getScore().equals(FightScore.NULL_STATE)) {
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
                    }
                });
                return cell;
            });

            tv.getColumns().addAll(firstParticipantColumn,doubleColumn,secondParticipantColumn);

            return tv;
        }

        public FightFactory(Fight fight){
            ArrayList<Fight> list = new ArrayList<>();
            list.add(fight);
            this.fight = FXCollections.observableArrayList(list);
            TableView<Fight> tv = generate_tv();
            this.boxForButtons.getChildren().addAll(tv);
        }


        HBox getHBox(){
            return this.boxForButtons;
        }

    }

    private GridPane prepareFinalLadderPane(WeaponType wt) {
        GridPane gridPaneForFights = new GridPane();
        FinalRound finalRound = Competition.getInstance().getSingleWeaponCompetition(wt).getFinalRound();

        RowConstraints finalRow = new RowConstraints();
        finalRow.setPercentHeight(33);
        RowConstraints thirdPlaceRow = new RowConstraints();
        thirdPlaceRow.setPercentHeight(33);
        RowConstraints semiFinalRow = new RowConstraints();
        semiFinalRow.setPercentHeight(33);

        gridPaneForFights.getRowConstraints().addAll(finalRow,thirdPlaceRow,semiFinalRow);

        ColumnConstraints first = new ColumnConstraints();
        first.setPercentWidth(50);
        ColumnConstraints second = new ColumnConstraints();
        second.setPercentWidth(50);

        gridPaneForFights.getColumnConstraints().addAll(first,second);

        Fight semiFinal1 = finalRound.getSemiFinal1();
        Fight semiFinal2 = finalRound.getSemiFinal2();

        FightFactory factorsemifinal1 = new FightFactory(semiFinal1);
        FightFactory factorsemifinal2 = new FightFactory(semiFinal2);

        HBox semi1HBox = factorsemifinal1.getHBox();
        HBox semi2HBox = factorsemifinal2.getHBox();

        GridPane.setConstraints(semi1HBox,0,2);
        GridPane.setConstraints(semi2HBox,1,2);

        if(finalRound.fFinalFightsReady()){
            Fight finalFight = finalRound.getFinalFight();
            Fight thirdPlaceFight = finalRound.getThirdPlaceFight();

            HBox finalFightBox = new FightFactory(finalFight).getHBox();
            HBox thirdPlaceFightBox = new FightFactory(thirdPlaceFight).getHBox();

            GridPane.setConstraints(finalFightBox,0,0,2,1);
            GridPane.setConstraints(thirdPlaceFightBox,0,1,2,1);

            gridPaneForFights.getChildren().addAll(finalFightBox,thirdPlaceFightBox);

        }

        gridPaneForFights.getChildren().addAll(semi1HBox,semi2HBox);

        return gridPaneForFights;
    }







}
