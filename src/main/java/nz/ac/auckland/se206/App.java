package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.Stack;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nz.ac.auckland.se206.controllers.MapController;
import nz.ac.auckland.se206.speech.FreeTextToSpeech;

// this is a test comment to test github flows

/**
 * This is the entry point of the JavaFX application. This class initializes and runs the JavaFX
 * application.
 */
public class App extends Application {

  private static Scene scene;
  private static Scene currentScene;

  private static Stack<Scene> sceneStack = new Stack<>(); // Stack to manage scene history

  /**
   * The main method that launches the JavaFX application.
   *
   * @param args the command line arguments
   */
  public static void main(final String[] args) {
    launch();
  }

  /**
   * Sets the root of the scene to the specified FXML file.
   *
   * @param fxml the name of the FXML file (without extension)
   * @throws IOException if the FXML file is not found
   */
  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  /**
   * Loads the FXML file and returns the associated node. The method expects that the file is
   * located in "src/main/resources/fxml".
   *
   * @param fxml the name of the FXML file (without extension)
   * @return the root node of the FXML file
   * @throws IOException if the FXML file is not found
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * Opens the chat view and sets the profession in the chat controller.
   *
   * @param event the mouse event that triggered the method
   * @param profession the profession to set in the chat controller
   * @throws IOException if the FXML file is not found
   */
  // public static void openChat(MouseEvent event, String profession) throws IOException {
  //   FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/chat.fxml"));
  //   Parent root = loader.load();

  //   // ChatController chatController = loader.getController();
  //   // chatController.setProfession(profession);

  // }

  /**
   * This method is invoked when the application starts. It loads and shows the "room" scene.
   *
   * @param stage the primary stage of the application
   * @throws IOException if the "src/main/resources/fxml/room.fxml" file is not found
   */
  @Override
  public void start(final Stage stage) throws IOException {
    // Initialize the static stage variable
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/startgame.fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
    sceneStack.push(scene);
  }

  // public static void openScene(MouseEvent event, String fxml) throws IOException {
  //   // Save the current scene to the stack
  //   sceneStack.push(currentScene);

  //   // Load and set the new scene
  //   FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
  //   Parent root = loader.load();
  //   currentScene = new Scene(root);
  //   Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
  //   stage.setScene(currentScene);
  //   stage.show();
  // }

  public static void openCrimeScene(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/crimescene.fxml"));

    Parent root = loader.load();
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
    sceneStack.push(scene);
  }

  public static void openMap(MouseEvent event, String path) throws IOException {

    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/map.fxml"));
    Parent root = loader.load();

    MapController mapController = loader.getController();
    mapController.changeBackground(path);

    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void openKitchen(MouseEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/kitchen.fxml"));
    Parent root = loader.load();

    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  private void handleWindowClose(WindowEvent event) {
    FreeTextToSpeech.deallocateSynthesizer();
  }
}
