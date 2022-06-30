package com.lothrazar.growthcontrols;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.growthcontrols.config.ConfigHandler;
import com.lothrazar.growthcontrols.item.ItemGrow;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(ModGrowthCtrl.MODID)
public class ModGrowthCtrl {

  public static final String MODID = "growthcontrols";
  public static ConfigHandler CONFIG;
  public static final Logger LOGGER = LogManager.getLogger();

  public ModGrowthCtrl() {
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(new GrowEvents());
    CONFIG = new ConfigHandler(ConfigHandler.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));
    //TODO: remake whole thing with biome tags
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    @SubscribeEvent
    public static void onItemsRegistry(RegistryEvent.Register<Item> event) {
      Item.Properties properties = new Item.Properties();
      IForgeRegistry<Item> r = event.getRegistry();
      r.register(new ItemGrow(properties).setRegistryName("growth_detector"));
    }
  }
}
