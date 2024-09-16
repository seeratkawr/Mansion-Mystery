package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.se206.App;

public class SafeOpenedController {

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
        assert safePane != null : "fx:id=\"safePane\" was not injected: check your FXML file 'safeOpened.fxml'.";

    }

    /**
     * This method is called when the user clicks the go back button. It returns users to the safe scene.
     *
     * @param event the event that triggered this method
     * @throws IOException if the FXML file is not found
     */
    @FXML
    void onGoBackSafe(MouseEvent event) throws IOException {

    }

}
