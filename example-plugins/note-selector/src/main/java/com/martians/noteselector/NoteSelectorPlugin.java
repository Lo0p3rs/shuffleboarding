package com.martians.noteselector;

import edu.wpi.first.shuffleboard.api.data.DataType;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;

import java.util.List;
import java.util.Map;

import com.martians.noteselector.widget.NoteSelector;


/**
 * An example plugin that provides a custom data type (a 2D point) and a simple widget for viewing such data.
 */
@Description(
    group = "com.martians",
    name = "NoteSelector",
    version = "2024.3.1",
    summary = "A plugin that allows you to select notes for autonomous"
)
public final class NoteSelectorPlugin extends Plugin {



  @Override
  public List<ComponentType> getComponents() {
    return List.of(
        WidgetType.forAnnotatedWidget(NoteSelector.class)
    );
  }


}
