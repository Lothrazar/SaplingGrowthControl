package com.lothrazar.growthcontrols.setup;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
//import com.lothrazar.examplemod.ExampleMod;
import com.lothrazar.growthcontrols.ModSaplings;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ConfigHandler {

  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
  public static ForgeConfigSpec COMMON_CONFIG;
  public static ForgeConfigSpec.ConfigValue<List<String>> OAK_BIOMES;
  private static ForgeConfigSpec.ConfigValue<List<String>> BIRCH_BIOMES;
  private static ForgeConfigSpec.ConfigValue<List<String>> SPRUCE_BIOMES;
  private static ForgeConfigSpec.ConfigValue<List<String>> ACACIA_BIOMES;
  private static ForgeConfigSpec.ConfigValue<List<String>> JUNGLE_BIOMES;
  public static ForgeConfigSpec.ConfigValue<List<String>> DARKOAK_BIOMES;
  private static final String[] mushrooms = new String[] {
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
  private static final String[] badlands = new String[] {
      "minecraft:eroded_badlands"
      , "minecraft:modified_wooded_badlands_plateau"
      , "minecraft:modified_badlands_plateau"
      , "minecraft:badlands"
      , "minecraft:wooded_badlands_plateau"
      , "minecraft:badlands_plateau"
  };
  private static final String[] oceans = new String[] {
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
  private static final String[] spruce = new String[] {
      "minecraft:taiga"
      , "minecraft:giant_tree_taiga"
      , "minecraft:snowy_tundra"
      , "minecraft:taiga_hills"
      , "minecraft:snowy_taiga"
      , "minecraft:snowy_taiga_hills"
      , "minecraft:giant_tree_taiga_hills" };
  private static final String[] oak = new String[] {
      "minecraft:forest"
      , "minecraft:dark_forest"
      , "minecraft:wooded_mountains"
      , "minecraft:wooded_hills"
      , "minecraft:swamp"
      , "minecraft:swamp_hills"
      , "minecraft:flower_forest"
  };
  private static final String[] birch = new String[] {
      "minecraft:birch_forest"
      , "minecraft:forest"
      , "minecraft:birch_forest_hills"
      , "minecraft:tall_birch_forest"
      , "minecraft:tall_birch_hills" };
  private static final String[] darkoak = new String[] {
      "minecraft:dark_forest"
      , "minecraft:dark_forest_hills"
      , "minecraft:flower_forest"
  };
  private static final String[] jungle = new String[] {
      "minecraft:jungle_edge"
      , "minecraft:jungle"
      , "minecraft:jungle_hills"
      , "minecraft:modified_jungle"
      , "minecraft:bamboo_jungle"
      , "minecraft:bamboo_jungle_hills"
      , "minecraft:modified_jungle_edge"
      , "minecraft:modified_jungle" };
  private static final String[] acacia = new String[] {
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
    //TODO: reverse allcapas and finalstatic
    COMMON_BUILDER.comment("General settings").push(ModSaplings.MODID);
    //    OAK_BIOMES = COMMON_BUILDER.comment("Testing config Tooltip").define("itemTooltip", true);
    OAK_BIOMES = COMMON_BUILDER.comment("List of biomes for sapling").define("minecraft:oak_sapling", Arrays.asList(oak));
    BIRCH_BIOMES = COMMON_BUILDER.comment("List of biomes for sapling").define("minecraft:birch_sapling", Arrays.asList(birch));
    SPRUCE_BIOMES = COMMON_BUILDER.comment("List of biomes for sapling").define("minecraft:spruce_sapling", Arrays.asList(spruce));
    JUNGLE_BIOMES = COMMON_BUILDER.comment("List of biomes for sapling").define("minecraft:jungle_sapling", Arrays.asList(jungle));
    ACACIA_BIOMES = COMMON_BUILDER.comment("List of biomes for sapling").define("minecraft:acacia_sapling", Arrays.asList(acacia));
    DARKOAK_BIOMES = COMMON_BUILDER.comment("List of biomes for sapling").define("minecraft:darkoak_sapling", Arrays.asList(darkoak));
    COMMON_BUILDER.pop();
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  public static List<String> getOakBiomes() {
    return OAK_BIOMES.get();
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
