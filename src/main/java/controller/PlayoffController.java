package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Fight;
import model.Participant;
import model.WeaponCompetition;
import model.enums.FightScore;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlayoffController implements Initializable {

    @FXML
    Text textField;
    @FXML
    Button okButton;
    @FXML
    Button cancelButton;
    @FXML
    TableView participantTable;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ;
    }

    private WeaponCompetition.RoundCreator rc;
    private ObservableList<Participant> eligble;
    private List<Participant> chosen = new ArrayList<>();
    private StringProperty text_to_display;
    private TableColumn<Participant,String> tc = new TableColumn<>();

//    PlayoffController(WeaponCompetition.RoundCreator rc)
//    {
//    }

    public void setData(WeaponCompetition.RoundCreator rc)
    {
        this.rc=rc;
        eligble= FXCollections.observableArrayList(rc.getParticipantsForPlayoff());
        update();
    }

    private void setText()
    {
        String str= "Wybierz "+rc.getParticpantsNeededFromPlayoffs() + "\n" + "Wybrano: " + chosen.size();
        text_to_display.setValue(str);

    }

    private void self_update()
    {
        setText();
        if(chosen.size()==rc.getParticpantsNeededFromPlayoffs())
        {
            okButton.setDisable(false);
        }
        else
        {
            okButton.setDisable(true);
        }

    }
    public void okPressed()
    {
        rc.setPlayOffWinners(chosen);
        Stage toClose = (Stage) okButton.getScene().getWindow();
        toClose.close();
        System.out.format("confirmplayoff\n");
    }

    public void cancelPressed()
    {
        Stage toClose = (Stage) okButton.getScene().getWindow();
        toClose.close();
        System.out.format("cancelPlayyof\n");
    }


    private void update()
    {
        text_to_display=textField.textProperty();
        participantTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tc.setCellValueFactory(x->{
            String v= x.getValue().getName()+" " + x.getValue().getSurname();
            return new SimpleStringProperty(v);
        });
        tc.setCellFactory(
                hue ->{
                    TableCell<Participant, String> cell = new TableCell<Participant, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(empty ? null : item);
                            setStyle("-fx-alignment: CENTER;");
                        }
                    };
                    cell.setOnMouseClicked(e -> {
                        if(cell.isEmpty())
                            return;
                        if (e.getButton().equals(MouseButton.PRIMARY) && !cell.isEmpty()) {
                            Participant p  = (Participant) cell.getTableRow().getItem();
                            if(chosen.contains(p))
                            {
                                cell.setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
                                chosen.remove(p);
                            }
                            else
                            {
                                cell.setStyle("-fx-alignment: CENTER; -fx-background-color: blue;");
                                chosen.add(p);
                            }
                            self_update();
                        }
                    });
                    return cell;});
        participantTable.getColumns().removeAll();
        participantTable.getColumns().add(0,tc);
        participantTable.setItems(eligble);
        participantTable.setVisible(true);
        self_update();
    }


//    private void update()
//    {
//        TableView tv = new TableView();
//        tv.setItems(eligble);
//        GridPane.setConstraints(tv,0,0,1,2);
//        TableColumn<Participant,String> onlyColumn= new TableColumn<>();
//        onlyColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));
//        onlyColumn.setCellFactory(
//                hue ->{
//                    TableCell<Participant, String> cell = new TableCell<Participant, String>() {
//                        @Override
//                        protected void updateItem(String item, boolean empty) {
//                            super.updateItem(item, empty);
////                            setText(empty ? null : item);
//                        }
//                    };
//                    cell.setOnMouseClicked(e -> {
//                        if (e.getButton().equals(MouseButton.PRIMARY) && !cell.isEmpty()) {
//                            Participant p  = (Participant) cell.getTableRow().getItem();
//                            if(chosen.contains(p))
//                            {
//                                cell.setStyle("-fx-alignment: CENTER; -fx-background-color: transparent;");
//                                chosen.remove(p);
//                                self_update();
//
//                            }
//                            else
//                            {
//                                cell.setStyle("-fx-alignment: CENTER; -fx-background-color: green;");
//                                chosen.add(p);
//                                self_update();
//                            }
//                        }
//                    });
//             return cell;});
//        Text text  = new Text();
//        text_to_display=text.textProperty();
//        self_update();
//        GridPane.setConstraints(text,1,0);
//        ok_button.setOnAction(x->{
//            if (chosen.size()!= rc.getParticpantsNeededFromPlayoffs())
//            {
//                System.out.println("tried to klick ok_boomer and chosen was not enoug");
//            }
//            Stage toClose = (Stage) ok_button.getScene().getWindow();
//            toClose.close();
//        });
//        ok_button.setStyle("-fx-alignment: RIGHT;");
//        GridPane.setConstraints(ok_button,1,1);
//        Button cancel_button = new Button();
//        cancel_button.setOnAction(x->{
//            System.out.println("canceled choosing playoffs");
//            Stage toClose = (Stage) ok_button.getScene().getWindow();
//            toClose.close();
//        });
//        cancel_button.setStyle("-fx-alignment: RIGHT;");
//        GridPane.setConstraints(cancel_button,1,1);
//        basePane.getChildren().addAll(tv,text,ok_button,cancel_button);
//
//    }

}
