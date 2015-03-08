package cz.ondraster.bettersleeping.api;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerData {
   public long sleepCounter = Integer.MAX_VALUE;
   public long lastUpdate = Integer.MAX_VALUE;
   public int ticksSinceUpdate = 0;

   /*
    * Constructor when being loaded from NBT
    */
   public PlayerData(NBTTagCompound compound) {
      readFromNBT(compound);
   }

   /*
    * Constructor when being newly set up.
    * Param sleepCounter specifies the spawn sleep counter (usually loaded from config)
    */
   public PlayerData(long sleepCounter) {
      this.sleepCounter = sleepCounter;
   }

   public void readFromNBT(NBTTagCompound compound) {
      sleepCounter = compound.getLong("sleepCounter");
   }

   public void writeToNBT(NBTTagCompound compound) {
      compound.setLong("sleepCounter", sleepCounter);
   }

   public long getSleepCounter() {
      return sleepCounter;
   }
}
