package controller;


import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.CompetitionGroup;
import model.Fight;
import model.Participant;
import model.Round;
import model.enums.FightScore;


/* Maybe it should be abstract class with elimination controller */
public class ParticipantViewController {

    private Round currentRound;

    private ScrollPane prepareGroupPane(int columns) {
        ScrollPane scrollPane = new ScrollPane();
        GridPane.setConstraints(scrollPane, 0, 0);
        /* TODO: Add grid pane inside scroll pane to include tables*/
        ObservableList<CompetitionGroup> competitionGroups = this.currentRound.getGroups();

        int rows = competitionGroups.size() % columns == 0 ? (competitionGroups.size() / columns) : (competitionGroups.size() / columns + 1);
        GridPane gridPane = new GridPane();

        for (int i = 0; i < rows; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            rc.setPercentHeight(100.0 / rows);
            gridPane.getRowConstraints().add(rc);
        }

        for (int i = 0; i < columns; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setPercentWidth(100.0 / columns);
            gridPane.getColumnConstraints().add(cc);
        }
        int i = 0;
        /* Create each table for Competition group */
        for (CompetitionGroup cg : competitionGroups) {
            int currentRow = (i + 1) / columns + 1;
            int currentColumn = i % columns;
            TableView<Fight> tableViewForGroupFights = new TableView<>();
            tableViewForGroupFights.setPadding(new Insets(5, 5, 5, 5));
            GridPane.setConstraints(tableViewForGroupFights, currentColumn, currentRow);
            tableViewForGroupFights.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            tableViewForGroupFights.setItems(FXCollections.observableList(cg.getFightsList()));

            TableColumn<Fight, Fight> oneVStwo = new TableColumn();
            oneVStwo.setStyle("-fx-alignment: CENTER;");
            oneVStwo.setText(cg.getGroupID());

            oneVStwo.setCellValueFactory(f -> new SimpleObjectProperty<Fight>(f.getValue()));
            oneVStwo.setCellFactory(column -> {
                return new TableCell<Fight, Fight>() {

                    @Override
                    protected void updateItem(Fight fight, boolean empty) {
                        super.updateItem(fight, empty);

                        if (fight == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            TextFlow textFlow = new TextFlow();
                            Text first = new Text(fight.getFirstParticipant().getName() + " " + fight.getFirstParticipant().getSurname());
                            Text vs = new Text(" VS ");
                            Text second = new Text(fight.getSecondParticipant().getName() + " " + fight.getSecondParticipant().getSurname());
                            if (fight.getScore().equals(FightScore.WON_FIRST)) {
                                first.setStyle("-fx-text-fill: GREEN");
                                vs.setStyle("-fx-text-fill: BLACK");
                                second.setStyle("-fx-text-fill: BLACK");
                            } else if (fight.getScore().equals(FightScore.WON_SECOND)) {
                                first.setStyle("-fx-text-fill: BLACK");
                                vs.setStyle("-fx-text-fill: BLACK");
                                second.setStyle("-fx-text-fill: GREEN");
                            } else if (fight.getScore().equals(FightScore.DOUBLE)) {
                                first.setStyle("-fx-text-fill: RED");
                                vs.setStyle("-fx-text-fill: BLACK");
                                second.setStyle("-fx-text-fill: RED");
                            } else {
                                first.setStyle("-fx-text-fill: BLACK");
                                vs.setStyle("-fx-text-fill: BLACK");
                                second.setStyle("-fx-text-fill: BLACK");
                            }
                            textFlow.getChildren().addAll(first, vs, second);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            setGraphic(textFlow);
                        }
                    }
                };
            });
            i++;
        }


        return new ScrollPane();
    }

    /* Prepare last text add the bottom */


    public void update() {
        ScrollPane groupPane = prepareGroupPane(3);
    }

    public void setData(Round round) {
        this.currentRound = round;
        this.update();
    }


}
