package controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.CompetitionGroup;
import model.Fight;
import model.Round;
import model.enums.FightScore;


/* Maybe it should be abstract class with elimination controller */
public class ParticipantViewController {

    private Round currentRound;

    @FXML
    GridPane mainPane;


    private TextFlow prepareTextFlowFromFight(Fight fight) {
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
        return textFlow;
    }

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
            int currentRow = (i) / columns;
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
                        System.out.format("xDDDD");
                        super.updateItem(fight, empty);
                        if (fight == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            System.out.format("xDDDD");
                            TextFlow textFlow = prepareTextFlowFromFight(fight);
                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                            setGraphic(textFlow);
                        }
                    }
                };
            });
            gridPane.getChildren().addAll(tableViewForGroupFights);
            i++;
        }

        scrollPane.fitToWidthProperty();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(gridPane);
        return scrollPane;
    }

    /* Prepare last text add the bottom */
    private VBox prepareLastFightPane() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        GridPane.setConstraints(vBox, 0, 1);
        Fight fight = this.currentRound.getLastModyfiedFight();
        Text lastWrittenScore = new Text("Ostatnio wpisany wynik\n");
        lastWrittenScore.setFont(new Font(20));
        TextFlow textFlow = prepareTextFlowFromFight(fight);
        vBox.getChildren().addAll(lastWrittenScore, textFlow);
        return vBox;
    }


    public void update() {
        ScrollPane groupPane = prepareGroupPane(3);
        VBox vBox = prepareLastFightPane();
        mainPane.getChildren().addAll(groupPane, vBox);
    }

    public void setData(Round round) {
        this.currentRound = round;
        this.update();
    }


}
