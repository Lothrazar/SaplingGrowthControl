package com.lothrazar.growthcontrols.item;

import com.lothrazar.growthcontrols.ModGrowthCtrl;
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
    //    BlockState blockstate = c.getWorld().getBlockState(c.getPos());
    Biome biome = c.getWorld().getBiome(c.getPos());
    String biomeid = biome.getRegistryName().toString();
    ModGrowthCtrl.LOGGER.info("b::" + biomeid);
    return super.onItemUse(c);
  }
}
