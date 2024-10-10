package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;

/**
 * NoteBookpg1Controller is responsible for handling the interactions on the
 * first page of the
 * notebook. It manages the timer display and navigation between pages.
 */
public class NoteBookpg1Controller {

  @FXML private Label timerLabel; // Label to display the timer

  /**
   * Handles the event when the user clicks to go to the middle page of the notebook.
   *
   * @param event the mouse event
   * @throws IOException if an I/O error occurs
   */
  @FXML
  private void onGoMiddlePage(MouseEvent event) throws IOException {
    App.playSound("pageflip.mp3"); // Play page flip sound
    App.goToPage(event, "notebookpg2"); // Navigate to the middle page
    System.out.println("Go right page"); // Log the action
  }

  /**
   * Handles the event when the user clicks to exit the notebook.
   *
   * @param event the action event
   * @throws IOException if an I/O error occurs
   */
  @FXML
  private void onExitBook(ActionEvent event) throws IOException {
    App.playSound("button.mp3"); // Play button click sound
    System.out.println("Go back"); // Log the action
    // if the back button is clicked, set the bookpane to be invisible
    App.goToDrawers(event); // Navigate back to the drawers
  }

  /**
   * Gets the timer label for the notebook page.
   *
   * @return the timer label
   */
  public Label getTimerLabel() {
    return timerLabel;
  }
}
