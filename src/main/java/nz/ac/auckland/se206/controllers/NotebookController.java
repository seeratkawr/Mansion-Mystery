package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;

public class NotebookController {

  @FXML private Pane bookPane;
  @FXML private Pane mainPane;
  @FXML private Label timerLabel;
  @FXML private Rectangle rectangleBook;

  @FXML private ImageView item1;
  @FXML private ImageView item2;
  @FXML private ImageView item3;
  @FXML private ImageView item4;
  @FXML private ImageView item5;

  private double startX;
  private double startY;

  /**
   * Initializes the controller class. This method is called after the FXML fields are injected.
   */
  @FXML
  public void initialize() {
    makeDraggable(item1);
    makeDraggable(item2);
    makeDraggable(item3);
    makeDraggable(item4);
    makeDraggable(item5);
    rectangleBook.setDisable(true); // Initially disable the notebook interaction
  }

  /**
   * Method called when the notebook is clicked. Opens the notebook scene.
   *
   * @param event The mouse event that triggered the method
   * @throws IOException If an I/O error occurs
   */
  @FXML
  private void onClickedBook(MouseEvent event) throws IOException {
    if (!rectangleBook.isDisabled()) {
      System.out.println("Notebook clicked!");
      App.goToPage(event, "notebookpg1");
    } else {
      System.out.println("Notebook is disabled!");
    }
  }

  /**
   * Method called when the go back button is clicked. Goes back to the crime scene.
   *
   * @param event The action event that triggered the method
   * @throws IOException If an I/O error occurs
   */
  @FXML
  private void onGoBackCrimeScene(ActionEvent event) throws IOException {
    App.playSound("button.mp3");
    System.out.println("Go back to crime scene");
    App.openCrimeScene();
  }

  /**
   * Getter for the timer label.
   *
   * @return The timer label
   */
  public Label getTimerLabel() {
    return timerLabel;
  }

  /**
   * Makes an item draggable.
   *
   * @param item The item to make draggable
   */
  private void makeDraggable(ImageView item) {
    item.setOnMouseEntered(
        event -> {
          item.setCursor(javafx.scene.Cursor.MOVE); // Change cursor on hover
        });

    item.setOnMouseExited(
        event -> {
          item.setCursor(javafx.scene.Cursor.DEFAULT); // Reset cursor when not hovering
        });

    item.setOnMousePressed(
        event -> {
          startX = event.getSceneX();
          startY = event.getSceneY();
        });

    item.setOnMouseDragged(
        event -> {
          double offsetX = event.getSceneX() - startX;
          double offsetY = event.getSceneY() - startY;

          // Update layout position of the item
          item.setLayoutX(item.getLayoutX() + offsetX);
          item.setLayoutY(item.getLayoutY() + offsetY);

          // Update starting points for smooth dragging
          startX = event.getSceneX();
          startY = event.getSceneY();
        });

    item.setOnMouseReleased(
        event -> {
          checkIfItemsAreOutOfTheWay();
        });
  }

  /**
   * Checks if any items are overlapping the notebook area and disables the notebook if so.
   */
  private void checkIfItemsAreOutOfTheWay() {
    double notebookMinX = rectangleBook.getLayoutX();
    double notebookMaxX = rectangleBook.getLayoutX() + rectangleBook.getWidth();
    double notebookMinY = rectangleBook.getLayoutY();
    double notebookMaxY = rectangleBook.getLayoutY() + rectangleBook.getHeight();

    // Check if any items overlap the notebook area
    if (isOverlapping(item1, notebookMinX, notebookMaxX, notebookMinY, notebookMaxY)
        || isOverlapping(item2, notebookMinX, notebookMaxX, notebookMinY, notebookMaxY)
        || isOverlapping(item3, notebookMinX, notebookMaxX, notebookMinY, notebookMaxY)
        || isOverlapping(item4, notebookMinX, notebookMaxX, notebookMinY, notebookMaxY)
        || isOverlapping(item5, notebookMinX, notebookMaxX, notebookMinY, notebookMaxY)) {
      rectangleBook.setDisable(true); // Keep notebook disabled
    } else {
      rectangleBook.setDisable(false); // Make the notebook clickable
    }
  }

  /**
   * Checks if an item is overlapping the notebook area.
   *
   * @param item The item to check
   * @param notebookMinX The minimum x-coordinate of the notebook area
   * @param notebookMaxX The maximum x-coordinate of the notebook area
   * @param notebookMinY The minimum y-coordinate of the notebook area
   * @param notebookMaxY The maximum y-coordinate of the notebook area
   * @return True if the item is overlapping the notebook area, false otherwise
   */
  private boolean isOverlapping(
      ImageView item,
      double notebookMinX,
      double notebookMaxX,
      double notebookMinY,
      double notebookMaxY) {
        // Calculate the center of the item
    double itemCenterX = item.getLayoutX() + item.getFitWidth() / 2;
    double itemCenterY = item.getLayoutY() + item.getFitHeight() / 2;

    // Check if the item's center is within the notebook's boundaries
    return (itemCenterX >= notebookMinX
        && itemCenterX <= notebookMaxX
        && itemCenterY >= notebookMinY
        && itemCenterY <= notebookMaxY);
  }
}
