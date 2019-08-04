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
  public static final String DELIM = "->";
  public static ForgeConfigSpec.ConfigValue<List<String>> GROWABLE_BIOMES;
  private static ForgeConfigSpec.BooleanValue dropFailedGrowth;
  public static ForgeConfigSpec.ConfigValue<List<String>> CROP_BIOMES;
  public static ForgeConfigSpec COMMON_CONFIG;

  public static boolean getdropFailedGrowth() {
    return dropFailedGrowth.get();
  }

  //  private static final String[] badlands = new String[] {
  //      "minecraft:eroded_badlands"
  //      , "minecraft:modified_wooded_badlands_plateau"
  //      , "minecraft:modified_badlands_plateau"
  //      , "minecraft:badlands"
  //      , "minecraft:wooded_badlands_plateau"
  //      , "minecraft:badlands_plateau"
  //  };
  //  private static final String[] oceans = new String[] {
  //      "minecraft:ocean"
  //      , "minecraft:river"
  //      , "minecraft:frozen_ocean"
  //      , "minecraft:frozen_river"
  //      , "minecraft:beach"
  //      , "minecraft:deep_ocean"
  //      , "minecraft:warm_ocean"
  //      , "minecraft:lukewarm_ocean"
  //      , "minecraft:cold_ocean"
  //      , "minecraft:deep_warm_ocean"
  //      , "minecraft:deep_lukewarm_ocean"
  //      , "minecraft:deep_cold_ocean"
  //      , "minecraft:deep_frozen_ocean"
  //  };
  private static final String[] netherwart = new String[] {
      "minecraft:nether"
      , "minecraft:hell"
  };
  //  Beets (Beta vulgaris) are a cool-season, root vegetable, which means they grow in the cool weather of spring and fall.

  static {
    initConfig();
  }

  private static void initConfig() {
    //TODO: reverse allcapas and finalstatic
    COMMON_BUILDER.comment("General settings").push(ModSaplings.MODID);
    //
    dropFailedGrowth = COMMON_BUILDER.comment("Drop sapling item on failed growth").define("dropOnFailedGrow", true);
    //
    List<String> configstuff = new ArrayList<>();
    configstuff.add(Blocks.ACACIA_SAPLING.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:savanna"
        , "minecraft:shattered_savanna"
        , "minecraft:shattered_savanna_plateau"
        , "minecraft:savanna_plateau"
        , "minecraft:modified_wooded_badlands_plateau"
        , "minecraft:wooded_badlands_plateau"
    }));
    configstuff.add(Blocks.BIRCH_SAPLING.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:birch_forest"
        , "minecraft:forest"
        , "minecraft:birch_forest_hills"
        , "minecraft:tall_birch_forest"
        , "minecraft:tall_birch_hills" }));
    configstuff.add(Blocks.SPRUCE_SAPLING.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:taiga"
        , "minecraft:giant_tree_taiga"
        , "minecraft:snowy_tundra"
        , "minecraft:taiga_hills"
        , "minecraft:snowy_taiga"
        , "minecraft:snowy_taiga_hills"
        , "minecraft:giant_tree_taiga_hills" }));
    configstuff.add(Blocks.OAK_SAPLING.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:forest"
        , "minecraft:dark_forest"
        , "minecraft:wooded_mountains"
        , "minecraft:wooded_hills"
        , "minecraft:swamp"
        , "minecraft:swamp_hills"
        , "minecraft:flower_forest"
    }));
    configstuff.add(Blocks.DARK_OAK_SAPLING.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:dark_forest"
        , "minecraft:dark_forest_hills"
        , "minecraft:flower_forest"
    }));
    configstuff.add(Blocks.JUNGLE_SAPLING.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:jungle_edge"
        , "minecraft:jungle"
        , "minecraft:jungle_hills"
        , "minecraft:modified_jungle"
        , "minecraft:bamboo_jungle"
        , "minecraft:bamboo_jungle_hills"
        , "minecraft:modified_jungle_edge"
        , "minecraft:modified_jungle" }));
    //unsupported type: map
    GROWABLE_BIOMES = COMMON_BUILDER.comment("Map growable block to CSV list of biomes no spaces, -> in between.  It SHOULD be fine to add modded saplings. An empty list means the sapling can gro nowhere.  Delete the key-entry for a sapling to let it grow everywhere.")
        .define("SaplingBlockToBiome", configstuff);
    //
    configstuff = new ArrayList<>();
    configstuff.add(Blocks.WHEAT.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:plains"
        , "minecraft:swamp"
        , "minecraft:beach"
    }));
    configstuff.add(Blocks.CARROTS.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:taiga"
        , "minecraft:savanna"
        , "minecraft:savanna_*"
        , "minecraft:*_savanna"
        , "minecraft:shattered_savanna_plateau"
        , "minecraft:sunflower_plains"
        , "minecraft:giant_tree_taiga_hills" }));
    configstuff.add(Blocks.POTATOES.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:taiga"
        , "minecraft:snowy_*"
        , "minecraft:taiga_*"
        , "minecraft:*_forest"
        , "minecraft:dark_forest_hills"
        , "minecraft:*_taiga"
        , "minecraft:*_mountains"
        , "minecraft:giant_tree_taiga_hills"
    }));
    configstuff.add(Blocks.BEETROOTS.getRegistryName().toString() + DELIM + String.join(",", new String[] {
        "minecraft:taiga"
        , "minecraft:forest"
        , "minecraft:swamp"
        , "minecraft:flower_forest"
        , "minecraft:birch_forest"
        , "minecraft:birch_*"
        , "minecraft:tall_birch_forest"
        , "minecraft:tall_birch_hills"
        , "minecraft:dark_forest"
        , "minecraft:nether"
        , "minecraft:hell"
        , "minecraft:dark_forest_hills"
    }));
    final String[] mushrooms = new String[] {
        "minecraft:mushroom_*"
        , "minecraft:mushroom_field_shore"
        , "minecraft:nether"
        , "minecraft:mesa_*"
        , "minecraft:mesa"
        , "minecraft:small_end_islands"
        , "minecraft:end_*"
        , "minecraft:the_end"
        , "minecraft:the_void"
    };
    configstuff.add(Blocks.BROWN_MUSHROOM.getRegistryName().toString() + DELIM + String.join(",", mushrooms));
    configstuff.add(Blocks.RED_MUSHROOM.getRegistryName().toString() + DELIM + String.join(",", mushrooms));
    configstuff.add(Blocks.COCOA.getRegistryName().toString() + DELIM + String.join(",",
        new String[] {
            "minecraft:jungle_edge"
            , "minecraft:jungle"
            , "minecraft:jungle_hills"
            , "minecraft:modified_jungle"
            , "minecraft:bamboo_jungle"
            , "minecraft:bamboo_jungle_hills"
            , "minecraft:modified_jungle_edge"
            , "minecraft:modified_jungle" }
    ));
    configstuff.add(Blocks.MELON_STEM.getRegistryName().toString() + DELIM + String.join(",",
        new String[] {
            "minecraft:ocean"
            , "minecraft:mesa_*"
            , "minecraft:mesa"
            , "minecraft:*_ocean"
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
            , "minecraft:jungle"
            , "minecraft:jungle_*"
            , "minecraft:modified_jungle"
            , "minecraft:bamboo_jungle"
            , "minecraft:bamboo_jungle_hills"
            , "minecraft:modified_jungle_edge"
            , "minecraft:modified_jungle" }
    ));
    configstuff.add(Blocks.PUMPKIN_STEM.getRegistryName().toString() + DELIM + String.join(",",
        new String[] {
            "minecraft:extreme_*"
            // // and "mesa"
            , "minecraft:taiga"
            , "minecraft:snowy_*"
            , "minecraft:taiga_*"
            , "minecraft:dark_forest_hills"
            , "minecraft:*_taiga"
            , "minecraft:giant_tree_taiga_hills" }
    ));
    CROP_BIOMES = COMMON_BUILDER.comment("Map growable block to CSV list of biomes no spaces, -> in between.  It SHOULD be fine to add modded blocks. An empty list means the crop"
        + " can gro nowhere.  Delete the key-entry for a crop to let it grow everywhere.")
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
        final String[] split = splitme.split(DELIM);
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
