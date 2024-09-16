package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

// nz.ac.auckland.se206.controllers.JamesClueController

public class JamesClueController {
  @FXML private Rectangle rectangleButtons;

  @FXML
  private void closeClue(MouseEvent event) throws IOException {
    App.setRoot("laptopClue");
  }
}
