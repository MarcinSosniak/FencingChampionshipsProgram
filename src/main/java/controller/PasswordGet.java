package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import model.enums.TriRet;
import util.Pointer;

import java.net.URL;
import java.util.ResourceBundle;

public class PasswordGet implements Initializable {

    @FXML
    PasswordField password;
    @FXML
    Button okButton;
    @FXML
    Button cancelButton;

    private String goodPassword= null;
    private Pointer<TriRet> out= null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setData(String goodPassword,Pointer<TriRet> ret)
    {
        this.goodPassword=goodPassword;
        this.out=ret;
        out.set(TriRet.NO_VALUE);
    }

    public void okPressed()
    {
        try{
            if(password.getText().equals(goodPassword))
                out.set(TriRet.TRUE);
            else
                out.set(TriRet.FALSE);
        }
        catch (Exception ex){out.set(TriRet.FALSE);}
        Stage toClose = (Stage) okButton.getScene().getWindow();
        toClose.close();
        System.out.format("cancel ok password\n");
    }

    public void cancelPressed()
    {
        out.set(TriRet.NO_VALUE);
        Stage toClose = (Stage) okButton.getScene().getWindow();
        toClose.close();
        System.out.format("cancel get password\n");
    }
}
