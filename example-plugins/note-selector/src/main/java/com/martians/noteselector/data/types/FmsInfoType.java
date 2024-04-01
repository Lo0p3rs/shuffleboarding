package com.martians.noteselector.data.types;

import edu.wpi.first.shuffleboard.api.data.ComplexDataType;
import com.martians.noteselector.data.fms.Alliance;
import com.martians.noteselector.data.fms.ControlWord;
import com.martians.noteselector.data.fms.FmsInfo;
import com.martians.noteselector.data.fms.MatchType;

import java.util.Map;
import java.util.function.Function;

public final class FmsInfoType extends ComplexDataType<FmsInfo> {

  public static final FmsInfoType Instance = new FmsInfoType();

  private FmsInfoType() {
    super("FMSInfo", FmsInfo.class);
  }

  @Override
  public Function<Map<String, Object>, FmsInfo> fromMap() {
    return FmsInfo::new;
  }

  @Override
  public FmsInfo getDefaultValue() {
    return new FmsInfo("", "", 0, 0, MatchType.NONE, Alliance.RED, 0, ControlWord.fromBits(0));
  }
}
