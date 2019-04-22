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

  public static void loadConfig(Configuration config) {
    config.load();
    String category = ModSaplings.MODID;
    ModConfig.dropBlockOnDeny = config.getBoolean("dropBlockOnDeny", category, true, "If true, then whenever sapling growth "
        + "is denied it tries to drop the plant as an item.");
    // @formatter:off
    String[] defaultValues = new String[]{
         "minecraft:forest#" + oak
        ,"minecraft:forest_hills#" + oak
        ,"minecraft:swampland#"+ oak
        ,"minecraft:smaller_extreme_hills#" + spruce
        ,"minecraft:extreme_hills_with_trees#" + spruce
        ,"minecraft:extreme_hills#" + spruce
        ,"minecraft:taiga#" + spruce
        ,"minecraft:taiga_hills#" + spruce
        ,"minecraft:redwood_taiga#" + String.join(",", spruce, darkoak)
        ,"minecraft:redwood_taiga_hills#" + String.join(",", spruce, darkoak)
        ,"minecraft:jungle#"+ jungle
        ,"minecraft:jungle_hills#"+ jungle
        ,"minecraft:jungle_edge#"+ jungle
        ,"minecraft:birch_forest#" + birch
        ,"minecraft:birch_forest_hills#" + birch
        ,"minecraft:roofed_forest#" + darkoak
        ,"minecraft:savanna#" + acacia
        ,"minecraft:savanna_rock#" + acacia
        ,"minecraft:mesa#" + acacia
        ,"minecraft:mesa_rock#" + acacia
        ,"minecraft:mesa_clear_rock#" + acacia
        ,"minecraft:ocean#"
        ,"minecraft:plains#"
        ,"minecraft:desert#"
        ,"minecraft:desert_hills#" 
        ,"minecraft:river#"
        ,"minecraft:hell#"
        ,"minecraft:sky#"
        ,"minecraft:ice_flats#"
        ,"minecraft:ice_mountains#"
        ,"minecraft:mushroom_island#"
        ,"minecraft:mushroom_island_shore#"
        ,"minecraft:taiga_cold#"
        ,"minecraft:taiga_cold_hills#"
        ,"minecraft:frozen_ocean#"
        ,"minecraft:frozen_river#"
        ,"minecraft:deep_ocean#"
        ,"minecraft:stone_beach#"
        ,"minecraft:beaches#"
        ,"minecraft:cold_beach#"
    }; 
    // @formatter:on
    String[] mapListRaw = config.getStringList("biome sapling map", category, defaultValues, "entry must be 'biome#list,of,sapling,item,ids'.  "
        + "An empty entry for a biome means all saplings disabled in this biome.  "
        + "No entry for a biome means no changes for that biome, this mod ignores it.  "
        + "Biome IDs must be unique, if the same one is listed twice it might probably take the second.  "
        + "For , 0=oak,1=spruce,2=birch,3=jungle,4=acacia,5=darkoak");
    for (String s : mapListRaw) {
      try {
        // 
        String[] temp = s.split("#");
        String biomeId = temp[0];
        biomeAllows.put(biomeId, new String[0]);
        String[] saplingCsv = temp[1].split(",");
        biomeAllows.put(biomeId, saplingCsv);
      }
      catch (Throwable e) {
        ModSaplings.logger.error("Error on config parse, format must be like "
            + "'minecraft:beaches#minecraft:sapling:0,minecraft:sapling:1' . s = " + s, e);
      }
    }
    if (config.hasChanged()) {
      config.save();
    }
  }

  static Map<String, String[]> biomeAllows = new HashMap<>();
  private static boolean dropBlockOnDeny;

  public static boolean isAllowedToGrow(Biome biome, IBlockState state) {
    int meta = 0;
    meta = state.getBlock().getMetaFromState(state);
    if (state.getBlock() == Blocks.SAPLING) {
    int growthData = 8;// 0-5 is the type, then it adds on a 0x8
      //now meta is tree type , ignoring grtowth 
      meta = meta - growthData;
    }
    String blockId = state.getBlock().getRegistryName().toString();
    String blockIdWithMeta = blockId + ":" + meta;
    String biomeId = biome.getRegistryName().toString();
    if (biomeAllows.containsKey(biomeId) && biomeAllows.get(biomeId) != null) {
      String[] blocksAllowed = biomeAllows.get(biomeId);

      
      ModSaplings.logger.info(biomeId + " entry = " + blocksAllowed.length);
      for (String sapling : blocksAllowed) {
        ModSaplings.logger.info(" ? " + sapling + "<->" + blockIdWithMeta);
        if (blockId.equals(sapling) || blockIdWithMeta.equals(sapling)) {
          ModSaplings.logger.info("YES you can grow here  = " + sapling);
          return true;
        }
      }
    }
    else {
      ModSaplings.logger.info(biomeId + "  has NO ENTRIES, so allow everything");
      return true;
    }
    ModSaplings.logger.info("Not allowed ");
    return false;
  }


  public static boolean dropBlockOnGrowDeny() {
    return dropBlockOnDeny;
  }
}
