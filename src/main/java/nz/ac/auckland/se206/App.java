package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controllers.MapController;
import java.util.List;
import java.util.ArrayList;

// this is a test comment to test github flows

/**
 * This is the entry point of the JavaFX application. This class initializes and runs the JavaFX
 * application.
 */
public class App extends Application {

  private static Scene scene;
  private static MediaPlayer
      mediaPlayer; // media play stored at class level to prevent garbage collection
  private static Set<String> suspectsTalkedTo = new HashSet<>();
  private static Set<String> cluesViewed = new HashSet<>();
  private static String aiGameResult;
  private static Stage primaryStage;

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
   * This method is invoked when the application starts. It loads and shows the "room" scene.
   *
   * @param stage the primary stage of the application
   * @throws IOException if the "src/main/resources/fxml/room.fxml" file is not found
   */
  @Override
  public void start(final Stage stage) throws IOException {
    // Initialize the static stage variable
    primaryStage = stage;
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/startgame.fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
    sceneStack.push(scene);

    // ending any remaining threads on close
    stage.setOnCloseRequest(
        e -> {
          if (mediaPlayer != null) {
            System.out.println("Closing media player");
            mediaPlayer.stop();
            mediaPlayer.dispose();
          }
        });
  }

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

    App.playSound("mapunfolding.mp3");
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/map.fxml"));
    Parent root = loader.load();

    MapController mapController = loader.getController();
    mapController.changeBackground(path);

    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void openDrawer(MouseEvent event) throws IOException {

    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/notebook.fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void goToPage(MouseEvent event, String fxml) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void goToDrawers(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/notebook.fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void openSafe(MouseEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/safe.fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void openSafeOpened(MouseEvent event) throws IOException {

    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/safeOpened.fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void openSafeKeypad(MouseEvent event) throws IOException {

    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/safeKeypad.fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void openGuess(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/guessingscene.fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void openLaptop(MouseEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/laptopClue.fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void openLaptopClue(MouseEvent event, String path) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource(path));
    Parent laptopClueView = loader.load();
    AnchorPane laptopPane =
        (AnchorPane) ((Node) event.getSource()).getScene().lookup("#laptopPane");
    laptopPane.getChildren().add(laptopClueView);
  }

  public static void closeClue(ActionEvent event) throws IOException {
    openCrimeScene(event);
  }

  public static void playSound(String soundFileName) {
    try {

      // Create a background task to play the sound to prevent blocking the application thread
      Task<Void> backgroundTask =
          new Task<>() {
            @Override
            protected Void call() {

              // Construct the file path to your sound file
              String soundPath = App.class.getResource("/sounds/" + soundFileName).toExternalForm();

              // Create a Media object with the sound file
              Media sound = new Media(soundPath);

              // Create a MediaPlayer to play the sound
              mediaPlayer = new MediaPlayer(sound);

              // Play the sound
              mediaPlayer.play();
              return null;
            }
          };
      Thread backgroundThread = new Thread(backgroundTask);
      backgroundThread.setDaemon(true);
      backgroundThread.start();
    } catch (Exception e) {
      System.out.println("Error loading sound file: " + e.getMessage());
    }
  }

  public static void openSuspect(MouseEvent event, String fxml) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  /**
   * This method is called to verify if the player is allowed to guess. The player can guess if they
   * have talked to all suspects and viewed at least one clue.
   *
   * @return a list of booleans indicating if the player has talked to all suspects, viewed at least
   *         one clue, and can guess respectively.
   *         The list format is Boolean [enoughSuspectsTalkedTo, enoughCluesViewed, canGuess]
   */
  public static List<Boolean> verifyCanGuess() {
    List<Boolean> result = new ArrayList<Boolean>();

    if(suspectsTalkedTo.size() == 3) {
      System.out.println("Enough suspects talked to");
      result.add(true);
    } else {
      System.out.println("Not enough suspects talked to");
      result.add(false);
    }

    if(cluesViewed.size() >= 1) {
      System.out.println("Enough clues viewed");
      result.add(true);
    } else {
      System.out.println("Not enough clues viewed");
      result.add(false);
    }

    if (suspectsTalkedTo.size() == 3 && cluesViewed.size() >= 1) {
      System.out.println("Can guess");
      result.add(true);
    } else {
      System.out.println("Cannot guess");
      result.add(false);
    }

    return result;
  }

  /**
   * This method is called to add a suspect to the list of suspects talked to
   *
   * @param suspect the suspect to add to the list
   */
  public static void addSuspectTalkedTo(String suspect) {
    suspectsTalkedTo.add(suspect);
    System.out.println(suspectsTalkedTo);
  }

  /**
   * This method is called to add a clue to the list of clues viewed
   *
   * @param clue the clue to add to the list
   */
  public static void addCluesViewed(String clue) {
    cluesViewed.add(clue);
    System.out.println(cluesViewed);
  }

  public static void setAiGameResult(String result) {
    aiGameResult = result;
  }

  public static String getAiGameResult() {
    return aiGameResult;
  }

  public static void openGameOver(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/gameOver.fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void restartGame(ActionEvent event) throws IOException {
    // clear game state
    aiGameResult = null;
    suspectsTalkedTo.clear();
    cluesViewed.clear();

    // open the start game scene
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/startgame.fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void goLastScene(String lastScene) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/crimescene.fxml"));

    if (lastScene.equals("daughter")) {
      loader = new FXMLLoader(App.class.getResource("/fxml/daughter.fxml"));
    } else if (lastScene.equals("kitchen")) {
      loader = new FXMLLoader(App.class.getResource("/fxml/kitchen.fxml"));
    } else if (lastScene.equals("cleaner")) {
      loader = new FXMLLoader(App.class.getResource("/fxml/cleaner.fxml"));
    }

    Parent root = loader.load();
    Scene newScene = new Scene(root);
    primaryStage.setScene(newScene);
    primaryStage.show();
  }
}
