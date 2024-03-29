package com.martians.noteselector.widget;

import javax.swing.plaf.basic.BasicToggleButtonUI;

import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


@Description(
    name = "Note Selector",
    dataTypes = Boolean.class,
    summary = "Allows you to select which notes you would like to score in Autonomous"
)
@ParametrizedController("NoteSelector.fxml")
public final class NoteSelector extends SimpleAnnotatedWidget<Boolean> {

  @FXML
  private Pane root;
  private Stage stage;
  private HBox Controls = new HBox();

  //private Image image = new Image("field.png", true);
   BackgroundImage myBI= new BackgroundImage(new Image("field.png", 500, 500, true, true),
        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
          BackgroundSize.DEFAULT);
  @FXML
  private void initialize() {
    // Bind the text in the labels to the data
    // If you are unfamiliar with the -> notation used here, read the Oracle tutorial on lambda expressions:
    // https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
    //xCoordinateView.textProperty().bind(dataOrDefault.map(point -> point.getX()).map(x -> "X: " + x));
    //yCoordinateView.textProperty().bind(dataOrDefault.map(point -> point.getY()).map(y -> "Y: " + y));
    root.setBackground(new Background(myBI));
    /*StackPane stack = new StackPane(
      new Button("Hello StackPane")
    );*/
    ToggleButton toggle = new ToggleButton("Toggle");
    Controls.getChildren().add(toggle);
    
    
    
  }

  @Override
  public Pane getView() {
    return root;
  }
}
