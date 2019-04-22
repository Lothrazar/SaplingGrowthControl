package com.lothrazar.samssaplings;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Configuration;

public class ModConfig {

  public static void loadConfig(Configuration config) {
    config.load();
    //replanting different mod eh
    //	SaplingDespawnGrowth.chanceReplantDespawning = config.getBoolean("sapling_plant_despawn", ModSaplings.MODID, true, "When a sapling (or mushroom) despawns while sitting on grass or dirt, it will instead attempt to plant itself.");
    //SaplingDespawnGrowth.drop_on_failed_growth = config.getBoolean("drop_on_failed_growth", ModSaplings.MODID, true, "When a sapling fails to grow and turns to a dead bush, if this is true than the sapling item will also drop on the ground.");
    String category = ModSaplings.MODID;
 // @formatter:off
    String[] defaultValues = new String[]{
         "minecraft:ocean#minecraft:sapling"
        ,"minecraft:plains#minecraft:sapling"
        ,"minecraft:desert#minecraft:sapling"
        ,"minecraft:extreme_hills#minecraft:sapling"
        ,"minecraft:forest#minecraft:sapling"
        ,"minecraft:taiga#minecraft:sapling"
        ,"minecraft:swampland#minecraft:sapling"
        ,"minecraft:river#minecraft:sapling"
        ,"minecraft:hell#minecraft:sapling"
        ,"minecraft:sky#minecraft:sapling"
        ,"minecraft:frozen_ocean#minecraft:sapling"
        ,"minecraft:frozen_river#minecraft:sapling"
        ,"minecraft:ice_flats#minecraft:sapling"
        ,"minecraft:ice_mountains#minecraft:sapling"
        ,"minecraft:mushroom_island#minecraft:sapling"
        ,"minecraft:mushroom_island_shore#minecraft:sapling"
        ,"minecraft:beaches#minecraft:sapling"
        ,"minecraft:desert_hills#minecraft:sapling"
        ,"minecraft:forest_hills#minecraft:sapling"
        ,"minecraft:taiga_hills#minecraft:sapling"
        ,"minecraft:smaller_extreme_hills#minecraft:sapling"
        ,"minecraft:jungle#minecraft:sapling"
        ,"minecraft:jungle_hills#minecraft:sapling"
        ,"minecraft:jungle_edge#minecraft:sapling"
        ,"minecraft:deep_ocean#minecraft:sapling"
        ,"minecraft:stone_beach#minecraft:sapling"
        ,"minecraft:cold_beach#minecraft:sapling"
        ,"minecraft:birch_forest#minecraft:sapling"
        ,"minecraft:birch_forest_hills#minecraft:sapling"
        ,"minecraft:roofed_forest#minecraft:sapling"
        ,"minecraft:taiga_cold#minecraft:sapling"
        ,"minecraft:taiga_cold_hills#minecraft:sapling"
        ,"minecraft:redwood_taiga#minecraft:sapling"
        ,"minecraft:redwood_taiga_hills#minecraft:sapling"
        ,"minecraft:extreme_hills_with_trees#minecraft:sapling"
        ,"minecraft:savanna#minecraft:sapling"
        ,"minecraft:savanna_rock#minecraft:sapling"
        ,"minecraft:mesa#minecraft:sapling"
        ,"minecraft:mesa_rock#minecraft:sapling"
        ,"minecraft:mesa_clear_rock#minecraft:sapling"
    }; 
    // @formatter:on
    String[] mapListRaw = config.getStringList("biome sapling map", category, defaultValues, "entry must be 'biome#list,of,sapling,item,ids'.  "
        + "An empty entry for a biome means all saplings disabled in this biome.  "
        + "No entry for a biome means no changes for that biome, this mod ignores it.  "
        + "Biome IDs must be unique, if the same one is listed twice it might probably take the second.  ");
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
