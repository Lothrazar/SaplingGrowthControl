package com.lothrazar.growthcontrols;
import java.util.List;
import java.util.Map;

import com.lothrazar.growthcontrols.setup.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.event.world.BlockEvent.CropGrowEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SaplingDespawnGrowth {

  public SaplingDespawnGrowth() {
  }

  private List<String> getBiomesForGrowth(Block block, ForgeConfigSpec.ConfigValue<List<String>> confi) {
    Map<String, List<String>> mapInit = ConfigHandler.getMapBiome(confi);
    String key = block.getRegistryName().toString();
    if (mapInit.containsKey(key) == false) {
      //null means no list set, so everything allowed
      return null;
    }
    //my list is allowed
    return mapInit.get(key);
  }

  @SubscribeEvent
  public void onCropGrowEvent(CropGrowEvent.Pre event) {
    //     event.setCanceled(true);
    IWorld world = event.getWorld();
    BlockPos pos = event.getPos();
    Block b = world.getBlockState(pos).getBlock();
    Biome biome = world.getBiome(pos);
    String biomeId = biome.getRegistryName().toString();
    List<String> allowed = this.getBiomesForGrowth(b, ConfigHandler.CROP_BIOMES);
    ModSaplings.LOGGER.info(b + "test size    " + allowed);
    if (allowed == null) {
      //nothing listede for this sapling, evertyhings fine stop blocking the event
      return;
    }
    boolean allowedToGrow = allowed.contains(biomeId);//from biome
    if (allowedToGrow == false) {
      ModSaplings.LOGGER.info("CropGrowEvent DENY " + biomeId + b);
      event.setResult(Event.Result.DENY);
      world.destroyBlock(pos, true);
    }
    else
      ModSaplings.LOGGER.info("CropGrowEvent allow  " + biomeId + b);
  }

  @SubscribeEvent
  public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
    IWorld world = event.getWorld();
    BlockPos pos = event.getPos();
    Block b = world.getBlockState(pos).getBlock();
    Biome biome = world.getBiome(pos);
    String biomeId = biome.getRegistryName().toString();
    List<String> allowed = this.getBiomesForGrowth(b, ConfigHandler.GROWABLE_BIOMES);
    if (allowed == null) {
      //nothing listede for this sapling, evertyhings fine stop blocking the event
      return;
    }
    boolean treeAllowedToGrow = allowed.contains(biomeId);//from biome
    ModSaplings.LOGGER.info(treeAllowedToGrow + " treeAllowedToGrow  "
        + biomeId + allowed.size());
    if (treeAllowedToGrow == false) {
      event.setResult(Event.Result.DENY);
      // overwrite the sapling. - we could set to Air first, but dont
      // see much reason to
      if (ConfigHandler.dropFailedGrowth.get()) {
        world.destroyBlock(pos, true);
       //  dropItemStackInWorld((World) world, pos, new ItemStack(b));
      }
      world.setBlockState(pos, Blocks.DEAD_BUSH.getDefaultState(), 3);
    }// else a tree grew that was added by some mod
  }


}
