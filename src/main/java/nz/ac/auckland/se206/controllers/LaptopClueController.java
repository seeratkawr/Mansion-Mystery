package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import nz.ac.auckland.se206.App;

// Controller class for the Laptop Clue scene
public class LaptopClueController {
  @FXML private Button backButton; // Button to go back to the previous scene
  @FXML private Circle alexCircle; // Circle representing Alex
  @FXML private Circle mariaCircle; // Circle representing Maria
  @FXML private Circle jamesCircle; // Circle representing James
  @FXML private Label timerLabel; // Label to display the timer
  @FXML private Circle hoverCircle1; // Circle button 1
  @FXML private Circle hoverCircle2; // Circle button 2
  @FXML private Circle hoverCircle3; // Circle button 3

  // Getter for the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }

  // Method called when the controller is initialized
  @FXML
  public void initialize() {
    // Set images for the circles
    jamesCircle.setFill(new ImagePattern(new Image("/images/chef.jpg")));
    mariaCircle.setFill(new ImagePattern(new Image("/images/daughter.jpg")));
    alexCircle.setFill(new ImagePattern(new Image("/images/cleaner.jpg")));
  }

  // Method called when a circle is clicked
  @FXML
  private void onCircleClicked(MouseEvent event) {

    // Check which circle was clicked and open the corresponding clue
    try {
      if (event.getTarget() == jamesCircle || event.getTarget() == hoverCircle1) {
        App.playSound("mouseclick.mp3");
        App.openLaptopClue(event, "/fxml/jamesClue.fxml");
      } else if (event.getTarget() == mariaCircle || event.getTarget() == hoverCircle2) {
        App.playSound("mouseclick.mp3");
        App.openLaptopClue(event, "/fxml/mariaClue.fxml");
      } else if (event.getTarget() == alexCircle || event.getTarget() == hoverCircle3) {
        App.playSound("mouseclick.mp3");
        App.openLaptopClue(event, "/fxml/alexClue.fxml");
      }
      // Catch any exceptions that occur 
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Method called when the back button is clicked
  @FXML
  private void onGoBack(ActionEvent event) {
    try {
      // Go back to the crime scene
      App.playSound("button.mp3");
      App.closeClue(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
