package cz.ondraster.bettersleeping;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerData {
   public long sleepCounter = Config.spawnSleepCounter;
   public long lastUpdate = Integer.MAX_VALUE;
   public int ticksSinceUpdate = 0;

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
