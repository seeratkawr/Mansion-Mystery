package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.App;

// Controller class for the Laptop Clue scene
public class LaptopClueController {
  @FXML private Button backButton; // Button to go back to the previous scene
  @FXML private Button signInButton; 
  @FXML private Label timerLabel; // Label to display the timer
  @FXML private Label lbPopup;
  @FXML private TextField txtInput;

  // Getter for the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }

  // Method called when the controller is initialized
  @FXML
  public void initialize() {
        txtInput.setOnKeyPressed(
        event -> {
          switch (event.getCode()) {
            case ENTER:
              signInButton.fire(); // Trigger the send button programmatically
              break;
            default:
              break;
          }
        });
  }

  // Method called when the back button is clicked
  @FXML
  private void onGoBack(ActionEvent event) {
    System.out.println("Back button clicked");
    try {
      // Go back to the crime scene
      App.playSound("mouseclick.mp3");
      App.closeClue(event);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onSignIn(ActionEvent event) {
    System.out.println("Sign in button clicked");
    try {
      App.playSound("mouseclick.mp3");
      // Get the username from the text field
      String username = txtInput.getText().toLowerCase();
      System.out.println("typed in: " + username);
      if (username.equals("james")) {
        App.openLaptopClue(event, "/fxml/jamesClue.fxml");
      } else if (username.equals("maria")) {
        App.openLaptopClue(event, "/fxml/mariaClue.fxml");
      } else if (username.equals("alex")) {
        App.openLaptopClue(event, "/fxml/alexClue.fxml");
      } else {
              // Display popup message for 2 seconds
              lbPopup.setVisible(true);
              Timer timer = new Timer();
              App.addTimer(timer); // Store timer in App.java for garbage collection
              timer.schedule(
                  new TimerTask() {
                    @Override
                    public void run() {
                      lbPopup.setVisible(false); // Hide popup message after 2 seconds
                    }
                  },
                  2000);
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
