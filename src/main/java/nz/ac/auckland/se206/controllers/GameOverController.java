package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.App;

public class GameOverController {

  @FXML private ResourceBundle resources;
  @FXML private URL location;
  @FXML private TextArea txtaResults;

  private String result;

  /** Initializes the controller class. */
  @FXML
  private void initialize() {
    result = App.getAiGameResult();
    txtaResults.setText(result);
  }

  /**
   * Restarts the game
   *
   * @param event
   * @throws IOException
   */
  @FXML
  private void onClickedRestart(ActionEvent event) throws IOException {
    App.playSound("button.mp3");
    App.restartGame(event);
  }
}
