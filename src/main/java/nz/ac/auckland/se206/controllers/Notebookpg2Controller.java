package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import nz.ac.auckland.se206.App;

public class Notebookpg2Controller {

  // FXML annotations to link with the corresponding elements in the FXML file
  @FXML private Pane bookPane;
  @FXML private Pane mainPane;
  @FXML private Label timerLabel;

  /**
   * Method to handle the event when the middle page is clicked.
   * 
   * @param event The mouse event that triggered the method
   * @throws IOException If an I/O error occurs
   */
  @FXML
  private void onGoLastPage(MouseEvent event) throws IOException {
    App.playSound("pageflip.mp3"); // Play page flip sound
    System.out.println("Go last page"); // Log action
    App.goToPage(event, "notebookpg3"); // Navigate to the last page
  }

  /**
   * Method to handle the event when the exit button is clicked.
   * 
   * @param event The action event that triggered the method
   * @throws IOException If an I/O error occurs
   */
  @FXML
  private void onClickedBook(MouseEvent event) {
    System.out.println("Book clicked"); // Log action
    // Make the bookPane visible and hide the mainPane
    bookPane.setVisible(true);
    mainPane.setVisible(false);
  }

  /**
   * Method to handle the event when the back button is clicked.
   * 
   * @param event The action event that triggered the method
   * @throws IOException If an I/O error occurs
   */
  @FXML
  private void onExitBook(ActionEvent event) throws IOException {
    App.playSound("button.mp3"); // Play button click sound
    App.goToDrawers(event); // Navigate to the drawers
  }

  /**
   * Method to handle the event when the book is clicked.
   * 
   * @param event
   * @throws IOException
   */
  @FXML
  private void onGoBackCrimeScene(ActionEvent event) throws IOException {
    App.playSound("button.mp3"); // Play button click sound
    System.out.println("Go back to crime scene"); // Log action
    App.openCrimeScene(); // Navigate to the crime scene
  }

  /**
   * Method to handle the event when the first page button is clicked.
   * 
   * @param event The mouse event that triggered the method
   * @throws IOException If an I/O error occurs
   */
  @FXML
  private void onGoFirstPage(MouseEvent event) throws IOException {
    App.playSound("pageflip.mp3"); // Play page flip sound
    System.out.println("Go first page"); // Log action
    App.goToPage(event, "notebookpg1"); // Navigate to the first page
  }

  /**
   * Getter method for the timer label for the notebook page.
   * 
   * @return The timer label
   */
  public Label getTimerLabel() {
    return timerLabel; // Getter for the timer label
  }
}
