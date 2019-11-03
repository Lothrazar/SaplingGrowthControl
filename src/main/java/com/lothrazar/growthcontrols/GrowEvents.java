package com.lothrazar.growthcontrols;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.world.BlockEvent.CropGrowEvent;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GrowEvents {

  public GrowEvents() {}

  @SubscribeEvent
  public void onCropGrowEvent(CropGrowEvent.Pre event) {
    IWorld world = event.getWorld();
    BlockPos pos = event.getPos();
    if (world.isAirBlock(pos)) {
      Block blockBelow = world.getBlockState(pos.down()).getBlock();
      if (blockBelow == Blocks.CACTUS || blockBelow == Blocks.CHORUS_FLOWER) {
        //with reeds, its the base growing. with cactus its the air block above that its 'growing' into
        pos = pos.down();
      }
      else {
        //        ModGrowthCtrl.LOGGER.info("air and below is nada?" + pos + "?" + world.getBlockState(pos).getBlock());
        return;
      }
    }
    Block b = world.getBlockState(pos).getBlock();
    Biome biome = world.getBiome(pos);
    List<String> allowed = ModGrowthCtrl.config.getBiomesForCrop(b);
    if (allowed == null) {
      //      ModGrowthCtrl.LOGGER.info("CropGrowEvent ALLOW since all entries null for this crop" + b);
      //nothing listede for this sapling, evertyhings fine stop blocking the event
      return;
    }
    //    String biomeId = biome.getRegistryName().toString();
    boolean allowedToGrow = UtilString.isInList(allowed, biome.getRegistryName());
    if (allowedToGrow == false) {
      //      ModGrowthCtrl.LOGGER.info("CropGrowEvent DENY " + biome.getRegistryName() + ":" + b);
      event.setResult(Event.Result.DENY);
      this.onGrowCancel(world, pos, biome);
    }
    //    else {
    //      ModGrowthCtrl.LOGGER.info("CropGrowEvent ALLOW " + biome.getRegistryName() + ":" + b);
    //    }
  }

  @SubscribeEvent
  public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
    IWorld world = event.getWorld();
    BlockPos pos = event.getPos();
    Block b = world.getBlockState(pos).getBlock();
    Biome biome = world.getBiome(pos);
    String biomeId = biome.getRegistryName().toString();
    List<String> allowed = ModGrowthCtrl.config.getBiomesForSapling(b);
    if (allowed == null) {
      //nothing listede for this sapling, evertyhings fine stop blocking the event
      return;
    }
    boolean treeAllowedToGrow = UtilString.isInList(allowed, biome.getRegistryName());
    //    ModGrowthCtrl.LOGGER.info(treeAllowedToGrow + " treeAllowedToGrow  "
    //        + biomeId + allowed.size());
    if (treeAllowedToGrow == false) {
      event.setResult(Event.Result.DENY);
      if (ModGrowthCtrl.config.getdropFailedGrowth()) {
        this.onGrowCancel(world, pos, biome);
        //  dropItemStackInWorld((World) world, pos, new ItemStack(b));
      }
      world.setBlockState(pos, Blocks.DEAD_BUSH.getDefaultState(), 3);
    } // else a tree grew that was added by some mod
  }

  @SubscribeEvent
  public void onBone(BonemealEvent event) {
    World world = event.getWorld();
    BlockPos pos = event.getPos();
    Block b = world.getBlockState(pos).getBlock();
    Biome biome = world.getBiome(pos);
    //only block bonemeal, IF we find the block in here
    List<String> crops = ModGrowthCtrl.config.getBiomesCombinedAllowNull(b);
    if (crops == null) {
      return;
    }
    boolean allowedCrop = UtilString.isInList(crops, biome.getRegistryName());
    if (!allowedCrop) {
      event.setCanceled(true);
      event.setResult(Event.Result.DENY);
      this.doSmoke(world, pos);//no drops, let it happen naturally
    }
  }

  private void onGrowCancel(IWorld world, BlockPos pos, Biome biome) {
    world.destroyBlock(pos, true);
    //    ModGrowthCtrl.LOGGER.info("CropGrowEvent DENY " + biome.getRegistryName());
    this.doSmoke(world, pos);
  }

  private void doSmoke(IWorld world, BlockPos pos) {
    double x = pos.getX() + .5;
    double y = pos.getY();
    double z = pos.getZ() + .5;
    double ySpeed = 0.2;
    for (int i = 0; i < 20; i++) {
      world.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0D, ySpeed, 0.0D);
    }
  }
}
