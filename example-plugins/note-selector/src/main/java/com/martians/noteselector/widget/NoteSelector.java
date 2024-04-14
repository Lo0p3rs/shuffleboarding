package com.martians.noteselector.widget;

import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import javax.swing.JComboBox;

import org.fxmisc.easybind.monadic.MonadicBinding;

import javafx.event.EventHandler;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;  
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import edu.wpi.first.networktables.DoubleArrayPublisher;
import edu.wpi.first.networktables.DoubleArrayTopic;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.PubSubOption;
import edu.wpi.first.networktables.PubSubOptions;
import edu.wpi.first.networktables.StringArrayPublisher;
import edu.wpi.first.networktables.StringArrayTopic;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.networktables.StringTopic;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import com.martians.noteselector.data.fms.FmsInfo;

@Description(
    name = "Note Selector",
    dataTypes = FmsInfo.class,
    summary = "Allows you to select which notes you would like to score in Autonomous"
)
@ParametrizedController("NoteSelector.fxml")
public final class NoteSelector extends SimpleAnnotatedWidget<FmsInfo> {
  //private final Alliance alliance;

  @FXML
  private Pane root;
  @FXML
  public Button anButton, snButton, srnButton, anButtonR, snButtonR, srnButtonR, f1nButton, f2nButton, f3nButton, f4nButton, f5nButton, startButton, clearButton, confirmButton;

  @FXML
  public Slider noteSlider;
  
  @FXML
  public TableView<Item> noteTbl;
  public TableColumn<Item, String> noteNum;
  public TableColumn<Item, String> noteSelec;

  public String[] notes = new String[] {"Note 1", "Note 2", "Note 3", "Note 4", "Note 5", "Note 6", "Note 7", "Note 8"};
  public int arrayVal = 0;
  public int arrayLimit;
  public String[] finalNotes;

  @FXML
  public Rectangle coverPane;
  public Text coverText;

  @FXML
  
  public ObservableList<String> startingPoses = FXCollections.observableArrayList("Starting Amp", "Starting Speaker", "Starting Source");
  public String[] startingPosesArray = new String[] {"Starting Amp", "Starting Speaker", "Starting Source"};
  public ComboBox startingPosChooser = startingPosChooser = new ComboBox();

  //public Label fmsConnect, fmsMatch, dsConnect;
  //public String[] alliances = new String[] {"Red Alliance", "Blue Alliance"};
  public Boolean isBlueAlliance = true;
  //public ComboBox allianceBox;

  public int startingPos;

  public NetworkTableInstance inst2 = NetworkTableInstance.getDefault();
  
  //StringPublisher startingPosition

  @FXML
  public Label connectLabel;

