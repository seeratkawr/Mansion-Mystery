package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.se206.App;

public class SafeKeypadController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private AnchorPane safePane;

    List<Integer> code = new ArrayList<>();
    List<Integer> password = new ArrayList<>(Arrays.asList(1, 4, 2, 8));

    /**
     * This method initializes the controller.
     */
    @FXML
    void initialize() {

        // reset the user entry code when the scene is loaded
        code.clear();
        
    }

    void validateCode(MouseEvent event) throws IOException {
        if (code.size() == 4) {
            if (code.equals(password)) {
                System.out.println("Correct code entered");
                App.openSafeOpened(event);
            } else {
                System.out.println("Incorrect code entered");
                code.clear();
            }
        }
    }
    
    @FXML
    void onClickedClear(MouseEvent event) {
        System.out.println("clearing code: +" + code);
        code.clear();
        System.out.println("code cleared");
    }

    @FXML
    void onClickedEight(MouseEvent event) throws IOException {
        code.add(8);
        validateCode(event);
        System.out.println("8");
    }

    @FXML
    void onClickedFive(MouseEvent event) throws IOException {
        code.add(5);
        validateCode(event);
        System.out.println("5");
    }

    @FXML
    void onClickedFour(MouseEvent event) throws IOException {
        code.add(4);
        validateCode(event);
        System.out.println("4");
    }

    @FXML
    void onClickedNine(MouseEvent event) throws IOException {
        code.add(9);
        validateCode(event);
        System.out.println("9");
    }

    @FXML
    void onClickedOne(MouseEvent event) throws IOException {
        code.add(1);
        validateCode(event);
        System.out.println("1");
    }

    @FXML
    void onClickedSeven(MouseEvent event) throws IOException {
        code.add(7);
        validateCode(event);
        System.out.println("7");
    }

    @FXML
    void onClickedSix(MouseEvent event) throws IOException {
        code.add(6);
        validateCode(event);
        System.out.println("6");
    }

    @FXML
    void onClickedThree(MouseEvent event) throws IOException {
        code.add(3);
        validateCode(event);
        System.out.println("3");
    }

    @FXML
    void onClickedTwo(MouseEvent event) throws IOException {
        code.add(2);
        validateCode(event);
        System.out.println("2");
    }

    @FXML
    void onClickedZero(MouseEvent event) throws IOException {
        code.add(0);
        validateCode(event);
        System.out.println("0");
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
