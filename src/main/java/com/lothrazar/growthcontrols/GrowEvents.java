package com.lothrazar.growthcontrols;

import com.lothrazar.growthcontrols.item.ItemGrow;
import java.util.List;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.world.BlockEvent.CropGrowEvent;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GrowEvents {

  @SubscribeEvent
  public void onCropGrowEvent(CropGrowEvent.Pre event) {
    LevelAccessor world = event.getWorld();
    BlockPos pos = event.getPos();
    if (world.isEmptyBlock(pos)) {
      Block blockBelow = world.getBlockState(pos.below()).getBlock();
      if (blockBelow == Blocks.CACTUS || blockBelow == Blocks.CHORUS_FLOWER) {
        //with reeds, its the base growing. with cactus its the air block above that its 'growing' into
        pos = pos.below();
      }
      else {
        //        ModGrowthCtrl.LOGGER.info("air and below is nada?" + pos + "?" + world.getBlockState(pos).getBlock());
        return;
      }
    }
    Block b = world.getBlockState(pos).getBlock();
    Biome biome = ItemGrow.getBiome(world, pos);
    List<String> allowed = ModGrowthCtrl.CONFIG.getBiomesForCrop(b);
    if (allowed == null) {
      //nothing listede for this sapling, evertyhings fine stop blocking the event
      return;
    }
    Registry<Biome> biomeReg = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);
    boolean allowedToGrow = UtilString.isInList(allowed, biomeReg.getKey(biome));
    if (allowedToGrow == false) {
      event.setResult(Event.Result.DENY);
      this.onGrowCancel(world, pos, biome);
    }
  }

  @SubscribeEvent
  public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
    LevelAccessor world = event.getWorld();
    BlockPos pos = event.getPos();
    Block b = world.getBlockState(pos).getBlock();
    Biome biome = ItemGrow.getBiome(world, pos);
    List<String> allowed = ModGrowthCtrl.CONFIG.getBiomesForSapling(b);
    if (allowed == null) {
      //nothing listede for this sapling, evertyhings fine stop blocking the event
      return;
    }
    Registry<Biome> biomeReg = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);
    boolean treeAllowedToGrow = UtilString.isInList(allowed, biomeReg.getKey(biome));
    if (treeAllowedToGrow == false) {
      event.setResult(Event.Result.DENY);
      if (ModGrowthCtrl.CONFIG.getdropFailedGrowth()) {
        this.onGrowCancel(world, pos, biome);
        //  dropItemStackInWorld((World) world, pos, new ItemStack(b));
      }
      world.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(), 3);
    } // else a tree grew that was added by some mod
  }

  @SubscribeEvent
  public void onBone(BonemealEvent event) {
    Level world = event.getWorld();
    BlockPos pos = event.getPos();
    Block b = world.getBlockState(pos).getBlock();
    Biome biome = ItemGrow.getBiome(world, pos);
    //only block bonemeal, IF we find the block in here
    List<String> crops = ModGrowthCtrl.CONFIG.getBiomesCombinedAllowNull(b);
    if (crops == null) {
      return;
    }
    Registry<Biome> biomeReg = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);
    boolean allowedCrop = UtilString.isInList(crops, biomeReg.getKey(biome));
    if (!allowedCrop) {
      event.setCanceled(true);
      event.setResult(Event.Result.DENY);
      this.doSmoke(world, pos);
      //no drops, let it happen naturally
    }
  }

  private void onGrowCancel(LevelAccessor world, BlockPos pos, Biome biome) {
    world.destroyBlock(pos, true);
    this.doSmoke(world, pos);
  }

  private void doSmoke(LevelAccessor world, BlockPos pos) {
    double x = pos.getX() + .5;
    double y = pos.getY();
    double z = pos.getZ() + .5;
    double ySpeed = 0.2;
    for (int i = 0; i < 20; i++) {
      world.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0D, ySpeed, 0.0D);
    }
  }
}
