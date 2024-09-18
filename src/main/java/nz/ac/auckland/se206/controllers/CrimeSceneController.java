package nz.ac.auckland.se206.controllers;

import java.io.IOException; // Add this import statement
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class CrimeSceneController {

  // set the game state to game started
  @FXML private ImageView map;
  @FXML private Rectangle laptopRectangle;
  @FXML private Rectangle bookshelfSafeRectangle;
  @FXML private Button guessingButton;
  @FXML private Label lbPopup;

  @FXML
  private void onMapClicked(MouseEvent event) {

    try {
      // Open the map view
      App.openMap(event, "/images/Study.png");
      MapController.setLastScene("crimeScene");

    } catch (IOException e) {
      // Print stack trace for debugging in case of error
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
      // Open the drawer view
      App.playSound("draweropen.mp3");
      App.openDrawer(event);
    } catch (IOException e) {
      // Print stack trace for debugging in case of error
      e.printStackTrace();
    }
  }

  /**
   * This method is called when the user clicks the bookshelf safe.
   *
   * @param event the event that triggered this method
   */
  @FXML
  private void onBookshelfSafeClicked(MouseEvent event) {
    App.addCluesViewed("safe");
    try {
      App.openSafe(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is called when the user clicks the guessing button. It will open the guess view if
   * the user has viewed at least 1 clue and spoken to all suspects.
   *
   * @param event the event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  @FXML
  private void onGuessClicked(ActionEvent event) throws IOException {
    App.playSound("button.mp3");
    System.out.println("Guessing button clicked");
    
    // verifyCanGuess() returns a list of booleans in the format 
    // [enoughSuspectsTalkedTo, enoughCluesViewed, canGuess]
    Boolean canGuess = App.verifyCanGuess().get(2);
    Boolean enoughCluesViewed = App.verifyCanGuess().get(1);
    Boolean enoughSuspectsTalkedTo = App.verifyCanGuess().get(0);

    // verify if the user can guess.
    if (canGuess) {
      App.openGuess(event);
    } else {

      // update the popup message based on the user's progress
      if(!enoughSuspectsTalkedTo){
        lbPopup.setText("You need to talk to all suspects before making a guess.");
      } else if(!enoughCluesViewed){
        lbPopup.setText("You need to view at least 1 clue before making a guess.");
      }

      // display popup message for 3 seconds
      lbPopup.setVisible(true);
      new java.util.Timer()
          .schedule(
              new java.util.TimerTask() {
                @Override
                public void run() {
                  lbPopup.setVisible(false);
                }
              },
              3000);
    }
  }
}
