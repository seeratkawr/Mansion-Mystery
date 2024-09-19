package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Timer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
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
import nz.ac.auckland.se206.controllers.SafeController;
import nz.ac.auckland.se206.controllers.SafeKeypadController;
import nz.ac.auckland.se206.controllers.SafeOpenedController;

// this is a test comment to test github flows

/**
 * This is the entry point of the JavaFX application. This class initializes and runs the JavaFX
 * application.
 */
public class App extends Application {

  private static Scene scene;
  private static MediaPlayer mediaPlayer;
  private static Set<String> suspectsTalkedTo = new HashSet<>();
  private static Set<String> cluesViewed = new HashSet<>();
  private static String aiGameResult;
  private static Stage primaryStage;
  private static boolean timerStarted = false;
  private static TimerUtility timer;
  private static Timeline timerCheckTimeline;
  private static List<Timer> activeTimers = new ArrayList<>();
  private static List<Thread> activeThreads = new ArrayList<>();
  private static Label timerLabel;
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
          // ending media players
          if (mediaPlayer != null) {
            System.out.println("Closing media player");
            mediaPlayer.stop();
            mediaPlayer.dispose();
          }

          // ending threads
          if (activeTimers.size() > 0) {
            System.out.println("Closing active timers");
            for (Timer timer : activeTimers) {
              timer.cancel();
            }
          }

