package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class CrimeSceneController {

  @FXML private ImageView map;
  @FXML private Rectangle laptopRectangle;
  @FXML private Rectangle bookshelfSafeRectangle;
  @FXML private Rectangle drawerRectangle;
  @FXML private Button guessingButton;
  @FXML private Label lbPopup;
  @FXML private Label lbPopup2;
  @FXML private Label timerLabel;
  @FXML private Label backstory1;
  @FXML private Label backstory2;
  @FXML private Label backstory3;
  @FXML private Rectangle backstoryRectangle;
  @FXML private ImageView closeButton;

  public Label getTimerLabel() {
    return timerLabel;
  }

  @FXML
  private void onMapClicked(MouseEvent event) {
    try {
      App.playSound("map.mp3");
      App.openMap(event, "/images/Study.png");
      MapController.setLastScene("crimeScene");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onLaptopClicked(MouseEvent event) {
    App.addCluesViewed("laptop");
    try {
      App.openLaptop(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onDrawersClicked(MouseEvent event) {
    App.addCluesViewed("drawer");
    try {
      App.playSound("draweropen.mp3");
      App.openDrawer(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onBookshelfSafeClicked(MouseEvent event) {
    App.addCluesViewed("safe");
    try {
      App.openSafe(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onGuessClicked(ActionEvent event) throws IOException {
    App.playSound("button.mp3");
    System.out.println("Guessing button clicked");

    List<Boolean> canGuessList = App.verifyCanGuess();
    Boolean canGuess = canGuessList.get(2);
    Boolean enoughCluesViewed = canGuessList.get(1);
    Boolean enoughSuspectsTalkedTo = canGuessList.get(0);

    if (canGuess) {
      App.openGuessingScene();
    } else {
      if (!enoughSuspectsTalkedTo && !enoughCluesViewed) {
        lbPopup2.setVisible(true);
      } else if (!enoughSuspectsTalkedTo) {
        lbPopup.setText("You need to talk to all suspects before making a guess.");
        lbPopup.setVisible(true);
      } else if (!enoughCluesViewed) {
        lbPopup.setText("You need to view at least 1 clue before making a guess.");
        lbPopup.setVisible(true);
      }

      Timer timer = new Timer();
      App.addTimer(timer);
      timer.schedule(
          new TimerTask() {
            @Override
            public void run() {
              lbPopup.setVisible(false);
              lbPopup2.setVisible(false);
            }
          },
          4000);
    }
  }

  @FXML
  private void onCloseClicked(MouseEvent event) {
    backstory1.setVisible(false);
    backstory2.setVisible(false);
    backstory3.setVisible(false);
    backstoryRectangle.setVisible(false);
    closeButton.setVisible(false);

    map.setMouseTransparent(false);
    laptopRectangle.setMouseTransparent(false);
    bookshelfSafeRectangle.setMouseTransparent(false);
    drawerRectangle.setMouseTransparent(false);
    guessingButton.setDisable(false);
  }
}
