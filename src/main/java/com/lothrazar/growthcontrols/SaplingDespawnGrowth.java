package com.lothrazar.growthcontrols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lothrazar.growthcontrols.setup.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.SaplingGrowTreeEvent;
import net.minecraftforge.event.world.BlockEvent.CropGrowEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SaplingDespawnGrowth {

  public SaplingDespawnGrowth() {
  }

  private List<String> getBiomesForGrowth(Block block, ForgeConfigSpec.ConfigValue<List<String>> confi) {
    Map<String, List<String>> mapInit = ConfigHandler.getMapBiome(confi);
    String key = block.getRegistryName().toString();
    if (mapInit.containsKey(key) == false) {
      //null means no list set, so everything allowed
      return null;
    }
    //my list is allowed
    return mapInit.get(key);
  }

  @SubscribeEvent
  public void onUse(PlayerInteractEvent.RightClickBlock event) {
    if (event.getItemStack().getItem() != Items.STICK ||
        event.getEntityPlayer().isSneaking() == false) {
      //if im not holding a stick, OR im not sneaking, halt
      return;
    }//else i am holding a stick AND am sneaking
    String msg = "";
 //   Biome biome = event.getWorld().getBiome(event.getPos());
    BlockState st = event.getWorld().getBlockState(event.getPos());
    List<String> crops = this.getBiomesForGrowth(st.getBlock(), ConfigHandler.CROP_BIOMES);
    List<String> saplings = this.getBiomesForGrowth(st.getBlock(), ConfigHandler.GROWABLE_BIOMES);
   // String strBlock = st.getBlock().getRegistryName().toString();
    if (crops != null) {
      //"!" + biome.getRegistryName() + "[C]" + strBlock
      msg = lang("allowedin.crops")+ " | " + String.join(" , ", crops);
      this.chatMessage(event.getEntityPlayer(), msg);
    }
    if (saplings != null) {
      msg =lang( "allowedin.saplings")+ " | " + String.join(" , ", saplings);
      this.chatMessage(event.getEntityPlayer(), msg);
    }
    //    else
    //      this.chatMessage(event.getEntityPlayer(), "everywhere" + st.getBlock().getRegistryName());
  }

  void chatMessage(PlayerEntity player, String message) {
    if (player.world.isRemote) {
      player.sendMessage(new TranslationTextComponent("=="));
      player.sendMessage(new TranslationTextComponent(message));
    }
  }

  public   String lang(String message) {
    TranslationTextComponent t = new TranslationTextComponent(message);
    return t.getFormattedText();
  }
  @SubscribeEvent
  public void onCropGrowEvent(CropGrowEvent.Pre event) {
    IWorld world = event.getWorld();
    BlockPos pos = event.getPos();
    Block b = world.getBlockState(pos).getBlock();
    Biome biome = world.getBiome(pos);
    List<String> allowed = this.getBiomesForGrowth(b, ConfigHandler.CROP_BIOMES);
    ModSaplings.LOGGER.info(b + "test size    " + allowed);
    if (allowed == null) {
      //nothing listede for this sapling, evertyhings fine stop blocking the event
      return;
    }
    String biomeId = biome.getRegistryName().toString();
    boolean allowedToGrow = UtilString.isInList(allowed, biome.getRegistryName());
    if (allowedToGrow == false) {
      ModSaplings.LOGGER.info("CropGrowEvent DENY " + biomeId + b);
      event.setResult(Event.Result.DENY);
      this.onGrowCancel(world, pos, biome);
    }
  }

  @SubscribeEvent
  public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
    IWorld world = event.getWorld();
    BlockPos pos = event.getPos();
    Block b = world.getBlockState(pos).getBlock();
    Biome biome = world.getBiome(pos);
    String biomeId = biome.getRegistryName().toString();
    List<String> allowed = this.getBiomesForGrowth(b, ConfigHandler.GROWABLE_BIOMES);
    if (allowed == null) {
      //nothing listede for this sapling, evertyhings fine stop blocking the event
      return;
    }
    boolean treeAllowedToGrow = UtilString.isInList(allowed, biome.getRegistryName());
    ModSaplings.LOGGER.info(treeAllowedToGrow + " treeAllowedToGrow  "
        + biomeId + allowed.size());
    if (treeAllowedToGrow == false) {
      event.setResult(Event.Result.DENY);
      if (ConfigHandler.getdropFailedGrowth()) {
        this.onGrowCancel(world, pos, biome);
        //  dropItemStackInWorld((World) world, pos, new ItemStack(b));
      }
      world.setBlockState(pos, Blocks.DEAD_BUSH.getDefaultState(), 3);
    }// else a tree grew that was added by some mod
  }

  @SubscribeEvent
  public void onBone(BonemealEvent event) {
    World world = event.getWorld();
    BlockPos pos = event.getPos();
    Block b = world.getBlockState(pos).getBlock();
    //    if(b instanceof IGrowable == false || world.getBlockState(pos).isSolid()){
    //      return;//non solid blocks etc
    //    }
    Biome biome = world.getBiome(pos);
    String biomeId = biome.getRegistryName().toString();
    //only block bonemeal, IF we find the block in here
    List<String> crops = this.getBiomesForGrowth(b, ConfigHandler.CROP_BIOMES);
    List<String> saplings = this.getBiomesForGrowth(b, ConfigHandler.GROWABLE_BIOMES);
    boolean allowedCrop = crops == null || UtilString.isInList(crops, biome.getRegistryName());
    boolean allowedSa = saplings == null || UtilString.isInList(saplings, biome.getRegistryName());
    if (!allowedCrop || !allowedSa) {
      event.setCanceled(true);
      event.setResult(Event.Result.DENY);
      this.doSmoke(world, pos);//no drops, let it happen naturally
    }
  }

  private void onGrowCancel(IWorld world, BlockPos pos, Biome biome) {
    world.destroyBlock(pos, true);
    ModSaplings.LOGGER.info("CropGrowEvent DENY " + biome.getRegistryName());
    this.doSmoke(world, pos);
  }

  private void doSmoke(IWorld world, BlockPos pos) {
    double x = pos.getX() + .5;
    double y = pos.getY();
    double z = pos.getZ() + .5;
    double ySpeed = 0.2;
    for (int i = 0; i < 20; i++) {
      world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, ySpeed, 0.0D);
    }
  }
  //exisitng list of 75 original
  //  minecraft:ocean
  //  minecraft:plains
  //  minecraft:desert
  //  minecraft:mountains
  //  minecraft:forest
  //  minecraft:taiga
  //  minecraft:swamp
  //  minecraft:river
  //  minecraft:nether
  //  minecraft:the_end
  //  minecraft:frozen_ocean
  //  minecraft:frozen_river
  //  minecraft:snowy_tundra
  //  minecraft:snowy_mountains
  //  minecraft:mushroom_fields
  //  minecraft:mushroom_field_shore
  //  minecraft:beach
  //  minecraft:desert_hills
  //  minecraft:wooded_hills
  //  minecraft:taiga_hills
  //  minecraft:mountain_edge
  //  minecraft:jungle
  //  minecraft:jungle_hills
  //  minecraft:jungle_edge
  //  minecraft:deep_ocean
  //  minecraft:stone_shore
  //  minecraft:snowy_beach
  //  minecraft:birch_forest
  //  minecraft:birch_forest_hills
  //  minecraft:dark_forest
  //  minecraft:snowy_taiga
  //  minecraft:snowy_taiga_hills
  //  minecraft:giant_tree_taiga
  //  minecraft:giant_tree_taiga_hills
  //  minecraft:wooded_mountains
  //  minecraft:savanna
  //  minecraft:savanna_plateau
  //  minecraft:badlands
  //  minecraft:wooded_badlands_plateau
  //  minecraft:badlands_plateau
  //  minecraft:small_end_islands
  //  minecraft:end_midlands
  //  minecraft:end_highlands
  //  minecraft:end_barrens
  //  minecraft:warm_ocean
  //  minecraft:lukewarm_ocean
  //  minecraft:cold_ocean
  //  minecraft:deep_warm_ocean
  //  minecraft:deep_lukewarm_ocean
  //  minecraft:deep_cold_ocean
  //  minecraft:deep_frozen_ocean
  //  minecraft:the_void
  //  minecraft:sunflower_plains
  //  minecraft:desert_lakes
  //  minecraft:gravelly_mountains
  //  minecraft:flower_forest
  //  minecraft:taiga_mountains
  //  minecraft:swamp_hills
  //  minecraft:ice_spikes
  //  minecraft:modified_jungle
  //  minecraft:modified_jungle_edge
  //  minecraft:tall_birch_forest
  //  minecraft:tall_birch_hills
  //  minecraft:dark_forest_hills
  //  minecraft:snowy_taiga_mountains
  //  minecraft:giant_spruce_taiga
  //  minecraft:giant_spruce_taiga_hills
  //  minecraft:modified_gravelly_mountains
  //  minecraft:shattered_savanna
  //  minecraft:shattered_savanna_plateau
  //  minecraft:eroded_badlands
  //  minecraft:modified_wooded_badlands_plateau
  //  minecraft:modified_badlands_plateau
  //  minecraft:bamboo_jungle
  //  minecraft:bamboo_jungle_hills
}
