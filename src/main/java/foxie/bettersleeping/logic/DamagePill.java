package foxie.bettersleeping.logic;

import net.minecraft.util.DamageSource;

public class DamagePill extends DamageSource {
   public DamagePill() {
      super("pill");
      setDamageBypassesArmor();
   }
}
