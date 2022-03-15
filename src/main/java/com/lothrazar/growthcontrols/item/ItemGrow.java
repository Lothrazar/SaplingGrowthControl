package com.lothrazar.growthcontrols.item;

import com.lothrazar.growthcontrols.ModGrowthCtrl;
import com.lothrazar.growthcontrols.UtilString;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemGrow extends Item {

  public ItemGrow(Properties p) {
    super(p.stacksTo(1).tab(CreativeModeTab.TAB_TOOLS));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (worldIn.isClientSide && playerIn.isCrouching()) {
      ModGrowthCtrl.CONFIG.getEmptyBiomes(playerIn);
      ItemStack itemstack = playerIn.getItemInHand(handIn);
      return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
    }
    return super.use(worldIn, playerIn, handIn);
  }

  public static Biome getBiome(LevelAccessor world, BlockPos pos) {
    return world.getBiomeManager().getBiome(pos).value();
  }

  @Override
  public InteractionResult useOn(UseOnContext c) {
    if (c.getLevel().isClientSide) {
      //first get block info
      //if any blocks are in a growable list,
      //show be all biomes for it
      Level world = c.getLevel();
      BlockState block = world.getBlockState(c.getClickedPos());
      List<String> biomes = ModGrowthCtrl.CONFIG.getBiomesCombined(block.getBlock());
      if (biomes != null && biomes.size() > 0) {
        Biome b = getBiome(c.getLevel(), c.getClickedPos());
        Registry<Biome> biomeReg = world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);
        boolean growHere = UtilString.isInList(biomes, biomeReg.getKey(b));
        ChatFormatting formatf = (growHere) ? ChatFormatting.GREEN : ChatFormatting.RED;
        UtilString.chatMessage(c.getPlayer(),
            formatf
                + block.getBlock().getName().getString(100)
                + " -> " + String.join(", ", biomes));
      }
      else {
        //
        //else, block is plain, go for the biome lookup
        Biome biome = getBiome(world, c.getClickedPos());
        List<String> growths = ModGrowthCtrl.CONFIG.getGrowthsForBiome(biome);
        sendInfoToPlayer(c.getPlayer(), growths, biome);
      }
    }
    return super.useOn(c);
  }

  private void sendInfoToPlayer(Player p, List<String> growths, Biome biome) {
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
        valid.add(b.getName().getString(100));
      }
    }
    Registry<Biome> biomeReg = p.level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);
    String id = biomeReg.getKey(biome).toString();
    String name = id; 
    Collections.sort(valid);
    String bname = (p.isCrouching()) ? id : name;
    UtilString.chatMessage(p, bname + " : " + String.join(", ", valid));
  }
}
