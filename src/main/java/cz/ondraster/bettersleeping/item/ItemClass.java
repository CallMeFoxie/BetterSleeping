package cz.ondraster.bettersleeping.item;

import cz.ondraster.bettersleeping.Config;
import cz.ondraster.bettersleeping.Registrator;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

public class ItemClass {
   public static Item itemRingWatch;
   public static Item itemPillSleeping;
   public static Item itemPillCaffeine;

   public static void register() {
      if (Config.enableRingWatch) {
         itemRingWatch = new ItemRingWatch();
         Registrator.registerItem(itemRingWatch);
      }

      if (Config.sleepingPillAmount > 0) {
         itemPillSleeping = new ItemPill().setTextureName("pill_sleeping").setUnlocalizedName("pillSleeping");
         Registrator.registerItem(itemPillSleeping);
         OreDictionary.registerOre("pillSleeping", itemPillSleeping);
         OreDictionary.registerOre("pill", itemPillSleeping);
      }

      if (Config.caffeinePillAmount > 0) {
         itemPillCaffeine = new ItemPill().setTextureName("pill_caffeine").setUnlocalizedName("pillCaffeine");
         Registrator.registerItem(itemPillCaffeine);
         OreDictionary.registerOre("pillCaffeine", itemPillCaffeine);
         OreDictionary.registerOre("pill", itemPillCaffeine);
      }
   }
}
