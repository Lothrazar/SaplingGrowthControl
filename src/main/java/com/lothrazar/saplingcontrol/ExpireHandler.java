package com.lothrazar.saplingcontrol;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ExpireHandler {

  @SubscribeEvent
  public void onItemExpireEvent(ItemExpireEvent event) {
    if (ModConfig.plantDespawningSaplings == false) {
      return;
    }
    EntityItem entityItem = event.getEntityItem();
    Entity entity = event.getEntity();
    ItemStack is = entityItem.getItem();
    if (is == null) {
      return;
    } // has not happened in the wild, yet
    Block blockhere = entity.world.getBlockState(entityItem.getPosition()).getBlock();
    Block blockdown = entity.world.getBlockState(entityItem.getPosition().down()).getBlock();

    if (blockhere == Blocks.AIR && blockdown == Blocks.DIRT || // includes
    // podzol
    // and such
        blockdown == Blocks.GRASS) {
      // plant the sapling, replacing the air and on top of dirt/plantable
      if (Block.getBlockFromItem(is.getItem()) == Blocks.SAPLING)
        entity.world.setBlockState(entityItem.getPosition(), Blocks.SAPLING.getStateFromMeta(is.getItemDamage()));
      else if (Block.getBlockFromItem(is.getItem()) == Blocks.RED_MUSHROOM)
        entity.world.setBlockState(entityItem.getPosition(), Blocks.RED_MUSHROOM.getDefaultState());
      else if (Block.getBlockFromItem(is.getItem()) == Blocks.BROWN_MUSHROOM)
        entity.world.setBlockState(entityItem.getPosition(), Blocks.BROWN_MUSHROOM.getDefaultState());
    }
  }
}
