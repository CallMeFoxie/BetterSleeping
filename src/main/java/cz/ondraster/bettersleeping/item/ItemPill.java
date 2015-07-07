package cz.ondraster.bettersleeping.item;

import net.minecraft.item.ItemFood;

public class ItemPill extends ItemFood {
   public ItemPill() {
      super(0, 0, false);
      setAlwaysEdible();
   }
}
