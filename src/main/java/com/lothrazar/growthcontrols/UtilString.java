package com.lothrazar.growthcontrols;

import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class UtilString {

  public static void chatMessage(Player player, String message) {
    if (player.level.isClientSide) {
      //      player.sendSystemMessage(null);
      player.sendSystemMessage(Component.translatable(message));
    }
  }

  public static void chatMessage(Player player, Component displayName) {
    if (player.level.isClientSide) {
      player.sendSystemMessage(displayName);
    }
  }

  /**
   * If the list has "hc:*_sapling" and input is "hc:whatever_sapling" then match is true
   *
   * @param list
   * @param toMatch
   * @return
   */
  public static boolean isInList(final List<String> list, ResourceLocation toMatch) {
    if (toMatch == null) {
      return false;
    }
    String id = toMatch.getNamespace();
    for (String strFromList : list) {
      if (strFromList == null || strFromList.isEmpty()) {
        continue;
      }
      if (strFromList.equals(id)) {
        return true;
      }
      String[] blockIdArray = strFromList.split(":");
      if (blockIdArray.length <= 1) {
        //          ModGrowthCtrl.LOGGER.error("Invalid config value for block : " + strFromList);
        return false;
      }
      String modIdFromList = blockIdArray[0];
      String blockIdFromList = blockIdArray[1]; //has the *
      String modIdToMatch = toMatch.getNamespace();
      String blockIdToMatch = toMatch.getPath();
      if (modIdFromList.equals(modIdToMatch) == false) {
        continue;
      }
      String blockIdListWC = blockIdFromList.replace("*", "");
      if (blockIdToMatch.contains(blockIdListWC)) {
        return true;
      }
    }
    return false;
  }
}
