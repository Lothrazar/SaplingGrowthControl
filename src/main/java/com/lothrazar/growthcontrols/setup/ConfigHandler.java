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
  String[] mushrooms = new String[] {
      "minecraft:mushroom_fields"
      , "minecraft:mushroom_field_shore"
      , "minecraft:nether"
      , "minecraft:small_end_islands"
      , "minecraft:end_midlands"
      , "minecraft:end_highlands"
      , "minecraft:end_barrens"
      , "minecraft:the_end"
      , "minecraft:the_void"
  };
  String[] badlands = new String[] {
      "minecraft:eroded_badlands"
      , "minecraft:modified_wooded_badlands_plateau"
      , "minecraft:modified_badlands_plateau"
      , "minecraft:badlands"
      , "minecraft:wooded_badlands_plateau"
      , "minecraft:badlands_plateau"
  };
  String[] oceans = new String[] {
       "minecraft:ocean"
      , "minecraft:river"
      , "minecraft:frozen_ocean"
      , "minecraft:frozen_river"
      , "minecraft:beach"
      , "minecraft:deep_ocean"
      , "minecraft:warm_ocean"
      , "minecraft:lukewarm_ocean"
      , "minecraft:cold_ocean"
      , "minecraft:deep_warm_ocean"
      , "minecraft:deep_lukewarm_ocean"
      , "minecraft:deep_cold_ocean"
      , "minecraft:deep_frozen_ocean"
  };
  String[] spruce = new String[] {
      "minecraft:taiga"
      , "minecraft:giant_tree_taiga"
      , "minecraft:snowy_tundra"
      , "minecraft:taiga_hills"
      , "minecraft:snowy_taiga"
      , "minecraft:snowy_taiga_hills"
      , "minecraft:giant_tree_taiga_hills" };
  String[] oak = new String[] {
      "minecraft:forest"
      , "minecraft:dark_forest"
      , "minecraft:wooded_mountains"
      , "minecraft:wooded_hills"
      , "minecraft:swamp"
      , "minecraft:swamp_hills"
      , "minecraft:flower_forest"
  };
  String[] birch = new String[] {
      "minecraft:birch_forest"
      , "minecraft:forest"
      , "minecraft:birch_forest_hills"
      , "minecraft:tall_birch_forest"
      , "minecraft:tall_birch_hills" };
  String[] darkoak = new String[] {
      "minecraft:dark_forest"
      , "minecraft:dark_forest_hills"
      , "minecraft:flower_forest"
  };
  String[] jungle = new String[] {
      "minecraft:jungle_edge"
      , "minecraft:jungle"
      , "minecraft:jungle_hills"
      , "minecraft:modified_jungle"
      , "minecraft:bamboo_jungle"
      , "minecraft:bamboo_jungle_hills"
      , "minecraft:modified_jungle_edge"
      , "minecraft:modified_jungle" };
  String[] acacia = new String[] {
      "minecraft:savanna"
      , "minecraft:shattered_savanna"
      , "minecraft:shattered_savanna_plateau"
      , "minecraft:savanna_plateau"
      , "minecraft:modified_wooded_badlands_plateau"
      , "minecraft:wooded_badlands_plateau"
  };

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
