package nz.ac.auckland.se206.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class GuessingController {

  @FXML private ResourceBundle resources;
  @FXML private URL location;
  @FXML private Rectangle chef;
  @FXML private Rectangle cleaner;
  @FXML private Rectangle daughter;
  @FXML private Button btnSubmit;
  @FXML private TextField txtInput;
  @FXML private Label lbSelectedSuspect;
  @FXML private Label lbSelected;

  
  private String chosenSuspect;
  
  @FXML
  private void initialize() {
    
    // set text field and labels to disabled and invisible
    txtInput.setDisable(true);
    lbSelectedSuspect.setDisable(true);
    lbSelected.setDisable(true);
    btnSubmit.setDisable(true);
    txtInput.setVisible(false);
    lbSelectedSuspect.setVisible(false);
    lbSelected.setVisible(false);
    btnSubmit.setVisible(false);
  }

  @FXML
  private void onClickedChef(MouseEvent event) {
    chosenSuspect = "chef";
    updateSelectedSuspect(chosenSuspect);
  }

  @FXML
  private void onClickedCleaner(MouseEvent event) {
    chosenSuspect = "cleaner";
    updateSelectedSuspect(chosenSuspect);
  }

  @FXML
  private void onClickedDaughter(MouseEvent event) {
    chosenSuspect = "daughter";
    updateSelectedSuspect(chosenSuspect);
  }

  @FXML
  private void onSubmitMessage(ActionEvent event) {

  }

  private void updateSelectedSuspect(String chosenSuspect) {
    lbSelectedSuspect.setText(chosenSuspect);
    showTextField();
  }

  private void showTextField() {
    txtInput.setDisable(false);
    txtInput.setVisible(true);
    lbSelectedSuspect.setDisable(false);
    lbSelectedSuspect.setVisible(true);
    lbSelected.setDisable(false);
    lbSelected.setVisible(true);
    btnSubmit.setDisable(false);
    btnSubmit.setVisible(true);
  }


}
