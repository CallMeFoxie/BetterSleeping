package cz.ondraster.bettersleeping.api;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerData {
   private long sleepCounter = Integer.MAX_VALUE;
   public long lastUpdate = Integer.MAX_VALUE;
   public int ticksSinceUpdate = 0;
   private int caffeineCounter = 0;

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
   public PlayerData(long sleepCounter, int caffeineCounter) {
      this.sleepCounter = sleepCounter;
      this.caffeineCounter = caffeineCounter;
   }

   public PlayerData(long sleepCounter) {
      this(sleepCounter, 0);
   }

   public void readFromNBT(NBTTagCompound compound) {
      sleepCounter = compound.getLong("sleepCounter");
      caffeineCounter = compound.getInteger("caffeineCounter");
   }

   public void writeToNBT(NBTTagCompound compound) {
      compound.setLong("sleepCounter", sleepCounter);
      compound.setInteger("caffeineCounter", caffeineCounter);
   }

   public void increaseSleepCounter() {
      increaseSleepCounter(1);
   }

   public void increaseSleepCounter(long amount) {
      this.sleepCounter += amount;
   }

   public void decreaseSleepCounter() {
      decreaseSleepCounter(1);
   }

   public void decreaseSleepCounter(long amount) {
      this.sleepCounter -= amount;
      if (this.sleepCounter < 0)
         this.sleepCounter = 0;
   }

   public void resetSleepCounter(long amount) {
      this.sleepCounter = amount;
   }

   public long getSleepCounter() {
      return sleepCounter;
   }

   public int getCaffeineCounter() {
      return caffeineCounter;
   }

   public void decreaseCaffeineLevel(int amount) {
      this.caffeineCounter -= amount;
      if (caffeineCounter < 0)
         caffeineCounter = 0;
   }

   public void decreaseCaffeineLevel() {
      decreaseCaffeineLevel(1);
   }

   public void increaseCaffeineLevel(int amount) {
      this.caffeineCounter += amount;
   }
}
