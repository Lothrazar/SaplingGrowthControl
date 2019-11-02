package com.lothrazar.growthcontrols;

import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class UtilString {

  public static void chatMessage(PlayerEntity player, String message) {
    if (player.world.isRemote) {
      player.sendMessage(new TranslationTextComponent((message)));
    }
  }

  /**
   * One day i might make this a setting or an input arg for now i have no use to turn it off
   */
  public static final boolean matchWildcard = true;

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
        continue;//just ignore me
      }
      if (strFromList.equals(id)) {
        return true;
      }
      if (matchWildcard) {
        String[] blockIdArray = strFromList.split(":");
        if (blockIdArray.length <= 1) {
          ModGrowthCtrl.LOGGER.error("Invalid config value for block : " + strFromList);
          return false;
        }
        String modIdFromList = blockIdArray[0];
        String blockIdFromList = blockIdArray[1];//has the *
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
    }
    return false;
  }
  //  /**
  //   * TODO: make a unit testing module, or install a framework for now these pass so i removed call to them
  //   */
  //  public static void unitTests() {
  //    NonNullList<String> blacklist = NonNullList.from("",
  //        "terraqueous:pergola", "harvestcraft:*_sapling", "croparia:block_cane_*");
  //    ModSaplings.LOGGER.logger.logTestResult("1] expect true " + isInList(blacklist, new ResourceLocation("harvestcraft:fruit_sapling")));
  //    ModSaplings.LOGGER.logger.logTestResult("2] expect true " + isInList(blacklist, new ResourceLocation("croparia:block_cane_zzzzzz")));
  //    ModCyclic.logger.logTestResult("3] expect false " + isInList(blacklist, new ResourceLocation("harvestcraft:pampeach")));
  //    ModCyclic.logger.logTestResult("4] expect false " + isInList(blacklist, new ResourceLocation("harvestcraft:groundtrap")));
  //  }
}
