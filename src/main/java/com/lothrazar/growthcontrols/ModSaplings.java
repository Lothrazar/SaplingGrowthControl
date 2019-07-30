package com.lothrazar.growthcontrols;
import com.lothrazar.growthcontrols.setup.ClientProxy;
import com.lothrazar.growthcontrols.setup.ConfigHandler;
import com.lothrazar.growthcontrols.setup.IProxy;
import com.lothrazar.growthcontrols.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("growthcontrols")
public class ModSaplings {

  public static final String MODID = "growthcontrols";
  private String certificateFingerprint = "@FINGERPRINT@";
  public static final IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

  private static final Logger LOGGER = LogManager.getLogger();
//  public static Logger logger;

  public ModSaplings() {

    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    MinecraftForge.EVENT_BUS.register(new SaplingDespawnGrowth());
    MinecraftForge.EVENT_BUS.register(this);

    ConfigHandler.loadConfig(ConfigHandler.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));

  }

  private void setup(final FMLCommonSetupEvent event) {

    for(Biome b : Biome.BIOMES){
      LOGGER.info
          ("\"" + b.getRegistryName().toString() +"\"" );
    }
  }

  // You can use SubscribeEvent and let the Event Bus discover methods to call
  @SubscribeEvent
  public void onServerStarting(FMLServerStartingEvent event) {


  }

  // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
  // Event bus for receiving Registry Events)
  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
      // register a new block here
      LOGGER.info("HELLO from Register Block");
      //      event.getRegistry().register(new BlockRequest());
    }


    @SubscribeEvent
    public static void onItemsRegistry(RegistryEvent.Register<Item> event) {
      //      Item.Properties properties = new Item.Properties().group(SsnRegistry.itemGroup);
      //      event.getRegistry().register(new BlockItem(SsnRegistry.master, properties).setRegistryName("master"));
    }
  }

  @SubscribeEvent
  public static void onFingerprintViolation(FMLFingerprintViolationEvent event) {
    // https://tutorials.darkhax.net/tutorials/jar_signing/
    String source = (event.getSource() == null) ? "" : event.getSource().getName() + " ";
    String msg = MODID + "Invalid fingerprint detected! The file " + source + "may have been tampered with. This version will NOT be supported by the author!";
    System.out.println(msg);
  }

}
