package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerUtility;

public class Notebookpg2Controller {

  @FXML private Pane bookPane;
  @FXML private Pane mainPane;
  @FXML private Label timerLabel;

  private TimerUtility timer;

  @FXML
  private void onGoLastPage(MouseEvent event) throws IOException {
    App.playSound("pageflip.mp3");
    System.out.println("Go last page");
    App.goToPage(event, "notebookpg3");
  }

  @FXML
  private void onClickedBook(MouseEvent event) {
    System.out.println("Book clicked");
    // if the book is clicked, set the bookpane to be visible
    bookPane.setVisible(true);
    mainPane.setVisible(false);
  }

  @FXML
  private void onExitBook(ActionEvent event) throws IOException {
    App.goToDrawers(event);
  }

  @FXML
  private void onGoBackCrimeScene(ActionEvent event) throws IOException {
    System.out.println("Go back to crime scene");
    // if the back button is clicked, set the bookpane to be invisible
    App.openCrimeScene(event);
  }

  @FXML
  private void onGoFirstPage(MouseEvent event) throws IOException {
    App.playSound("pageflip.mp3");
    System.out.println("Go first page");
    App.goToPage(event, "notebookpg1");
  }

  public void setTimer(TimerUtility timer) {
    this.timer = timer;
    timer
        .timeSecondsProperty()
        .addListener(
            (obs, oldTime, newTime) -> {
              timerLabel.setText(timer.formatTime(newTime.intValue()));
            });
  }

  public Label getTimerLabel() {
    return timerLabel;
  }
}
