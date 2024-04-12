package com.martians.noteselector;

import edu.wpi.first.shuffleboard.api.data.DataType;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;

import java.util.List;
import java.util.Map;

import com.martians.noteselector.widget.NoteSelector;
import edu.wpi.first.shuffleboard.api.plugin.Requires;

@Requires(group = "edu.wpi.first.shuffleboard", name = "Base", minVersion = "1.0.0")
@Description(
    group = "com.martians",
    name = "NoteSelector",
    version = "1.0",
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
