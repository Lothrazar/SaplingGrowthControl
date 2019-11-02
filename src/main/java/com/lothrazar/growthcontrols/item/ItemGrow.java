package com.lothrazar.growthcontrols.item;

import java.util.List;
import com.lothrazar.growthcontrols.ModGrowthCtrl;
import com.lothrazar.growthcontrols.UtilString;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.biome.Biome;

public class ItemGrow extends Item {

  public ItemGrow(Properties p) {
    super(p);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext c) {
    Biome biome = c.getWorld().getBiome(c.getPos());
    List<String> growths = ModGrowthCtrl.config.getGrowthsForBiome(biome);
    String biomeid = biome.getRegistryName().toString();
    if (c.getWorld().isRemote) {
      sendInfoToPlayer(growths, biomeid);
    }
    return super.onItemUse(c);
  }

  private void sendInfoToPlayer(List<String> growths, String biomeid) {
    PlayerEntity p = Minecraft.getInstance().player;// ModGrowthCtrl.proxy.getClientWorld();
    UtilString.chatMessage(p, "Growable in :" + biomeid);
    UtilString.chatMessage(p, String.join(", ", growths));
  }
}
