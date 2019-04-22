package com.lothrazar.saplingcontrol;

import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BonemealHandler {

  @SubscribeEvent
  public void onBonemealEvent(BonemealEvent event) {
    event.setCanceled(ModConfig.cancelAllBonemeal());
  }
}
