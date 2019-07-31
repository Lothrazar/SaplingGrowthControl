package com.lothrazar.growthcontrols.setup;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
//import com.lothrazar.examplemod.ExampleMod;
import com.lothrazar.growthcontrols.ModSaplings;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigHandler {

  private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
  public static ForgeConfigSpec.ConfigValue<List<String>> GROWABLE_BIOMES;
  public static ForgeConfigSpec.BooleanValue dropFailedGrowth;
  public static ForgeConfigSpec.ConfigValue<List<String>> CROP_BIOMES;
  public static ForgeConfigSpec COMMON_CONFIG;
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
  private static final String[] wheat = new String[] {
      "minecraft:plains"
      , "minecraft:swamp"
  };

  static {
    initConfig();
  }

  private static void initConfig() {
    //TODO: reverse allcapas and finalstatic
    COMMON_BUILDER.comment("General settings").push(ModSaplings.MODID);
    System.out.println(Blocks.BIRCH_SAPLING.getRegistryName().toString());
    List<String> configstuff = new ArrayList<>();
    configstuff.add(Blocks.ACACIA_SAPLING.getRegistryName().toString() + "->" + String.join(",", acacia));
    configstuff.add(Blocks.BIRCH_SAPLING.getRegistryName().toString() + "->" + String.join(",", birch));
    configstuff.add(Blocks.SPRUCE_SAPLING.getRegistryName().toString() + "->" + String.join(",", spruce));
    configstuff.add(Blocks.OAK_SAPLING.getRegistryName().toString() + "->" + String.join(",", oak));
    configstuff.add(Blocks.DARK_OAK_SAPLING.getRegistryName().toString() + "->" + String.join(",", darkoak));
    configstuff.add(Blocks.JUNGLE_SAPLING.getRegistryName().toString() + "->" + String.join(",", jungle));
    //unsupported type: map
    GROWABLE_BIOMES = COMMON_BUILDER.comment("Map growable block to CSV list of biomes no spaces, -> in between.  It SHOULD be fine to add modded saplings. An empty list means the sapling can gro nowhere.  Delete the key-entry for a sapling to let it grow everywhere.")
        .define("SaplingBlockToBiome", configstuff);
    dropFailedGrowth =COMMON_BUILDER.comment("Drop sapling item on failed growth").define("dropOnFailedGrow", true);

    //
    configstuff = new ArrayList<>();
    configstuff.add(Blocks.WHEAT.getRegistryName().toString() + "->" + String.join(",", jungle));

    CROP_BIOMES = COMMON_BUILDER.comment("Map growable block to CSV list of biomes no spaces, -> in between.  It SHOULD be fine to add modded saplings. An empty list means the sapling can gro nowhere.  Delete the key-entry for a sapling to let it grow everywhere.")
        .define("CropBlockToBiome", configstuff);

    //YES: it is here actually
    COMMON_BUILDER.pop();
    COMMON_CONFIG = COMMON_BUILDER.build();
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


  public static Map<String, List<String>> getMapBiome(ForgeConfigSpec.ConfigValue<List<String>> conf) {
    final Map<String, List<String>> mapInit = new HashMap<>();
    for (String splitme : conf.get()) {
      try {
        final String[] split = splitme.split("->");
        final String blockId = split[0];
        final String[] biomes = split[1].split(",");
        mapInit.put(blockId, Arrays.asList(biomes));
      }
      catch (Exception e) {
        //bad config sucks to be you
        ModSaplings.LOGGER.error("Error reading bad config value :" + splitme, e);
      }
    }//TODO: DONT BE LAZY

    return mapInit;
  }
}
