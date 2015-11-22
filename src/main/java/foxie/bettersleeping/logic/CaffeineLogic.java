package foxie.bettersleeping.logic;

import foxie.bettersleeping.BSSavedData;
import foxie.bettersleeping.Config;
import cz.ondraster.bettersleeping.api.PlayerData;
import cz.ondraster.bettersleeping.api.PlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.List;

public class CaffeineLogic {
   public static DamageSource damageCaffeine = new DamageOverdose();
   public static DamageSource damagePill = new DamagePill();

   public static void checkDebuff(EntityPlayer player) {
      PlayerData data = BSSavedData.instance().getData(player);

      if (data.getCaffeineLevel() >= Config.deathFromCaffeineOverdose && Config.deathFromCaffeineOverdose > 0) {
         player.attackEntityFrom(damageCaffeine, 9001);
      }

      if (data.getCaffeineLevel() >= Config.caffeineDebuffsAt) {
         player.addPotionEffect(new PotionEffect(Potion.confusion.getId(), Config.POTION_DURATION * 2));
      }

      if (data.getPillLevel() >= Config.maximumPillLevel && Config.maximumPillLevel > 0) {
         player.attackEntityFrom(damagePill, 9001);
      }

      data.decreaseCaffeineLevel();
      data.decreasePillLevel();
   }

   public static boolean isCoffee(ItemStack itemStack) {
      return isItemAnOreDict(itemStack, Arrays.asList(Config.caffeineOredicts));
   }

   public static boolean isItemAnOreDict(ItemStack itemStack, List<String> allowed) {
      int[] ids = OreDictionary.getOreIDs(itemStack);
      if (ids.length > 0) {
         for (int id : ids) {
            String orename = OreDictionary.getOreName(id);
            if (allowed.contains(orename)) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean isSleepingPill(ItemStack itemStack) {
      return isItemAnOreDict(itemStack, Arrays.asList(Config.sleepingPillOredicts));
   }

   public static boolean isPill(ItemStack itemStack) {
      return isItemAnOreDict(itemStack, Arrays.asList(Config.pillOredicts));
   }
}
