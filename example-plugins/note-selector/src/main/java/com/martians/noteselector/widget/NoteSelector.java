package com.martians.noteselector.widget;

import java.util.Arrays;
import java.util.HashMap;

import com.martians.noteselector.widget.NoteSelector.Item;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.PubSubOption;
import edu.wpi.first.networktables.StringArrayPublisher;
import edu.wpi.first.networktables.StringArrayTopic;

import edu.wpi.first.shuffleboard.api.data.types.NoneType;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

@Description(name = "Note Selector", dataTypes = NoneType.class, // DataType is NoneType becuase we do not require a
                                                                 // data binding
    summary = "Allows you to select which notes you would like to score in Autonomous")

@ParametrizedController("NoteSelector.fxml") // FXML file for the widget, Located in
                                             // /src/main/resources/com/martians/noteselector/widget/NoteSelector.fxml
public final class NoteSelector extends SimpleAnnotatedWidget<NoneType> { // We extend SimpleAnnotatedWidget becuase we
                                                                          // don't have any sources, so no need to
                                                                          // extend AbstractWidget

  @FXML
  private Pane root;

  @FXML
  public Button anButton, snButton, srnButton, f1nButton, f2nButton, f3nButton, f4nButton, f5nButton, startButton,
      clearButton, confirmButton;
  public Button[] noteButtons = new Button[8];

  @FXML
  public Slider noteSlider;

  @FXML
  public TableView<Item> noteTbl; // Table for the notes
  public TableColumn<Item, String> noteNum;
  public TableColumn<Item, String> noteSelec;

  @FXML
  public Rectangle coverPane;
  public Text coverText;

  @FXML
  public ObservableList<String> startingPoses = FXCollections.observableArrayList("Starting Amp", "Starting Speaker",
      "Starting Source"); // ObservableList for the starting positions
  public ComboBox startingPosChooser = new ComboBox();

  @FXML
  public Label fmsLabel;

  // Note Stuff
  public String startingPos;
  public int arrayVal = 0;
  public String[] noteOrderSelected;
  public HashMap<Button, String> noteMap = new HashMap<>(); // HashMap for the note buttons and their corresponding note
                                                            // names (e.g. anButton -> "Amp Note")

  // Network Table Stuff
  public NetworkTableInstance inst = NetworkTableInstance.getDefault(); // Create a NetworkTable Instance
  public NetworkTable noteTable = inst.getTable("NoteTable"); // Get the table from the NetworkTable Instance
  public StringArrayTopic strtopic;
  public StringArrayPublisher strPub;

  @SuppressWarnings("unchecked")

