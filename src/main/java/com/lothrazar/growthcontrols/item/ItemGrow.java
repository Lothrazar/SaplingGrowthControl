package com.lothrazar.growthcontrols.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.lothrazar.growthcontrols.ModGrowthCtrl;
import com.lothrazar.growthcontrols.UtilString;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemGrow extends Item {

  public ItemGrow(Properties p) {
    super(p);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext c) {
    Biome biome = c.getWorld().getBiome(c.getPos());
    List<String> growths = ModGrowthCtrl.config.getGrowthsForBiome(biome);
    if (c.getWorld().isRemote) {
      sendInfoToPlayer(growths, biome);
    }
    return super.onItemUse(c);
  }

  private void sendInfoToPlayer(List<String> growths, Biome biome) {
    PlayerEntity p = Minecraft.getInstance().player;// ModGrowthCtrl.proxy.getClientWorld();
    //    UtilString.chatMessage(p, );
    List<String> valid = new ArrayList<>();
    for (String g : growths) {
      Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(g));
      if (b != null) {
        valid.add(b.getNameTextComponent().getFormattedText());
      }
    }
    Collections.sort(valid);
    UtilString.chatMessage(p, biome.getDisplayName().getFormattedText() + " : " + String.join(", ", valid));
  }
}
