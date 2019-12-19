package com.lothrazar.growthcontrols.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.lothrazar.growthcontrols.ModGrowthCtrl;
import com.lothrazar.growthcontrols.UtilString;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.text.TextFormatting;
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
      ModGrowthCtrl.config.getEmptyBiomes();
      ItemStack itemstack = playerIn.getHeldItem(handIn);
      return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }

  private Biome getBiome(World world, BlockPos pos) {
    return world.func_225523_d_().func_226836_a_(pos);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext c) {
    if (c.getWorld().isRemote) {
      //first get block info
      //if any blocks are in a growable list,
      //show be all biomes for it
      BlockState block = c.getWorld().getBlockState(c.getPos());
      List<String> biomes = ModGrowthCtrl.config.getBiomesCombined(block.getBlock());
      if (biomes != null && biomes.size() > 0) {
        boolean growHere = UtilString.isInList(biomes, getBiome(c.getWorld(), c.getPos()).getRegistryName());
        TextFormatting formatf = (growHere) ? TextFormatting.GREEN : TextFormatting.RED;
        UtilString.chatMessage(c.getPlayer(),
            formatf
                + block.getBlock().getNameTextComponent().getFormattedText()
                + " -> " + String.join(", ", biomes));
      }
      else {
        //
        //else, block is plain, go for the biome lookup
        Biome biome = getBiome(c.getWorld(), c.getPos());
        List<String> growths = ModGrowthCtrl.config.getGrowthsForBiome(biome);
        sendInfoToPlayer(growths, biome);
      }
    }
    return super.onItemUse(c);
  }

  private void sendInfoToPlayer(List<String> growths, Biome biome) {
    PlayerEntity p = Minecraft.getInstance().player;// ModGrowthCtrl.proxy.getClientWorld();
    //    UtilString.chatMessage(p, );
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
        valid.add(b.getNameTextComponent().getFormattedText());
      }
    }
    Collections.sort(valid);
    String bname = (p.isCrouching()) ? biome.getRegistryName().toString() : biome.getDisplayName().getFormattedText();
    UtilString.chatMessage(p, bname
        + " : " + String.join(", ", valid));
  }
}
