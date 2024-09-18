package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;

public class NotebookController {

  @FXML Pane bookPane;
  @FXML Pane mainPane;

  @FXML
  private void onClickedBook(MouseEvent event) throws IOException {
    App.goToPage(event, "notebookpg1");
  }

  @FXML
  private void onGoBackCrimeScene(ActionEvent event) throws IOException {
    App.playSound("button.mp3");
    System.out.println("Go back to crime scene");
    // if the back button is clicked, set the bookpane to be invisible
    App.openCrimeScene(event);
  }
}