  @FXML
  private void initialize(){

    startingPosChooser.setItems(startingPoses);

    EventHandler<ActionEvent> chooserEvent =
                  new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                String chooserVal = startingPosChooser.getValue().toString();
                if(Objects.equals(chooserVal, startingPoses.toArray()[0])){
                    startingPos = 0;
                } else if(Objects.equals(chooserVal, startingPoses.toArray()[1])){
                    startingPos = 1;
                } else if (Objects.equals(chooserVal, startingPoses.toArray()[2])){
                    startingPos = 2;
                }
            }
        };

    startingPosChooser.setOnAction(chooserEvent);


    if(isBlueAlliance == true){
      anButton.setVisible(true);
      snButton.setVisible(true);
      srnButton.setVisible(true);
      anButtonR.setVisible(false);
      snButtonR.setVisible(false);
      srnButtonR.setVisible(false);
      System.out.println(dataOrDefault.map(this::getAlliance).toString());
    } else {
      anButton.setVisible(false);
      snButton.setVisible(false);
      srnButton.setVisible(false);
      anButtonR.setVisible(true);
      snButtonR.setVisible(true);
      srnButtonR.setVisible(true);
    }
    noteNum.setCellValueFactory(cellData ->new SimpleStringProperty(cellData.getValue().getNoteNum()));
    noteSelec.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNoteSelec()));


    clearButton.setOnAction(event -> {
      arrayVal = 0;
      noteTbl.getItems().clear();
      noteSlider.setDisable(false);
      //removeButtonState(confirmButton);
      coverPane.setVisible(false);
      coverText.setVisible(false);
      confirmButton.setDisable(true);
      startingPosChooser.setDisable(false);
      removeButtonState(startButton);
      /*File noteFile = new File("C:\\note-selector\\notes.txt"); 
      if (noteFile.delete()) { 
        System.out.println("Deleted the file: " + noteFile.getName());
      } else {
        System.out.println("Failed to delete the file.");
      }*/
    });

    startButton.setOnAction(event -> {
      if(inst2.isConnected() == true){      
        arrayLimit = (int) noteSlider.getValue() - 1;
        noteSlider.setDisable(true);
        finalNotes = new String[arrayLimit+1];
        arrayVal = 0;
        startButton.setDisable(true);
        confirmButton.setDisable(true);
        coverPane.setVisible(false);
        coverText.setVisible(false);  
        startingPosChooser.setDisable(true);
        removeButtonState(anButton);
        removeButtonState(snButton);
        removeButtonState(srnButton);
        removeButtonState(anButtonR);
        removeButtonState(snButtonR);
        removeButtonState(srnButtonR);
        removeButtonState(f1nButton);
        removeButtonState(f2nButton);
        removeButtonState(f3nButton);
        removeButtonState(f4nButton);
        removeButtonState(f5nButton); 
        startNT();
        finalNotes[arrayVal] = startingPosesArray[startingPos];
        String[] preloadNames = new String[] {"Amp", "Speaker", "Source"};
        noteTbl.getItems().addAll(new Item("Preload", preloadNames[startingPos]));
        arrayVal += 1;
        if(arrayVal > arrayLimit){
          disableAllNoteButtons();
          confirmButton.setDisable(false);
      }} else{
        System.out.println("NT Not Connected");
      }
      
    });

    confirmButton.setOnAction(event -> {
      coverPane.setVisible(true);
      coverText.setVisible(true);
      disableAllNoteButtons();
      confirmButton.setDisable(true);
      System.out.println(Arrays.toString(finalNotes));
      publishVals(finalNotes);
      /*try {
        writeArray("C:\\note-selector\\notes.txt", finalNotes, startingPosesArray, startingPos);
      } catch (IOException e) {
        System.out.println("Write Failed");
        e.printStackTrace();
      }*/
    });

    initializeButtons("Amp Note", anButton);
    initializeButtons("Speaker Note", snButton);
    initializeButtons("Source Note", srnButton);
    initializeButtons("Far 1 Note", f1nButton);
    initializeButtons("Far 2 Note", f2nButton);
    initializeButtons("Far 3 Note", f3nButton);
    initializeButtons("Far 4 Note", f4nButton);
    initializeButtons("Far 5 Note", f5nButton);
    initializeButtons("Amp Note", anButtonR);
    initializeButtons("Speaker Note", snButtonR);
    initializeButtons("Source Note", srnButtonR);

    /*fmsMatch.textProperty().bind(dataOrDefault.map(this::generateInfoText));

    fmsConnect.textProperty().bind(
        dataOrDefault
            .map(info -> "FMS " + (info.getFmsControlData().isFmsAttached() ? "connected" : "disconnected")));
    dsConnect.textProperty().bind(
        dataOrDefault
            .map(info -> "DriverStation " + (info.getFmsControlData().isDsAttached() ? "connected" : "disconnected")));

    fmsConnect.graphicProperty().bind(
        dataOrDefault
            .map(FmsInfo::getFmsControlData)
            .map(d -> d.isFmsAttached() ? checkMarkLabel() : crossLabel()));
    dsConnect.graphicProperty().bind(
        dataOrDefault
            .map(FmsInfo::getFmsControlData)
            .map(d -> d.isDsAttached() ? checkMarkLabel() : crossLabel()));*/
  
  }

  public void initializeButtons(String noteName, Button button){
    button.setOnAction(event -> {
      noteTbl.getItems().addAll(new Item(notes[arrayVal-1], noteName));
      //finalNotes[arrayVal] = noteName;
      finalNotes[arrayVal] = noteName;
     
      arrayVal += 1;
      button.setDisable(true);
      
      if(arrayVal > arrayLimit){
          disableAllNoteButtons();
          confirmButton.setDisable(false);
      }
    });
  }

  public void removeButtonState(Button button){
    button.setDisable(false);
  }

  public void disableAllNoteButtons(){
    anButton.setDisable(true);
    snButton.setDisable(true);
    srnButton.setDisable(true);
    anButtonR.setDisable(true);
    snButtonR.setDisable(true);
    srnButtonR.setDisable(true);
    f1nButton.setDisable(true);
    f2nButton.setDisable(true);
    f3nButton.setDisable(true);
    f4nButton.setDisable(true);
    f5nButton.setDisable(true);
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

public void startNT (){
  System.out.println("Im a useless function");
}

public void publishVals(String[] notePath){
  NetworkTableInstance inst = NetworkTableInstance.getDefault();
  NetworkTable noteTable = inst.getTable("NoteTable");
  StringArrayTopic strtopic = noteTable.getStringArrayTopic("AutoNotes");
  StringArrayPublisher strPub = strtopic.publish(PubSubOption.keepDuplicates(false));
  strPub.set(notePath);
  strPub.close();
  inst.close();
  System.out.println("Published NT Values");
}


public static void writeArray (String[] array, String[] startingPos, int x){
  /*BufferedWriter outputWriter = null;
  outputWriter = new BufferedWriter(new FileWriter(filename));
  outputWriter.write(startingPos[x]);
  outputWriter.newLine();
  for (int i = 0; i < array.length; i++) {
    outputWriter.write(array[i]);
    outputWriter.newLine();
  }
  outputWriter.flush();  
  outputWriter.close();  */
}


private Label crossLabel() {
  Label label = new Label("✖");
  label.setStyle("-fx-text-fill: red;");
  return label;
}

private Label checkMarkLabel() {
  Label label = new Label("✔");
  label.setStyle("-fx-text-fill: green;");
  return label;
}

  @Override
  public Pane getView() {
    return root;
    //return fieldGrid;
  }

  public String getAlliance (FmsInfo info){
    return info.getAlliance().toString();
  }
  
  private String generateInfoText(FmsInfo info) {
    return String.format("%s %s Match: %d",
        info.getEventName(),
        info.getMatchType().getHumanReadableName(),
        info.getMatchNumber());
  }


}
