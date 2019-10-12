package controller;

import javafx.fxml.FXML;
import model.Participant;

public class AddInjuryController implements GeneralPopupControllerInterface {

    @FXML
    Participant p;

    @Override
    public void setData(Participant p) {
       this.p = p;
    }
}
