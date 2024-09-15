package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;

public class Notebookpg3Controller {

  @FXML private Pane bookPane;
  @FXML private Pane mainPane;

  @FXML
  private void onGoMiddlePage(MouseEvent event) throws IOException {
    System.out.println("Go middle page");
    App.goMiddlePage(event);
  }

  @FXML
  private void onExitBook(ActionEvent event) throws IOException {
    App.goToDrawers(event);
  }

  @FXML
  private void onClickedBook(MouseEvent event) {
    System.out.println("Book clicked");
    // if the book is clicked, set the bookpane to be visible
    bookPane.setVisible(true);
    mainPane.setVisible(false);
  }

  @FXML
  private void onGoBackCrimeScene(ActionEvent event) throws IOException {
    System.out.println("Go back to crime scene");
    // if the back button is clicked, set the bookpane to be invisible
    App.openCrimeScene(event);
  }
}