  @FXML
  private void initialize() {

    fmsCheck(); // function to check if the FMS is connected, Line 108
    startingPosChooser.setItems(startingPoses); // Add the starting positions to the ComboBox

    EventHandler<ActionEvent> chooserEvent = // Event Handler checks whether the user has selected a starting position
                                             // from the ComboBox
        new EventHandler<ActionEvent>() {
          public void handle(ActionEvent e) {
            String chooserVal = startingPosChooser.getValue().toString();
            switch (chooserVal) {
              case "Starting Amp":
                startingPos = startingPoses.toArray()[0].toString();
                break;
              case "Starting Speaker":
                startingPos = startingPoses.toArray()[1].toString();
                break;
              case "Starting Source":
                startingPos = startingPoses.toArray()[2].toString();
                break;
              default:
                System.out.println("Invalid Choice");
                break;
            }
          }
        };

    startingPosChooser.setOnAction(chooserEvent);

    noteNum.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNoteNum()));
    noteSelec.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNoteSelec()));

    startButton.setOnAction(event -> { // Event Handler for the start button
      arrayVal = 0;
      if (fmsCheck() == true) {

        updateMap(); // Update the HashMap with the note buttons and their corresponding note names
        StartNT(); // Start the Network Table Instance

        noteOrderSelected = new String[(int) noteSlider.getValue()];
        noteOrderSelected[arrayVal] = startingPos;

        switch (startingPos) {
          case "Starting Amp":
            noteTbl.getItems().addAll(new Item("Preload", "Amp"));
            arrayVal += 1;
            break;
          case "Starting Speaker":
            noteTbl.getItems().addAll(new Item("Preload", "Speaker"));
            arrayVal += 1;
            break;
          case "Starting Source":
            noteTbl.getItems().addAll(new Item("Preload", "Source"));
            arrayVal += 1;
            break;
          default:
            break;
        }

        // Disable and enable a bunch of FXML elements
        startButton.setDisable(true);
        confirmButton.setDisable(true);
        coverPane.setVisible(false);
        coverText.setVisible(false);
        startingPosChooser.setDisable(true);
        noteSlider.setDisable(true);
        for (Button button : noteButtons) {
          button.setDisable(false);
        }

        arrayLimitCheck();

      }
    });

    confirmButton.setOnAction(event -> { // Event Handler for the confirm button

      publishVals(noteOrderSelected); // Publish the selected notes to the Network Table

      for (Button button : noteButtons) {
        button.setDisable(true);
      }
      coverPane.setVisible(true);
      coverText.setVisible(true);
      confirmButton.setDisable(true);
      System.out.println(Arrays.toString(noteOrderSelected));

    });

    clearButton.setOnAction(event -> { // Event Handler for the clear button
      noteTbl.getItems().clear(); // Clear the table
      arrayVal = 0;
      for (Button button : noteButtons) {
        button.setDisable(false);
      }
      confirmButton.setDisable(true);
      startButton.setDisable(false);
      startingPosChooser.setDisable(false);
      noteSlider.setDisable(false);
      coverPane.setVisible(false);
      coverText.setVisible(false);
    });

    initializeButtons();
  }

  public void initializeButtons() { // For each button in the noteButtons array, set the onAction event to add the
                                    // note to the table and the noteOrderSelected array
    fillBtnArr();
    for (Button button : noteButtons) {
      button.setOnAction(event -> {
        noteTbl.getItems().addAll(new Item("Note " + (arrayVal), noteMap.get(button)));
        noteOrderSelected[arrayVal] = noteMap.get(button);

        arrayVal += 1;
        button.setDisable(true);

        arrayLimitCheck();

      });
    }
  }

  public void updateMap() {
    noteMap.put(anButton, "Amp Note");
    noteMap.put(snButton, "Speaker Note");
    noteMap.put(srnButton, "Source Note");
    noteMap.put(f1nButton, "Far 1 Note");
    noteMap.put(f2nButton, "Far 2 Note");
    noteMap.put(f3nButton, "Far 3 Note");
    noteMap.put(f4nButton, "Far 4 Note");
    noteMap.put(f5nButton, "Far 5 Note");
  }

  public void arrayLimitCheck() { // Check if the # of noted in the array is greater than the # of notes for auto
    if (arrayVal > (int) noteSlider.getValue() - 1) {
      for (Button button : noteButtons) {
        button.setDisable(true);
      }
      confirmButton.setDisable(false);
    }
  }

  public boolean fmsCheck() {
    if (inst.isConnected() == true) {
      fmsLabel.setVisible(false);
      return true;
    } else {
      fmsLabel.setVisible(true);
      return false;
    }
  }

  public void StartNT() {
    inst = NetworkTableInstance.getDefault();
    strtopic = noteTable.getStringArrayTopic("AutoNotes"); // Create a Network Table Topic for the notes
    strPub = strtopic.publish(PubSubOption.keepDuplicates(true)); // Create a Network Table Publisher for the notes
  }

  public void publishVals(String[] notePath) {
    strPub.set(notePath);
    strPub.close();
    inst.disconnect(); // Disconnect the Network Table Instance. If the instance is not disconnected or
                       // closed, weird behavior may occur
    System.out.println("Published NT Values");
  }

  public void fillBtnArr() {
    noteButtons[0] = anButton;
    noteButtons[1] = snButton;
    noteButtons[2] = srnButton;
    noteButtons[3] = f1nButton;
    noteButtons[4] = f2nButton;
    noteButtons[5] = f3nButton;
    noteButtons[6] = f4nButton;
    noteButtons[7] = f5nButton;
  }

  public class Item { // Class for the table items

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

  @Override
  public Pane getView() { // Return the root pane, which is the main pane in the FXML file
    return root;
  }

}
