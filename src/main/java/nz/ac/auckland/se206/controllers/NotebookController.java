package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;

public class NotebookController {

  @FXML private Pane bookPane;
  @FXML private Pane mainPane;

  // initially set the bookpane to be invisible
  public void initialize() {
    mainPane.setVisible(true);
    bookPane.setVisible(false);
  }

  @FXML
  private void onClickedBook(MouseEvent event) {
    System.out.println("Book clicked");
    // if the book is clicked, set the bookpane to be visible
    bookPane.setVisible(true);
    mainPane.setVisible(false);
  }

  @FXML
  private void onGoRightPage(MouseEvent event) {
    System.out.println("Go right page");
  }

  @FXML
  private void onGoLeftPage(MouseEvent event) {
    System.out.println("Go left page");
  }

  @FXML
  private void onExitBook(ActionEvent event) {
    System.out.println("Go back");
    // if the back button is clicked, set the bookpane to be invisible
    bookPane.setVisible(false);
    mainPane.setVisible(true);
  }

  @FXML
  private void onGoBackCrimeSeceen(ActionEvent event) throws IOException {
    System.out.println("Go back to crime scene");
    // if the back button is clicked, set the bookpane to be invisible
    App.openCrimeScene(event);
  }
}
