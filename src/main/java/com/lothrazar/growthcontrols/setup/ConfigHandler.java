package com.lothrazar.growthcontrols.setup;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
//import com.lothrazar.examplemod.ExampleMod;
import com.lothrazar.growthcontrols.ModSaplings;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class ConfigHandler {

  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
  public static ForgeConfigSpec COMMON_CONFIG;
  public static ForgeConfigSpec.BooleanValue TOOLTIPS;


  static {
    initConfig();
  }

  private static void initConfig() {
    COMMON_BUILDER.comment("General settings").push(ModSaplings.MODID);
    TOOLTIPS = COMMON_BUILDER.comment("Testing config Tooltip").define("itemTooltip", true);

    COMMON_BUILDER.pop();
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  public static boolean tooltipsEnabled() {
    return TOOLTIPS.get();
  }


  public static void loadConfig(ForgeConfigSpec spec, Path path) {
    final CommentedFileConfig configData = CommentedFileConfig.builder(path)
        .sync()
        .autosave()
        .writingMode(WritingMode.REPLACE)
        .build();
    configData.load();
    spec.setConfig(configData);
  }
}
