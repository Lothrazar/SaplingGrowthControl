package com.lothrazar.samssaplings;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SaplingDespawnGrowth {

  public static List<String> oakBiomes = new ArrayList<String>();
  public static List<String> spruceBiomes = new ArrayList<String>();
  public static List<String> birchBiomes = new ArrayList<String>();
  public static List<String> jungleBiomes = new ArrayList<String>();
  public static List<String> darkoakBiomes = new ArrayList<String>();
  public static List<String> acaciaBiomes = new ArrayList<String>();

	public static boolean drop_on_failed_growth;
	public static boolean plantDespawningSaplings;

	public SaplingDespawnGrowth() {
	}


	@SubscribeEvent
	public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
    IBlockState state = world.getBlockState(pos);
		Block b = world.getBlockState(pos).getBlock();

		boolean treeAllowedToGrow = false;

    if (b == Blocks.SAPLING) {// this may not always be true: such as trees
								// added by Mods, so not a vanilla tree, but
								// throwing same event

      int meta = Blocks.SAPLING.getMetaFromState(world.getBlockState(pos));

      Biome biome = world.getBiome(pos);// event.world.getBiomeGenForCoords(event.pos).biomeID;

      ResourceLocation biomeid = biome.getRegistryName();
			int growth_data = 8;// 0-5 is the type, then it adds on a 0x8
			// and we know that it is always maxed out at ready to grow 8 since
			// it is turning into a tree.

			int tree_type = meta - growth_data;
      treeAllowedToGrow = ModConfig.isAllowedToGrow(biome, state);
			// IDS:
			// http://www.minecraftforum.net/forums/minecraft-discussion/recent-updates-and-snapshots/381405-full-list-of-biome-ids-as-of-13w36b
			// as of 12 march 2015, it seems biome id 168 does not exist, so 167
			// is highest used (vanilla minecraft)
      //			switch (tree_type) {
      //			case sapling_acacia:
      //          treeAllowedToGrow = ModConfig.contains(biomeID);
      //				break;
      //			case sapling_spruce:
      //				treeAllowedToGrow = spruceBiomes.contains(biomeID);
      //				break;
      //			case sapling_oak:
      //				treeAllowedToGrow = oakBiomes.contains(biomeID);
      //				break;
      //			case sapling_birch:
      //				treeAllowedToGrow = birchBiomes.contains(biomeID);
      //				break;
      //			case sapling_darkoak:
      //				treeAllowedToGrow = darkoakBiomes.contains(biomeID);
      //				break;
      //			case sapling_jungle:
      //				treeAllowedToGrow = jungleBiomes.contains(biomeID);
      //				break;
      //
      //			}

			if (treeAllowedToGrow == false) {
				event.setResult(Result.DENY);
        if (drop_on_failed_growth) {
          world.destroyBlock(pos, true);
        }

				// overwrite the sapling. - we could set to Air first, but dont
				// see much reason to
        if (ModConfig.bushOnDeny()) {
          world.setBlockState(pos, Blocks.DEADBUSH.getDefaultState());
        }
        else {
          world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
			}
		}// else a tree grew that was added by some mod
	}

	@SubscribeEvent
	public void onItemExpireEvent(ItemExpireEvent event) {
		if (plantDespawningSaplings == false) {
			return;
		}

		EntityItem entityItem = event.getEntityItem();
		Entity entity = event.getEntity();
    ItemStack is = entityItem.getItem();
		if (is == null) {
			return;
		}// has not happened in the wild, yet

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

	public static EntityItem dropItemStackInWorld(World world, BlockPos pos, ItemStack stack) {
		EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);

    if (world.isRemote == false) {// do not spawn a second 'ghost' one on
										// client side
      world.spawnEntity(entityItem);
    }
		return entityItem;
	}
}
