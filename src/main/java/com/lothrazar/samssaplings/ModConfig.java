package com.lothrazar.samssaplings;

import net.minecraft.block.state.IBlockState;
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
    //replanting different mod eh
    //	SaplingDespawnGrowth.chanceReplantDespawning = config.getBoolean("sapling_plant_despawn", ModSaplings.MODID, true, "When a sapling (or mushroom) despawns while sitting on grass or dirt, it will instead attempt to plant itself.");
    //SaplingDespawnGrowth.drop_on_failed_growth = config.getBoolean("drop_on_failed_growth", ModSaplings.MODID, true, "When a sapling fails to grow and turns to a dead bush, if this is true than the sapling item will also drop on the ground.");
    String category = ModSaplings.MODID;
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
        ,"minecraft:redwood_taiga#" + spruce
        ,"minecraft:redwood_taiga_hills#" + spruce
        ,"minecraft:jungle#"+ jungle
        ,"minecraft:jungle_hills#"+ jungle
        ,"minecraft:jungle_edge#"+ jungle
        ,"minecraft:birch_forest#" + birch
        ,"minecraft:birch_forest_hills#" + birch
        ,"minecraft:roofed_forest#" + darkoak
        ,"minecraft:savanna#" + acacia
        ,"minecraft:savanna_rock#" + acacia
        ,"minecraft:mesa#" + acacia
        ,"minecraft:mesa_rock#" + String.join(",", acacia, darkoak)
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
    if (config.hasChanged()) {
      config.save();
    }
  }

  public static boolean isAllowedToGrow(Biome biome, IBlockState state) {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * True bush; false air
   * 
   * @return
   */
  public static boolean bushOnDeny() {
    // TODO Auto-generated method stub
    return false;
  }
}
