package cz.ondraster.bettersleeping.api;

import cz.ondraster.bettersleeping.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class SleepingProperty implements IExtendedEntityProperties {
   public long sleepCounter = Config.spawnSleepCounter;
   public long lastUpdate = Integer.MAX_VALUE;
   public static final String EXT_PROP_NAME = "SleepingProperty";
   public int ticksSinceUpdate = 0;

   EntityPlayer player;

   public SleepingProperty(EntityPlayer player) {
      this.player = player;
   }

   @Override
   public void saveNBTData(NBTTagCompound compound) {
      NBTTagCompound properties = new NBTTagCompound();
      properties.setLong("sleepCounter", sleepCounter);
      compound.setTag(EXT_PROP_NAME, properties);
   }

   @Override
   public void loadNBTData(NBTTagCompound compound) {
      NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
      if (properties == null)
         sleepCounter = Config.spawnSleepCounter;
      else
         sleepCounter = properties.getLong("sleepCounter");
   }

   @Override
   public void init(Entity entity, World world) {

   }

   public static void register(EntityPlayer player) {
      player.registerExtendedProperties(EXT_PROP_NAME, new SleepingProperty(player));
   }

   public static SleepingProperty get(EntityPlayer player) {
      return (SleepingProperty) player.getExtendedProperties(EXT_PROP_NAME);
   }
}
