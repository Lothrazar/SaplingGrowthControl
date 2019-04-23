package com.lothrazar.biomegrowthcontrol;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.event.world.BlockEvent.CropGrowEvent;
import net.minecraftforge.event.world.BlockEvent.FarmlandTrampleEvent;
//import net.minecraftforge.event.world.BlockEvent.FarmlandTrampleEvent;
//import net.minecraftforge.event.world.BlockEvent.CropGrowEvent;  
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GrowthHandler {

  @SubscribeEvent
  public void onFarmlandTrampleEvent(FarmlandTrampleEvent event) {
    //
  }

  @SubscribeEvent
  public void onCropGrowEvent(CropGrowEvent event) {
    //  
    World world = event.getWorld();
    BlockPos pos = event.getPos();
    if (pos == null || world.isAirBlock(pos)) {
      return;
    }
    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();
    Biome biome = world.getBiome(pos);
    try {
      if (ModConfig.isAllowedToGrow(biome, state) == false) {
        if (block == Blocks.CHORUS_PLANT) {
          if (world.getBlockState(pos.up()).getBlock() == Blocks.CHORUS_FLOWER) {
            world.destroyBlock(pos.up(), ModConfig.dropBlockOnGrowDeny());
            world.destroyBlock(pos, false);
          }
        }
        event.setResult(Result.DENY);
        if (ModConfig.dropBlockOnGrowDeny()) {
          world.destroyBlock(pos, true);
        }
      }
    }
    catch (Throwable e) {
      ModSaplings.logger.error("sapling event ", e);
    }
  }

  @SubscribeEvent
  public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
    World world = event.getWorld();
    BlockPos pos = event.getPos();
    IBlockState state = world.getBlockState(pos);
    Block block = world.getBlockState(pos).getBlock();
    ModSaplings.log("Growth event " + pos + " " + block.getLocalizedName());
    Biome biome = world.getBiome(pos);
    try {
      if (ModConfig.isAllowedToGrow(biome, state) == false) {
        event.setResult(Result.DENY);
        //        world.spawnParticle(EnumParticleTypes.BARRIER, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
        if (ModConfig.dropBlockOnGrowDeny()) {
          world.destroyBlock(pos, true);
          world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
      }
    }
    catch (Throwable e) {
      ModSaplings.logger.error("sapling event ", e);
    }
  }
}
