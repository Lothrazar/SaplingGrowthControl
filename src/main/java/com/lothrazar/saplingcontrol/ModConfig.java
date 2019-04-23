package com.lothrazar.saplingcontrol;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Configuration;

public class ModConfig {

  private static final String oak = "minecraft:sapling:0";
  private static final String spruce = "minecraft:sapling:1";
  private static final String birch = "minecraft:sapling:2";
  private static final String jungle = "minecraft:sapling:3";
  private static final String acacia = "minecraft:sapling:4";
  private static final String darkoak = "minecraft:sapling:5";
  static String hillsSet = String.join(",", spruce, "minecraft:potatoes");
  static String forestSet = String.join(",", oak, "minecraft:pumkpin_stem");
  static String jungleSet = String.join(",", jungle, "minecraft:melon_stem", "minecraft:cocoa");
  static String plainsSet = String.join(",", "minecraft:wheat", "minecraft:carrots");
  static String desertSet = String.join(",", "minecraft:cactus");
  static String riverBeachSet = String.join(",", "minecraft:reeds");
  static String endSet = String.join(",", "minecraft:chorus_flower", "minecraft:chorus_plant", "endercrop:ender_crop");
  private static boolean logSpamEverything;

  public static boolean spam() {
    return logSpamEverything;
  }

  public static void loadConfig(Configuration config) {
    config.load();
    String category = ModSaplings.MODID;
    //    disableBonemeal = config.getBoolean("disableBonemeal", category, true, "If true, all bonemeal use on any block, "
    //        + "sapling or crop or anything, is completely blocked.  ");
    logSpamEverything = config.getBoolean("logSpamEverything", category, false, "If true, lots of events and data are logged.  Useful for debugging config values and building modpacks.  ");
    ModConfig.dropBlockOnDeny = config.getBoolean("dropBlockOnDeny", category, true, "If true, then whenever sapling growth "
        + "is denied it tries to drop the plant as an item.  Does not work on crops or every single block.");
    // @formatter:off
    String[] defaultValues = new String[]{
         "minecraft:hell#"
        ,"minecraft:sky#" + endSet 
        ,"minecraft:forest#" + forestSet
        ,"minecraft:forest_hills#" + forestSet
        ,"minecraft:swampland#"+ oak
        ,"minecraft:smaller_extreme_hills#" + hillsSet
        ,"minecraft:extreme_hills_with_trees#" + hillsSet
        ,"minecraft:extreme_hills#" + hillsSet
        ,"minecraft:taiga#" + spruce
        ,"minecraft:taiga_hills#" + spruce
        ,"minecraft:redwood_taiga#" + String.join(",", spruce, darkoak)
        ,"minecraft:redwood_taiga_hills#" + String.join(",", spruce, darkoak)
        ,"minecraft:jungle#" + jungleSet
        ,"minecraft:jungle_hills#" + jungleSet
        ,"minecraft:jungle_edge#" + jungleSet
        ,"minecraft:birch_forest#" + birch
        ,"minecraft:birch_forest_hills#" + birch
        ,"minecraft:roofed_forest#" + darkoak
        ,"minecraft:savanna#" + acacia
        ,"minecraft:savanna_rock#" + acacia
        ,"minecraft:mesa#" + acacia
        ,"minecraft:mesa_rock#" + acacia
        ,"minecraft:mesa_clear_rock#" + acacia
        ,"minecraft:plains#" + plainsSet
        ,"minecraft:desert#" + desertSet
        ,"minecraft:desert_hills#" + desertSet
        ,"minecraft:river#" + riverBeachSet
        ,"minecraft:stone_beach#" + riverBeachSet
        ,"minecraft:beaches#" + riverBeachSet
        ,"minecraft:cold_beach#" 
        ,"minecraft:frozen_river#"
        ,"minecraft:ice_flats#"
        ,"minecraft:ice_mountains#"
        ,"minecraft:mushroom_island#"
        ,"minecraft:mushroom_island_shore#"
        ,"minecraft:taiga_cold#" + String.join(",", "minecraft:beetroots")
        ,"minecraft:taiga_cold_hills#" + String.join(",", "minecraft:beetroots")
        ,"minecraft:ocean#"
        ,"minecraft:frozen_ocean#"
        ,"minecraft:deep_ocean#"
    }; 
    // @formatter:on
    String[] mapListRaw = config.getStringList("biome sapling map", category, defaultValues, "entry must be 'biome#list,of,sapling,item,ids'.  "
        + "An empty entry for a biome means all saplings disabled in this biome.  "
        + "No entry for a biome means no changes for that biome, this mod ignores it.  "
        + "Biome IDs must be unique, if the same one is listed twice it might probably take the second.  "
        + "Sapling meta example: 0=oak,1=spruce,2=birch,3=jungle,4=acacia,5=darkoak");
    for (String s : mapListRaw) {
      try {
      // 
      String[] temp = s.split("#");
      String biomeId = temp[0];

      if (temp.length == 1) {
        continue;
      }
      biomeAllows.put(biomeId, new String[0]);
      String stringCsv = temp[1];
      if (stringCsv == null || stringCsv.isEmpty()) {
        continue;
      }
      String[] saplingCsv = stringCsv.split(",");
      biomeAllows.put(biomeId, saplingCsv);
      }
      catch (Throwable e) {
        ModSaplings.logger.error(s + "  Error on config parse, format must be like "
            + "'minecraft:beaches#minecraft:sapling:0,minecraft:sapling:1'");
      }
    }
    if (config.hasChanged()) {
      config.save();
    }
  }

  static Map<String, String[]> biomeAllows = new HashMap<>();
  private static boolean dropBlockOnDeny;
  // private static boolean disableBonemeal;

  public static boolean isAllowedToGrow(Biome biome, IBlockState state) {
    int meta = state.getBlock().getMetaFromState(state);
    if (state.getBlock() == Blocks.SAPLING) {
      int growthData = 8;// 0-5 is the type, then it adds on a 0x8
      //now meta is tree type , ignoring grtowth 
      meta = meta - growthData;
    }
    String blockId = state.getBlock().getRegistryName().toString();
    String blockIdWithMeta = blockId + ":" + meta;
    String biomeId = biome.getRegistryName().toString();
    ModSaplings.log(biomeId + "  containsKey  " + biomeAllows.containsKey(biomeId));
    if (biomeAllows.containsKey(biomeId) && biomeAllows.get(biomeId) != null) {
      String[] blocksAllowed = biomeAllows.get(biomeId);
      ModSaplings.log(biomeId + " blocksAllowed = " + blocksAllowed.length);
      for (String sapling : blocksAllowed) {
        if (blockId.equals(sapling) || blockIdWithMeta.equals(sapling)) {
          ModSaplings.log(blockIdWithMeta + " allowed at " + biomeId);
          return true;
        }
      }
    }
    else {
      ModSaplings.log(biomeId + "  has NO ENTRIES, so allow everything " + biomeAllows.get(biomeId));
      return true;
    }
    ModSaplings.log(blockIdWithMeta + " NOT allowed at " + biomeId);
    return false;
  }

  public static void dump() {
    for (String val : ModConfig.biomeAllows.keySet()) {
      System.out.println(val + " ?  " + ModConfig.biomeAllows.get(val).length);
    }
  }

  public static boolean dropBlockOnGrowDeny() {
    return dropBlockOnDeny;
  }

}
