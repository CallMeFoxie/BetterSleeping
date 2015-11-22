package cz.ondraster.bettersleeping.api;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BetterSleepingAPI {
   private static List<PlayerDebuff> debuffs;
   private static ISavedDataProvider dataProvider = null;

   public static void addDebuff(PlayerDebuff debuff) {
      if (debuffs == null)
         debuffs = new ArrayList<PlayerDebuff>();

      if (debuff.tiredLevel == 0) {
         FMLLog.warning("[Better Sleeping API] Tried adding debuff with tired level of 0. That is not how it is meant to be used! Use " +
               "enabled=0 instead! Skipping this debuff...");
         return;
      }


      debuffs.add(debuff);
   }

   public static List<PlayerDebuff> getDebuffs() {
      return debuffs;
   }

   public static boolean removeDebuff(Potion potion) {
      boolean removed = false;
      Iterator iterator = debuffs.iterator();
      while (iterator.hasNext()) {
         PlayerDebuff debuff = (PlayerDebuff) iterator.next();
         if (debuff.potion.equals(potion)) {
            removed = true;
            iterator.remove();
         }
      }

      return removed;
   }

   public static PlayerData getSleepingProperty(EntityPlayer player) {
      if (dataProvider == null) {
         try {
            Class clazz = Class.forName("foxie.bettersleeping.BSSavedData");
            dataProvider = (ISavedDataProvider) clazz.newInstance();
         } catch (Exception e) {
            FMLLog.severe("[Better Sleeping API] Some mod tried accessing saved data without the mod loaded!");
         }
      }

      if (dataProvider == null)
         return null;

      return dataProvider.getPlayerData(player.getUniqueID());
   }
}
