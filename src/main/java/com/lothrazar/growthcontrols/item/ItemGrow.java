package com.lothrazar.growthcontrols.item;

import com.lothrazar.growthcontrols.ModGrowthCtrl;
import com.lothrazar.growthcontrols.UtilString;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemGrow extends Item {

  public ItemGrow(Properties p) {
    super(p.maxStackSize(1).group(ItemGroup.TOOLS));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    if (worldIn.isRemote && playerIn.isCrouching()) {
      ModGrowthCtrl.CONFIG.getEmptyBiomes(playerIn);
      ItemStack itemstack = playerIn.getHeldItem(handIn);
      return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }

  public static Biome getBiome(IWorld world, BlockPos pos) {
    return world.getBiomeManager().getBiome(pos);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext c) {
    if (c.getWorld().isRemote) {
      //first get block info
      //if any blocks are in a growable list,
      //show be all biomes for it
      World world = c.getWorld();
      BlockState block = world.getBlockState(c.getPos());
      List<String> biomes = ModGrowthCtrl.CONFIG.getBiomesCombined(block.getBlock());
      if (biomes != null && biomes.size() > 0) {
        Biome b = getBiome(c.getWorld(), c.getPos());
        Registry<Biome> biomeReg = world.func_241828_r().getRegistry(Registry.BIOME_KEY);
        boolean growHere = UtilString.isInList(biomes, biomeReg.getKey(b));
        TextFormatting formatf = (growHere) ? TextFormatting.GREEN : TextFormatting.RED;
        UtilString.chatMessage(c.getPlayer(),
            formatf
                + block.getBlock().getTranslatedName().getStringTruncated(100)
                + " -> " + String.join(", ", biomes));
      }
      else {
        //
        //else, block is plain, go for the biome lookup
        Biome biome = getBiome(world, c.getPos());
        List<String> growths = ModGrowthCtrl.CONFIG.getGrowthsForBiome(biome);
        sendInfoToPlayer(c.getPlayer(), growths, biome);
      }
    }
    return super.onItemUse(c);
  }

  private void sendInfoToPlayer(PlayerEntity p, List<String> growths, Biome biome) {
    List<String> valid = new ArrayList<>();
    for (String g : growths) {
      Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(g));
      if (b == null) {
        continue;
      }
      if (p.isCrouching()) {
        valid.add(b.getRegistryName().toString());
      }
      else {
        valid.add(b.getTranslatedName().getStringTruncated(100));
      }
    }
    Registry<Biome> biomeReg = p.world.func_241828_r().getRegistry(Registry.BIOME_KEY);
    String id = biomeReg.getKey(biome).toString();
    String name = id; 
    Collections.sort(valid);
    String bname = (p.isCrouching()) ? id : name;
    UtilString.chatMessage(p, bname + " : " + String.join(", ", valid));
  }
}
