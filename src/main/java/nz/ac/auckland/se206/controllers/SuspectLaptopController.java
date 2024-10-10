package nz.ac.auckland.se206.controllers;

// nz.ac.auckland.se206.controllers.SuspectLaptopController
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

public class SuspectLaptopController {
  @FXML private Button backButton;
  @FXML private Circle alexCircle;
  @FXML private Circle mariaCircle;
  @FXML private Circle jamesCircle;
  @FXML private Label timerLabel;

  public Label getTimerLabel() {
    return timerLabel;
  }

  @FXML
  public void initialize() {
    jamesCircle.setFill(new ImagePattern(new Image("/images/chef.jpg")));
    mariaCircle.setFill(new ImagePattern(new Image("/images/daughter.jpg")));
    alexCircle.setFill(new ImagePattern(new Image("/images/cleaner.jpg")));
  }

  @FXML
  private void onCircleClicked(MouseEvent event) {
    try {
      if (event.getTarget() == jamesCircle) {
        App.playSound("mouseclick.mp3");
        App.openLaptopClue(event, "/fxml/jamesClue.fxml");
      } else if (event.getTarget() == mariaCircle) {
        App.playSound("mouseclick.mp3");
        App.openLaptopClue(event, "/fxml/mariaClue.fxml");
      } else if (event.getTarget() == alexCircle) {
        App.playSound("mouseclick.mp3");
        App.openLaptopClue(event, "/fxml/alexClue.fxml");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onGoBackCrimeScene(ActionEvent event) {
    try {
      // Go back to the crime scene
      App.playSound("button.mp3");
      App.openCrimeScene();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
