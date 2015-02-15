package cz.ondraster.bettersleeping.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BetterSleepingAPI {
   private static List<PlayerDebuff> debuffs;

   public static void addDebuff(PlayerDebuff debuff) {
      if (debuffs == null)
         debuffs = new ArrayList<PlayerDebuff>();

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

   public static SleepingProperty getSleepingProperty(EntityPlayer player) {
      return SleepingProperty.get(player);
   }
}
