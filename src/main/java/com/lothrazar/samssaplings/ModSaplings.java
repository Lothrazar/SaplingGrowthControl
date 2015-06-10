package com.lothrazar.samssaplings;

import java.util.ArrayList; 
import org.apache.logging.log4j.Logger;     
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
  
@Mod(modid = ModSaplings.MODID, useMetadata = true )  
public class ModSaplings
{	
	public static final String MODID = "samssaplings";
	public static final String TEXTURE_LOCATION = MODID + ":";

	@Instance(value = MODID)
	public static ModSaplings instance;	

	public static Logger logger; 
	
	public static boolean plantDespawningSaplings;
	public static boolean saplingGrowthRestricted;
	public static boolean saplingAllNether;
	public static boolean saplingAllEnd;

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{ 
		logger = event.getModLog();  
		
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
	
	  	plantDespawningSaplings = config.getBoolean("sapling_plant_despawn",MODID, true,
    			"When a sapling (or mushroom) despawns while sitting on grass or dirt, it will instead attempt to plant itself.");

		saplingGrowthRestricted = config.getBoolean("sapling_biome_restricted",MODID, true,
    			"Sapling growth is restricted to only their native biomes (for example, birch trees will not grow in roofed forests).");
		 
		saplingAllNether = config.getBoolean("sapling_nether",MODID, false,
    			"If true, all saplings grow in the nether (ignoring sapling_biome_restricted).");
		
		saplingAllEnd = config.getBoolean("sapling_end",MODID, false,
    			"If true, all saplings grow in the end (ignoring sapling_biome_restricted)");
 
		if(config.hasChanged()) {config.save();}
		
		ArrayList<Object> handlers = new ArrayList<Object>();
		  
      	handlers.add(new SaplingDespawnGrowth());//this is only one needs terrain gen buff, plus one of the regular ones
      	handlers.add(instance                         );   

     	for(Object h : handlers)
     	{ 
    		FMLCommonHandler.instance().bus().register(h); 
    		MinecraftForge.EVENT_BUS.register(h); 
    		MinecraftForge.TERRAIN_GEN_BUS.register(h);
    		MinecraftForge.ORE_GEN_BUS.register(h); 
     	} 
	}
	
	public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, Block block)
	{
		return dropItemStackInWorld(worldObj, pos, new ItemStack(block));  
	}
	
	public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, Item item)
	{
		return dropItemStackInWorld(worldObj, pos, new ItemStack(item)); 
	}
	
	public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, ItemStack stack)
	{
		EntityItem entityItem = new EntityItem(worldObj, pos.getX(),pos.getY(),pos.getZ(), stack); 

 		if(worldObj.isRemote==false)//do not spawn a second 'ghost' one on client side
 			worldObj.spawnEntityInWorld(entityItem);
    	return entityItem;
	}
}
