package cz.ondraster.bettersleeping.logic;

import cz.ondraster.bettersleeping.BSSavedData;
import cz.ondraster.bettersleeping.Config;
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

   public static void checkDebuff(EntityPlayer player) {
      PlayerData data = BSSavedData.instance().getData(player);

      if (data.getCaffeineCounter() >= Config.deathFromCaffeineOverdose && Config.deathFromCaffeineOverdose > 0) {
         player.attackEntityFrom(damageCaffeine, 9001);
      }

      if (data.getCaffeineCounter() >= Config.caffeineDebuffsAt) {
         player.addPotionEffect(new PotionEffect(Potion.confusion.getId(), Config.POTION_DURATION * 2));
      }

      data.decreaseCaffeineLevel();
   }

   public static boolean isCoffee(ItemStack itemStack) {
      int[] ids = OreDictionary.getOreIDs(itemStack);
      if (ids.length > 0) {
         List<String> allowed = Arrays.asList(Config.allowedNames);
         for (int i = 0; i < ids.length; i++) {
            String orename = OreDictionary.getOreName(ids[i]);
            if (allowed.contains(orename)) {
               return true;
            }
         }
      }

      return false;
   }
}
