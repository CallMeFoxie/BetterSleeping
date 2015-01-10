package cz.ondraster.bettersleeping.item;

import cz.ondraster.bettersleeping.Config;
import cz.ondraster.bettersleeping.Registrator;
import net.minecraft.item.Item;

public class ItemClass {
   public static Item itemRingWatch;

   public static void register() {
      if (Config.enableRingWatch) {
         itemRingWatch = new ItemRingWatch();
         Registrator.registerItem(itemRingWatch);
      }
   }
}
