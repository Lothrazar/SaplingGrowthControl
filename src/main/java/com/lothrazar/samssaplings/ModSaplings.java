package com.lothrazar.samssaplings;

import java.util.ArrayList; 

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;     

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
  
@Mod(modid = ModSaplings.MODID, useMetadata = true )  
public class ModSaplings
{	
	public static final String MODID = "samssaplings";
	public static Logger logger; 

	@Instance(value = MODID)
	public static ModSaplings instance;	

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{ 
		logger = event.getModLog();  
		
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		ModConfig.loadConfig(config);
		
		ArrayList<Object> handlers = new ArrayList<Object>();
		  
      	handlers.add(new SaplingDespawnGrowth());//this is only one needs terrain gen buff, plus one of the regular ones
      	handlers.add(instance                         );   

     	for(Object h : handlers)
     	{ 
    		MinecraftForge.EVENT_BUS.register(h); 
    		MinecraftForge.TERRAIN_GEN_BUS.register(h);
    		MinecraftForge.ORE_GEN_BUS.register(h); 
     	} 
	}

}
