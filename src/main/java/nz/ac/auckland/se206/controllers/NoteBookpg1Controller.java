package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;

public class NoteBookpg1Controller {

  @FXML
  private void onGoMiddlePage(MouseEvent event) throws IOException {
    App.playSound("pageflip.mp3");
    App.goToPage(event, "notebookpg2");
    System.out.println("Go right page");
  }

  @FXML
  private void onExitBook(ActionEvent event) throws IOException {
    App.playSound("button.mp3");
    System.out.println("Go back");
    // if the back button is clicked, set the bookpane to be invisible
    App.goToDrawers(event);
  }
}
