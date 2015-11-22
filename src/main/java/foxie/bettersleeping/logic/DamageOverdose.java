package foxie.bettersleeping.logic;

import net.minecraft.util.DamageSource;

public class DamageOverdose extends DamageSource {
   public DamageOverdose() {
      super("overdose");
      setDamageBypassesArmor();
   }
}
