package com.lothrazar.saplingcontrol;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GrowthHandler {

  public static List<String> oakBiomes = new ArrayList<String>();
  public static List<String> spruceBiomes = new ArrayList<String>();
  public static List<String> birchBiomes = new ArrayList<String>();
  public static List<String> jungleBiomes = new ArrayList<String>();
  public static List<String> darkoakBiomes = new ArrayList<String>();
  public static List<String> acaciaBiomes = new ArrayList<String>();

  public GrowthHandler() {
	}


	@SubscribeEvent
	public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
    IBlockState state = world.getBlockState(pos);
		Block b = world.getBlockState(pos).getBlock();

		boolean treeAllowedToGrow = false;

    Biome biome = world.getBiome(pos);
			int growth_data = 8;// 0-5 is the type, then it adds on a 0x8

    int meta = Blocks.SAPLING.getMetaFromState(state);
			int tree_type = meta - growth_data;

      treeAllowedToGrow = ModConfig.isAllowedToGrow(biome, state);


			if (treeAllowedToGrow == false) {
				event.setResult(Result.DENY);
      if (ModConfig.dropOnFailedGrowth) {
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

	}


}