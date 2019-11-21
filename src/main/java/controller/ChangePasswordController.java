package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CheckPointManager;
import model.Competition;
import model.config.ConfigReader;

public class ChangePasswordController {

    @FXML
    PasswordField newPassword;
    @FXML
    PasswordField repeatedNewPassword;
    @FXML
    PasswordField oldPassword;

    private static String incorrectPassword = "Niepoprawne has" + "\u0142" + "o";
    private static String passwordsDoNotMatch = "Wprowadzone has" + "\u0142" + "o nie zgadza si"+ "\u0119" +
            " z powt" + "\u00F3" + "rzonym";
    private static String error = "B" + "\u0142" + "\u0105" + "d";
    private static String confirmation = "Has" + "\u0142" + "o zosta" +  "\u0142" + "o zmienione";


    public void confirmChangingPassword(){
        String old = oldPassword.getText();
        if (! old.equals(Competition.getInstance().getPassword()))
            handleBadPassword();
        else {
            if (! newPassword.getText().equals(repeatedNewPassword.getText()))
                handlePasswordAndRepeatedDoNoMatch();
            else {
                handleConfirmation();
                ApplicationController.currentStage.close();
            }
        }
    }

    public void cancelChangingPassword(){

    }

    private void handleBadPassword(){
        Alert alert = new Alert(Alert.AlertType.ERROR, incorrectPassword, ButtonType.OK);
        alert.setHeaderText(error);
        Scene childScene = alert.getDialogPane().getScene();
        Stage stage = new Stage();
        stage.setScene(childScene);
        stage.getIcons().add(ApplicationController.image);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setOnAction(e -> {
            stage.close();
        });
        stage.showAndWait();
    }

    private void handlePasswordAndRepeatedDoNoMatch(){
        Alert alert = new Alert(Alert.AlertType.ERROR, passwordsDoNotMatch, ButtonType.OK);
        alert.setHeaderText(error);
        Scene childScene = alert.getDialogPane().getScene();
        Stage stage = new Stage();
        stage.setScene(childScene);
        stage.getIcons().add(ApplicationController.image);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setOnAction(e -> {
            stage.close();
        });
        stage.showAndWait();
    }

    private void handleConfirmation(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, confirmation, ButtonType.OK);
        alert.setHeaderText("Potwierdzenie");
        Scene childScene = alert.getDialogPane().getScene();
        Stage stage = new Stage();
        stage.setScene(childScene);
        stage.getIcons().add(ApplicationController.image);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setOnAction(e -> {
            stage.close();
        });
        stage.showAndWait();

        ConfigReader.getInstance().setStringValue("SECURITY", "PASSWORD", newPassword.getText());
        Competition.getInstance().setPassword(newPassword.getText());
        CheckPointManager.createCheckPoint();
    }
}
