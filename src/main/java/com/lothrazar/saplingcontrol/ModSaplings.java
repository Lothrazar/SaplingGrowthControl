package com.lothrazar.saplingcontrol;

import org.apache.logging.log4j.Logger;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModSaplings.MODID, useMetadata = true, updateJSON = "https://raw.githubusercontent.com/LothrazarMinecraftMods/SaplingGrowthControl/master/update.json")
public class ModSaplings {

  public static final String MODID = "saplingcontrol";
  public static Logger logger;
  @Instance(value = MODID)
  public static ModSaplings instance;

  @EventHandler
  public void onPreInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();
    ModConfig.loadConfig(new Configuration(event.getSuggestedConfigurationFile()));
    //    MinecraftForge.EVENT_BUS.register(instance);
    MinecraftForge.TERRAIN_GEN_BUS.register(new GrowthHandler());
    MinecraftForge.EVENT_BUS.register(new GrowthHandler());
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
