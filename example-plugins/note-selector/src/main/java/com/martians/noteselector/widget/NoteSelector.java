package com.martians.noteselector.widget;

import java.util.concurrent.Callable;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
  @FXML
  public Button anButton, snButton, srnButton, f1nButton, f2nButton, f3nButton, f4nButton, f5nButton, startButton;

  @FXML
  public Slider noteSlider;
  
  @FXML
  public TableView<Item> noteTbl;
  public TableColumn<Item, String> noteNum;
  public TableColumn<Item, String> noteSelec;

  public String[] notes = new String[] {"Note 1", "Note 2", "Note 3", "Note 4", "Note 5", "Note 6", "Note 7", "Note 8"};
  public int arrayVal;
  public int arrayLimit;


  @FXML
  private void initialize() {
    root.setBackground(new Background( new BackgroundFill(Color.WHITE, null, null)));

    noteNum.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getNoteNum()));
    noteSelec.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNoteSelec()));

    startButton.setOnAction(event -> {
      arrayVal = 0;
      noteTbl.getItems().clear();
      removeButtonState(anButton);
      removeButtonState(snButton);
      removeButtonState(srnButton);
      removeButtonState(f1nButton);
      removeButtonState(f2nButton);
      removeButtonState(f3nButton);
      removeButtonState(f4nButton);
      removeButtonState(f5nButton);
      arrayLimit = (int) noteSlider.getValue(); //TODO: move to proper start button
    });

    initializeButtons("Amp Note", anButton);
    initializeButtons("Speaker Note", snButton);
    initializeButtons("Source Note", srnButton);
    initializeButtons("Far 1 Note", f1nButton);
    initializeButtons("Far 2 Note", f2nButton);
    initializeButtons("Far 3 Note", f3nButton);
    initializeButtons("Far 4 Note", f4nButton);
    initializeButtons("Far 5 Note", f5nButton);

    startButton.setOnMouseDragOver(event -> {
       
    });

    arrayLimit = (int) noteSlider.getValue();
  
  }

  public class Item {

    private final String noteNum;
    private final String noteSelec;


    public Item(String noteNum, String noteSelec) {
        this.noteNum = noteNum;
        this.noteSelec = noteSelec;
    }

    public String getNoteSelec() {
        return noteSelec;
    }

    public String getNoteNum() {
        return noteNum;
    }
}

  public void initializeButtons(String noteName, Button button){
    button.setOnAction(event -> {
      noteTbl.getItems().addAll(new Item(notes[arrayVal], noteName));
      arrayVal += 1;
      button.setDisable(true);

      if(arrayVal == arrayLimit){
          disableAllButtons();
      }
    });
  }

  public void removeButtonState(Button button){
    button.setDisable(false);
  }

  public void disableAllButtons(){
    anButton.setDisable(true);
    snButton.setDisable(true);
    srnButton.setDisable(true);
    f1nButton.setDisable(true);
    f2nButton.setDisable(true);
    f3nButton.setDisable(true);
    f4nButton.setDisable(true);
    f5nButton.setDisable(true);
  }

  @Override
  public Pane getView() {
    return root;
    //return fieldGrid;
  }


}
