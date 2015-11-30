package com.lothrazar.samssaplings;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;

public class ModConfig
{
	public static void loadConfig(Configuration config)
	{
		config.load();
	
		SaplingDespawnGrowth.plantDespawningSaplings = config.getBoolean("sapling_plant_despawn",ModSaplings.MODID, true,
    			"When a sapling (or mushroom) despawns while sitting on grass or dirt, it will instead attempt to plant itself.");
 
	  	SaplingDespawnGrowth.drop_on_failed_growth = config.getBoolean("drop_on_failed_growth",ModSaplings.MODID, true,
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
	}

	private static ArrayList<Integer> csvToInt(String csv)
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
				ModSaplings.logger.log(Level.WARN, "Invalid biome id from config file, must be integer: "+s_id);
			}
		}
		
		return bi;
	}
}
