package com.lothrazar.samssaplings;

import java.util.ArrayList; 

import org.apache.logging.log4j.Level;
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
	//public static final String TEXTURE_LOCATION = MODID + ":";

	@Instance(value = MODID)
	public static ModSaplings instance;	

	public static Logger logger; 
	
  
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{ 
		logger = event.getModLog();  
		
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
	
		SaplingDespawnGrowth.plantDespawningSaplings = config.getBoolean("sapling_plant_despawn",MODID, true,
    			"When a sapling (or mushroom) despawns while sitting on grass or dirt, it will instead attempt to plant itself.");
 
	  	SaplingDespawnGrowth.drop_on_failed_growth = config.getBoolean("drop_on_failed_growth",MODID, true,
    			"When a sapling fails to grow and turns to a dead bush, if this is true than the sapling item will also drop on the ground.");
	  	 
		String category = "sapling_biome_map";

		
		config.addCustomCategoryComment(category, 
				"A list of biome IDs that each sapling is allowed to grow in.  ");
		
		
		String oakCSV = config.get(category,"oak",  "4, 18, 132, 39, 166, 167, 21, 23, 151, 149, 22, 6, 134, 3, 20, 34, 12, 29, 157").getString();
		SaplingDespawnGrowth.oakBiomes = csvToInt(oakCSV);
		
		String acaciaCSV = config.get(category, "acacia",  "35, 36, 38, 163, 164").getString();
		SaplingDespawnGrowth.acaciaBiomes = csvToInt(acaciaCSV);

		String spruceCSV = config.get(category, "spruce",  "5, 19, 32, 160, 161, 33, 30, 31, 158, 3, 20, 34, 21, 12, 13").getString();
		SaplingDespawnGrowth.spruceBiomes = csvToInt(spruceCSV);

		String birchCSV = config.get(category, "birch",  "27, 28, 155, 156, 4, 18, 132, 29, 157").getString();
		SaplingDespawnGrowth.birchBiomes = csvToInt(birchCSV);

		String darkCSV = config.get(category, "dark_oak",  "29, 157").getString();
		SaplingDespawnGrowth.darkoakBiomes = csvToInt(darkCSV);

		String jungleCSV = config.get(category, "jungle",  "21, 23, 22, 149, 151").getString();
		SaplingDespawnGrowth.jungleBiomes = csvToInt(jungleCSV);
		
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

	private ArrayList<Integer> csvToInt(String csv)
	{
		//does not check validity of biome ids
		ArrayList<Integer> bi = new ArrayList<Integer>();
		
		String[] list = csv.split(",");
		
		int biome;
		
		for(String s_id : list)
		{
			try{
				biome = Integer.parseInt(s_id.trim());
				
				bi.add(biome);
			}
			catch(Exception e)
			{
				logger.log(Level.WARN, "Invalid biome id from config file, must be integer: "+s_id);
			}
		}
		
		return bi;
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
