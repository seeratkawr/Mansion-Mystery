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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controllers.AlexClueController;
import nz.ac.auckland.se206.controllers.CleanerController;
import nz.ac.auckland.se206.controllers.CrimeSceneController;
import nz.ac.auckland.se206.controllers.DaughterController;
import nz.ac.auckland.se206.controllers.JamesClueController;
import nz.ac.auckland.se206.controllers.KitchenController;
import nz.ac.auckland.se206.controllers.LaptopClueController;
import nz.ac.auckland.se206.controllers.MapController;
import nz.ac.auckland.se206.controllers.MariaClueController;
import nz.ac.auckland.se206.controllers.NoteBookpg1Controller;
import nz.ac.auckland.se206.controllers.NotebookController;
import nz.ac.auckland.se206.controllers.Notebookpg2Controller;
import nz.ac.auckland.se206.controllers.Notebookpg3Controller;

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
  private static boolean timerStarted = false;
  private static TimerUtility timer;

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

    CrimeSceneController controller = loader.getController();

    if (!timerStarted) {
      Label timerLabel = controller.getTimerLabel();
      timer = new TimerUtility(300, timerLabel);
      controller.setTimer(timer);
      timer.start();
      timerStarted = true;
    } else {
      controller.setTimer(timer);
    }
  }

  public static void openMap(MouseEvent event, String path) throws IOException {

    App.playSound("mapunfolding.mp3");
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/map.fxml"));
    Parent root = loader.load();

    MapController mapController = loader.getController();
    mapController.changeBackground(path);

    mapController.setTimer(timer);

    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();

    timer.setTimerLabel(mapController.getTimerLabel());
  }

  public static void openDrawer(MouseEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/notebook.fxml"));
    Parent root = loader.load();

    NotebookController notebookController = loader.getController();
    notebookController.setTimer(timer);

    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void goToPage(MouseEvent event, String fxml) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    Parent root = loader.load();

    if (fxml.equals("notebookpg1")) {
      NoteBookpg1Controller noteBookpg1Controller = loader.getController();
      noteBookpg1Controller.setTimer(timer);
    } else if (fxml.equals("notebookpg2")) {
      Notebookpg2Controller notebookpg2Controller = loader.getController();
      notebookpg2Controller.setTimer(timer);
    } else if (fxml.equals("notebookpg3")) {
      Notebookpg3Controller notebookpg3Controller = loader.getController();
      notebookpg3Controller.setTimer(timer);
    }

    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void goToDrawers(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/notebook.fxml"));
    Parent root = loader.load();

    NotebookController notebookController = loader.getController();
    notebookController.setTimer(timer);

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

    LaptopClueController laptopClueController = loader.getController();
    laptopClueController.setTimer(timer);

    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  public static void openLaptopClue(MouseEvent event, String path) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource(path));
    Parent laptopClueView = loader.load();

    if (path.equals("/fxml/jamesClue.fxml")) {
      JamesClueController jamesClueController = loader.getController();
      jamesClueController.setTimer(timer);
    } else if (path.equals("/fxml/mariaClue.fxml")) {
      MariaClueController mariaClueController = loader.getController();
      mariaClueController.setTimer(timer);
    } else if (path.equals("/fxml/alexClue.fxml")) {
      AlexClueController alexClueController = loader.getController();
      alexClueController.setTimer(timer);
    }

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

    if (fxml.equals("daughter")) {
      DaughterController daughterController = loader.getController();
      daughterController.setTimer(timer);
    } else if (fxml.equals("kitchen")) {
      KitchenController kitchenController = loader.getController();
      kitchenController.setTimer(timer);
    } else if (fxml.equals("cleaner")) {
      CleanerController cleanerController = loader.getController();
      cleanerController.setTimer(timer);
    }

    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  /**
   * This method is called to verify if the player is allowed to guess. The player can guess if they
   * have talked to all suspects and viewed at least one clue.
   *
   * @return true if the player can guess, false otherwise
   */
  public static Boolean verifyCanGuess() {
    if (suspectsTalkedTo.size() == 3 && cluesViewed.size() >= 1) {
      System.out.println("Can guess");
      return true;
    }
    return false;
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
    Parent root = null;

    if (lastScene.equals("daughter")) {
      FXMLLoader daughterLoader = new FXMLLoader(App.class.getResource("/fxml/daughter.fxml"));
      root = daughterLoader.load();
      DaughterController daughterController = daughterLoader.getController();
    } else if (lastScene.equals("kitchen")) {
      FXMLLoader kitchenLoader = new FXMLLoader(App.class.getResource("/fxml/kitchen.fxml"));
      root = kitchenLoader.load();
      KitchenController kitchenController = kitchenLoader.getController();
    } else if (lastScene.equals("cleaner")) {
      FXMLLoader cleanerLoader = new FXMLLoader(App.class.getResource("/fxml/cleaner.fxml"));
      root = cleanerLoader.load();
      CleanerController cleanerController = cleanerLoader.getController();
    } else {
      root = loader.load();
      CrimeSceneController controller = loader.getController();
      controller.setTimer(timer); // Default method call for CrimeSceneController
    }

    if (root != null) {
      Scene newScene = new Scene(root);
      primaryStage.setScene(newScene);
      primaryStage.show();
    } else {
      System.err.println("Failed to load the scene root.");
    }
  }
}