          // ending threads for AI
          if (timerCheckTimeline != null) {
            System.out.println("Closing timer check timeline");
            timerCheckTimeline.stop();
          }
        });

    startTimerCheckTask();
  }

  /**
   * Opens the game lost scene.
   *
   * @throws IOException if the FXML file is not found
   */
  public static void openGameLost() throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/gameLost.fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Opens the guessing scene.
   *
   * @throws IOException if the FXML file is not found
   */
  public static void openGuessingScene() throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/guessingscene.fxml"));
    Parent root = loader.load();

    scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Opens the crime scene.
   *
   * @param event the action event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  /**
   * Opens the crime scene.
   *
   * @param event the action event that triggered this method
   * @throws IOException if the FXML file is not found
   */
  public static void openCrimeScene(ActionEvent event) throws IOException {
    // Load the crime scene FXML file
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/crimescene.fxml"));

    // Load the root node from the FXML file
    Parent root = loader.load();
    // Create a new scene with the loaded root node
    scene = new Scene(root);
    // Get the current stage from the event source
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    // Set the new scene on the stage and show it
    stage.setScene(scene);
    stage.show();
    // Push the new scene onto the scene stack
    sceneStack.push(scene);

    // Get the controller associated with the crime scene
    CrimeSceneController controller = loader.getController();

    // Check if the timer has not been started yet
    if (!timerStarted) {
      // Get the timer label from the controller
      Label timerLabel = controller.getTimerLabel();
      // Initialize the timer with 300 seconds and the timer label
      timer = new TimerUtility(300, timerLabel);
      // Set the timer label in the TimerUtilityHandler
      TimerUtilityHandler.setTimer(timer, timerLabel);
      // Start the timer
      timer.start();
      // Mark the timer as started
      timerStarted = true;
    } else {
      // If the timer is already started, just update the timer label
      timerLabel = controller.getTimerLabel();
      TimerUtilityHandler.setTimer(timer, timerLabel);
    }
  }

  /**
   * Opens the map view when a mouse event occurs and sets up the necessary components.
   *
   * @param event the MouseEvent that triggers the opening of the map
   * @param path the path to the background image for the map
   * @throws IOException if there is an error loading the FXML file
   */
  public static void openMap(MouseEvent event, String path) throws IOException {

    // Initaial sound effect for map unfolding
    App.playSound("mapunfolding.mp3");
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/map.fxml"));
    Parent root = loader.load();

    // Set the last scene to the current scene
    MapController mapController = loader.getController();
    mapController.changeBackground(path);
    timerLabel = mapController.getTimerLabel();

    TimerUtilityHandler.setTimer(timer, timerLabel);

    // Create a new scene with the loaded root node
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Opens the drawer by loading the notebook.fxml file and setting up the scene. This method
   * initializes the NotebookController, retrieves the timer label, and sets the timer using the
   * TimerUtilityHandler. It then updates the stage with the new scene.
   *
   * @param event the MouseEvent that triggers the drawer to open
   * @throws IOException if the FXML file cannot be loaded
   */
  public static void openDrawer(MouseEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/notebook.fxml"));
    Parent root = loader.load();

    // Get the controller associated with the notebook scene
    NotebookController notebookController = loader.getController();
    timerLabel = notebookController.getTimerLabel();

    // Set the timer label in the TimerUtilityHandler
    TimerUtilityHandler.setTimer(timer, timerLabel);

    // Create a new scene with the loaded root node
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Navigates to a specific page in the notebook based on the provided FXML file name.
   *
   * @param event the MouseEvent that triggers the navigation
   * @param fxml the name of the FXML file (without extension) to load
   * @throws IOException if the FXML file cannot be loaded
   */
  public static void goToPage(MouseEvent event, String fxml) throws IOException {
    // Load the specified FXML file
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    Parent root = loader.load();

    // Set the timer label based on the specific notebook page
    if (fxml.equals("notebookpg1")) {
      NoteBookpg1Controller noteBookpg1Controller = loader.getController();
      timerLabel = noteBookpg1Controller.getTimerLabel();
      TimerUtilityHandler.setTimer(timer, timerLabel);
    } else if (fxml.equals("notebookpg2")) {
      Notebookpg2Controller notebookpg2Controller = loader.getController();
      timerLabel = notebookpg2Controller.getTimerLabel();
      TimerUtilityHandler.setTimer(timer, timerLabel);
    } else if (fxml.equals("notebookpg3")) {
      Notebookpg3Controller notebookpg3Controller = loader.getController();
      timerLabel = notebookpg3Controller.getTimerLabel();
      TimerUtilityHandler.setTimer(timer, timerLabel);
    }

    // Set the new scene and show it
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Navigates to the drawers view.
   *
   * @param event the ActionEvent that triggers the navigation
   * @throws IOException if the FXML file cannot be loaded
   */
  public static void goToDrawers(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/notebook.fxml"));
    Parent root = loader.load();

    // Get the controller associated with the notebook scene
    NotebookController notebookController = loader.getController();
    timerLabel = notebookController.getTimerLabel();
    TimerUtilityHandler.setTimer(timer, timerLabel);

    // Create a new scene with the loaded root node
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Opens the safe scene when a mouse event is triggered.
   *
   * @param event the MouseEvent that triggers the opening of the safe scene
   * @throws IOException if there is an error loading the FXML file
   */
  public static void openSafe(MouseEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/safe.fxml"));
    Parent root = loader.load();

    // Get the controller associated with the safe scene
    SafeController safeController = loader.getController();
    timerLabel = safeController.getTimerLabel();
    TimerUtilityHandler.setTimer(timer, timerLabel);

    // Create a new scene with the loaded root node
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Opens the "safe opened" scene when a specific mouse event occurs.
   *
   * @param event the MouseEvent that triggers the opening of the "safe opened" scene
   * @throws IOException if there is an error loading the FXML file
   */
  public static void openSafeOpened(MouseEvent event) throws IOException {
    // Load the safe opened FXML file
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/safeOpened.fxml"));
    Parent root = loader.load();

    // Get the controller associated with the safe opened scene
    SafeOpenedController safeOpenedController = loader.getController();
    timerLabel = safeOpenedController.getTimerLabel();
    TimerUtilityHandler.setTimer(timer, timerLabel);

    // Create a new scene with the loaded root node
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Opens the safe keypad scene when a mouse event is triggered.
   *
   * @param event the MouseEvent that triggers the opening of the safe keypad scene
   * @throws IOException if there is an error loading the FXML file
   */
  public static void openSafeKeypad(MouseEvent event) throws IOException {
    // Load the safe keypad FXML file
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/safeKeypad.fxml"));
    Parent root = loader.load();

    // Get the controller associated with the safe keypad scene
    SafeKeypadController safeKeypadController = loader.getController();
    // Set the timer label in the TimerUtilityHandler
    timerLabel = safeKeypadController.getTimerLabel();
    TimerUtilityHandler.setTimer(timer, timerLabel);

    // Create a new scene with the loaded root node
    scene = new Scene(root);
    // Get the current stage from the event source
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    // Set the new scene on the stage and show it
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Opens the laptop scene when a mouse event is triggered.
   *
   * @param event the MouseEvent that triggers the opening of the laptop scene
   * @throws IOException if there is an error loading the FXML file
   */
  public static void openLaptop(MouseEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/laptopClue.fxml"));
    Parent root = loader.load();

    // Get the controller associated with the laptop clue scene
    LaptopClueController laptopClueController = loader.getController();
    timerLabel = laptopClueController.getTimerLabel();
    TimerUtilityHandler.setTimer(timer, timerLabel);

    // Create a new scene with the loaded root node
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Opens a laptop clue scene based on the provided path.
   *
   * @param event the MouseEvent that triggers the opening of the laptop clue scene
   * @param path the path to the FXML file for the laptop clue scene
   * @throws IOException if there is an error loading the FXML file
   */
  public static void openLaptopClue(MouseEvent event, String path) throws IOException {
    // Load the specified FXML file for the laptop clue
    FXMLLoader loader = new FXMLLoader(App.class.getResource(path));
    Parent laptopClueView = loader.load();

    // Set the timer label based on the specific laptop clue
    if (path.equals("/fxml/jamesClue.fxml")) {
      JamesClueController jamesClueController = loader.getController();
      timerLabel = jamesClueController.getTimerLabel();
      TimerUtilityHandler.setTimer(timer, timerLabel);
    } else if (path.equals("/fxml/mariaClue.fxml")) {
      MariaClueController mariaClueController = loader.getController();
      timerLabel = mariaClueController.getTimerLabel();
      TimerUtilityHandler.setTimer(timer, timerLabel);
    } else if (path.equals("/fxml/alexClue.fxml")) {
      AlexClueController alexClueController = loader.getController();
      timerLabel = alexClueController.getTimerLabel();
      TimerUtilityHandler.setTimer(timer, timerLabel);
    }

    // Add the loaded laptop clue view to the laptop pane
    AnchorPane laptopPane =
        (AnchorPane) ((Node) event.getSource()).getScene().lookup("#laptopPane");
    laptopPane.getChildren().add(laptopClueView);
  }

  /**
   * Closes the currently open clue scene.
   *
   * @param event the ActionEvent that triggers the closing of the clue scene
   * @throws IOException if there is an error during the closing process
   */
  public static void closeClue(ActionEvent event) throws IOException {
    openCrimeScene(event);
  }

  /**
   * Plays a sound file in the background to avoid blocking the main application thread.
   *
   * @param soundFileName the name of the sound file to be played. The file should be located in the
   *     "/sounds/" directory within the application's resources.
   * @throws Exception if there is an error loading or playing the sound file.
   */
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

  /**
   * Opens the suspect scene based on the provided FXML file name.
   *
   * @param event the MouseEvent that triggers the opening of the suspect scene
   * @param fxml the name of the FXML file (without extension) to load
   * @throws IOException if the FXML file cannot be loaded
   */
  public static void openSuspect(MouseEvent event, String fxml) throws IOException {
    // Load the specified FXML file for the suspect scene
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    Parent root = loader.load();

    // Set the timer label based on the specific suspect scene
    if (fxml.equals("daughter")) {
      DaughterController daughterController = loader.getController();
      timerLabel = daughterController.getTimerLabel();
      TimerUtilityHandler.setTimer(timer, timerLabel);
    } else if (fxml.equals("kitchen")) {
      KitchenController kitchenController = loader.getController();
      timerLabel = kitchenController.getTimerLabel();
      TimerUtilityHandler.setTimer(timer, timerLabel);
    } else if (fxml.equals("cleaner")) {
      CleanerController cleanerController = loader.getController();
      timerLabel = cleanerController.getTimerLabel();
      TimerUtilityHandler.setTimer(timer, timerLabel);
    }

    // Create a new scene with the loaded root node
    scene = new Scene(root);
    // Get the current stage from the event source
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    // Set the new scene on the stage and show it
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Verifies if the player can make a guess based on the number of suspects talked to and clues
   * viewed.
   *
   * @return a list of booleans indicating the conditions for guessing: - First element: true if 3
   *     suspects have been talked to, false otherwise. - Second element: true if at least 1 clue
   *     has been viewed, false otherwise. - Third element: true if both conditions are met, false
   *     otherwise.
   */
  public static List<Boolean> verifyCanGuess() {
    List<Boolean> result = new ArrayList<>();

    // Check if the player has talked to 3 suspects
    if (suspectsTalkedTo.size() == 3) {
      result.add(true);
    } else {
      result.add(false);
    }

    // Check if the player has viewed at least 1 clue
    if (cluesViewed.size() >= 1) {
      result.add(true);
    } else {
      result.add(false);
    }

    // Check if both conditions are met
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

  /**
   * Sets the result of the AI game.
   *
   * @param result the result of the AI game
   */
  public static void setAiGameResult(String result) {
    aiGameResult = result;
  }

  /**
   * Gets the result of the AI game.
   *
   * @return the result of the AI game
   */
  public static String getAiGameResult() {
    return aiGameResult;
  }

  /**
   * Opens the game over scene.
   *
   * @param event the ActionEvent that triggers the opening of the game over scene
   * @throws IOException if there is an error loading the FXML file
   */
  public static void openGameOver(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/gameOver.fxml"));
    Parent root = loader.load();
    scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Restarts the game by clearing the game state and reinitializing the application.
   *
   * @param event the ActionEvent that triggers the game restart
   * @throws IOException if there is an error during the restart process
   */
  public static void restartGame(ActionEvent event) throws IOException {
    // clear game state
    aiGameResult = null;
    suspectsTalkedTo.clear();
    cluesViewed.clear();
    timer.reset();
    timerStarted = false;

    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    currentStage.close();

    new App().start(new Stage());
  }

  /**
   * Navigates to the last scene based on the provided scene name.
   *
   * @param lastScene the name of the last scene to navigate to
   * @throws IOException if there is an error loading the FXML file
   */
  public static void goLastScene(String lastScene) throws IOException {
    // Load the crime scene FXML file as a fallback
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/crimescene.fxml"));
    Parent root;

    // Determine which scene to load based on the lastScene parameter
    if (lastScene.equals("daughter")) {
      // Load the daughter scene
      FXMLLoader daughterLoader = new FXMLLoader(App.class.getResource("/fxml/daughter.fxml"));
      root = daughterLoader.load();
      DaughterController daughterController = daughterLoader.getController();
      timerLabel = daughterController.getTimerLabel();
      TimerUtilityHandler.setTimer(timer, timerLabel);
    } else if (lastScene.equals("kitchen")) {
      // Load the kitchen scene
      FXMLLoader kitchenLoader = new FXMLLoader(App.class.getResource("/fxml/kitchen.fxml"));
      root = kitchenLoader.load();
      KitchenController kitchenController = kitchenLoader.getController();
      timerLabel = kitchenController.getTimerLabel();
      TimerUtilityHandler.setTimer(timer, timerLabel);
    } else if (lastScene.equals("cleaner")) {
      // Load the cleaner scene
      FXMLLoader cleanerLoader = new FXMLLoader(App.class.getResource("/fxml/cleaner.fxml"));
      root = cleanerLoader.load();
      CleanerController cleanerController = cleanerLoader.getController();
      timerLabel = cleanerController.getTimerLabel();
      TimerUtilityHandler.setTimer(timer, timerLabel);
    } else {
      // Load the crime scene as the default
      root = loader.load();
      CrimeSceneController controller = loader.getController();
      timerLabel = controller.getTimerLabel();
      TimerUtilityHandler.setTimer(timer, timerLabel);
    }

    // Set the new scene and show it
    if (root != null) {
      Scene newScene = new Scene(root);
      primaryStage.setScene(newScene);
      primaryStage.show();
    } else {
      System.err.println("Failed to load the scene root.");
    }
  }

  /**
   * This method is called to add a timer to the list of active timers so that they can be stopped
   * when the application is closed
   *
   * @param timer the timer to add to the list
   */
  public static void addTimer(Timer timer) {
    activeTimers.add(timer);
  }

  /**
   * This method is called to add a thread to the list of active threads so that they can be stopped
   * when the application is closed
   *
   * @param thread the thread to add to the list
   */
  public static void addThread(Thread thread) {
    activeThreads.add(thread);
  }

  /**
   * Starts a task to periodically check the timer status. If the timer has
   * finished, it will either
   * open the guessing scene or the game lost scene based on the game state.
   */
  private void startTimerCheckTask() {
    timerCheckTimeline = new Timeline(
        new KeyFrame(
            Duration.seconds(1),
            event -> {
              if (timer != null && timer.isFinished()) {
                // Handle the case when the timer has finished
                System.out.println("Timer has finished.");
                // You might want to perform specific actions or show a notification
                try {
                  // Check if the player can guess based on the number
                  // of suspects talked to and clues viewed
                  if (verifyCanGuess().get(0).equals(true)
                      && verifyCanGuess().get(1).equals(true)
                      && verifyCanGuess().get(2).equals(true)) {
                    openGuessingScene();
                    System.out.println("Guessing scene opened.");
                  } else {
                    // If the player cannot guess, open the game lost scene
                    openGameLost();
                    System.out.println("Game lost scene opened.");
                    timerCheckTimeline.stop();
                  }
                } catch (IOException e) {
                  // Handle any IO exceptions that occur
                  e.printStackTrace();
                }
              }
            }));
    timerCheckTimeline.setCycleCount(Timeline.INDEFINITE);
    timerCheckTimeline.play();
  }
}
