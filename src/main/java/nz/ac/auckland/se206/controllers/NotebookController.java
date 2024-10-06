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

  // FXML annotations to link with the corresponding elements in the FXML file
  @FXML private Pane bookPane;
  @FXML private Pane mainPane;
  @FXML private Label timerLabel;
  @FXML private Rectangle rectangleBook;

  // Method to handle the event when the book is clicked

  @FXML private ImageView item1, item2, item3, item4, item5; // Declare the draggable items

  private double startX;
  private double startY;

  @FXML
  public void initialize() {
    makeDraggable(item1);
    makeDraggable(item2);
    makeDraggable(item3);
    makeDraggable(item4);
    makeDraggable(item5);
    rectangleBook.setDisable(true); // Initially disable the notebook interaction
  }

  @FXML
  private void onClickedBook(MouseEvent event) throws IOException {
    if (!rectangleBook.isDisabled()) {
      System.out.println("Notebook clicked!");
      App.goToPage(event, "notebookpg1");
    } else {
      System.out.println("Notebook is disabled!");
    }
  }

  // Method to handle the event when the "Go Back" button is clicked
  @FXML
  private void onGoBackCrimeScene(ActionEvent event) throws IOException {
    // Play a sound effect for the button click
    App.playSound("button.mp3");
    System.out.println("Go back to crime scene");
    // Navigate back to the crime scene
    App.openCrimeScene(event);
  }

  // Getter method for the timer label
  public Label getTimerLabel() {
    return timerLabel;
  }

  private void makeDraggable(ImageView item) {
    item.setOnMousePressed(
        event -> {
          startX = event.getSceneX();
          startY = event.getSceneY();
        });

    item.setOnMouseDragged(
        event -> {
          double offsetX = event.getSceneX() - startX;
          double offsetY = event.getSceneY() - startY;

          item.setLayoutX(item.getLayoutX() + offsetX);
          item.setLayoutY(item.getLayoutY() + offsetY);

          startX = event.getSceneX();
          startY = event.getSceneY();
        });

    item.setOnMouseReleased(
        event -> {
          checkIfItemsAreOutOfTheWay();
        });
  }

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
      // Still overlapping, keep notebook disabled
      rectangleBook.setDisable(true);
    } else {
      // No overlapping, make the notebook clickable
      rectangleBook.setDisable(false);
    }
  }

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
