package com.lothrazar.growthcontrols;

import java.util.List;
import java.util.UUID;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class UtilString {

  public static void chatMessage(PlayerEntity player, String message) {
    if (player.world.isRemote) {
      player.sendMessage(new TranslationTextComponent(message), UUID.randomUUID());
    }
  }

  public static void chatMessage(PlayerEntity player, ITextComponent displayName) {
    if (player.world.isRemote) {
      player.sendMessage(displayName, UUID.randomUUID());
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
