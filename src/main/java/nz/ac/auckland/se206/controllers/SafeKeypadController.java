package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.se206.App;

public class SafeKeypadController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane safePane;

    /**
     * This method initializes the controller.
     */
    @FXML
    void initialize() {
        assert safePane != null : "fx:id=\"safePane\" was not injected: check your FXML file 'safeKeypad.fxml'.";

    }
    
    @FXML
    void onClickedClear(MouseEvent event) {

    }

    @FXML
    void onClickedEight(MouseEvent event) {

    }

    @FXML
    void onClickedFive(MouseEvent event) {

    }

    @FXML
    void onClickedFour(MouseEvent event) {

    }

    @FXML
    void onClickedNine(MouseEvent event) {

    }

    @FXML
    void onClickedOne(MouseEvent event) {

    }

    @FXML
    void onClickedSeven(MouseEvent event) {

    }

    @FXML
    void onClickedSix(MouseEvent event) {

    }

    @FXML
    void onClickedThree(MouseEvent event) {

    }

    @FXML
    void onClickedTwo(MouseEvent event) {

    }

    @FXML
    void onClickedZero(MouseEvent event) {

    }
    
    /**
     * This method is called when the user clicks the go back button. It returns
     * users to the safe scene.
     *
     * @param event the event that triggered this method
     * @throws IOException if the FXML file is not found
     */
    @FXML
    void onGoBackCrimeScene(ActionEvent event) throws IOException {
        System.out.println("Go back to crimescene");
        App.openCrimeScene(event);
    }

}
