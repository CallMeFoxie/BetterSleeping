package foxie.bettersleeping.item;

import foxie.bettersleeping.BetterSleeping;
import foxie.bettersleeping.Config;
import foxie.bettersleeping.Registrator;
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
         itemPillSleeping = new ItemPill().setTextureName(BetterSleeping.MODID + ":pill").setUnlocalizedName("pillSleeping");
         Registrator.registerItem(itemPillSleeping);
         OreDictionary.registerOre("pillSleeping", itemPillSleeping);
         OreDictionary.registerOre("pill", itemPillSleeping);
      }

      if (Config.caffeinePillAmount > 0) {
         itemPillCaffeine = new ItemPill().setTextureName(BetterSleeping.MODID + ":pill").setUnlocalizedName("pillCaffeine");
         Registrator.registerItem(itemPillCaffeine);
         OreDictionary.registerOre("pillCaffeine", itemPillCaffeine);
         OreDictionary.registerOre("pill", itemPillCaffeine);
      }
   }
}
