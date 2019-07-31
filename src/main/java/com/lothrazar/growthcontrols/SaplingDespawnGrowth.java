package com.lothrazar.growthcontrols;
import java.util.HashMap;
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
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SaplingDespawnGrowth {

  public static boolean drop_on_failed_growth = true;
  public static boolean plantDespawningSaplings;

  public SaplingDespawnGrowth() {
  }

  private List<String> getBiomesForGrowth(Block block) {
    Map<String, List<String>> mapInit = ConfigHandler.getMapBiome();
    String key = block.getRegistryName().toString();
    if (mapInit.containsKey(key) == false) {
      //null means no list set, so everything allowed
      return null;
    }
    //my list is allowed
    return mapInit.get(key);
  }

  @SubscribeEvent
  public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
    IWorld world = event.getWorld();
    BlockPos pos = event.getPos();
    Block b = world.getBlockState(pos).getBlock();
    boolean treeAllowedToGrow = false;
    Biome biome = world.getBiome(pos);
    String biomeId = biome.getRegistryName().toString();
    List<String> allowed = this.getBiomesForGrowth(b);
    if(allowed == null){
      //nothing listede for this sapling, evertyhings fine stop blocking the event
      return ;
    }
    treeAllowedToGrow = allowed.contains(biomeId);//from biome
    ModSaplings.LOGGER.info(treeAllowedToGrow +" treeAllowedToGrow  "
        + biomeId + allowed.size());
    if (treeAllowedToGrow == false) {
      event.setResult(Event.Result.DENY);
      // overwrite the sapling. - we could set to Air first, but dont
      // see much reason to
      world.setBlockState(pos, Blocks.DEAD_BUSH.getDefaultState(), 3);
      if (drop_on_failed_growth) {
        dropItemStackInWorld((World) world, pos, new ItemStack(b));
      }
    }// else a tree grew that was added by some mod

  }

  //
  //	@SubscribeEvent
  //	public void onItemExpireEvent(ItemExpireEvent event) {
  //		if (plantDespawningSaplings == false) {
  //			return;
  //		}
  //
  //		EntityItem entityItem = event.getEntityItem();
  //		Entity entity = event.getEntity();
  //		ItemStack is = entityItem.getEntityItem();
  //		if (is == null) {
  //			return;
  //		}// has not happened in the wild, yet
  //
  //		Block blockhere = entity.worldObj.getBlockState(entityItem.getPosition()).getBlock();
  //		Block blockdown = entity.worldObj.getBlockState(entityItem.getPosition().down()).getBlock();
  //
  //		if (blockhere == Blocks.air && blockdown == Blocks.dirt || // includes
  //																	// podzol
  //																	// and such
  //		blockdown == Blocks.grass) {
  //			// plant the sapling, replacing the air and on top of dirt/plantable
  //
  //			if (Block.getBlockFromItem(is.getItem()) == Blocks.sapling)
  //				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.sapling.getStateFromMeta(is.getItemDamage()));
  //			else if (Block.getBlockFromItem(is.getItem()) == Blocks.red_mushroom)
  //				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.red_mushroom.getDefaultState());
  //			else if (Block.getBlockFromItem(is.getItem()) == Blocks.brown_mushroom)
  //				entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.brown_mushroom.getDefaultState());
  //
  //		}
  //	}

  public static ItemEntity dropItemStackInWorld(World worldObj, BlockPos pos, ItemStack stack) {
    ItemEntity entityItem = new ItemEntity(worldObj, pos.getX(), pos.getY(), pos.getZ(), stack);
    if (worldObj.isRemote == false)// do not spawn a second 'ghost' one on
    {
      worldObj.addEntity(entityItem);
    }
    return entityItem;
  }
}
