package foxie.bettersleeping;

import cz.ondraster.bettersleeping.api.ISavedDataProvider;
import cz.ondraster.bettersleeping.api.PlayerData;
import cz.ondraster.bettersleeping.api.ISavedDataProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BSSavedData extends WorldSavedData implements ISavedDataProvider {

   private static HashMap<UUID, PlayerData> savedData;

   private static BSSavedData instance;

   public BSSavedData(String foo) {
      this();
   }

   public BSSavedData() {
      super(BetterSleeping.MODID);
      savedData = new HashMap<UUID, PlayerData>();
      instance = this;
   }

   @Override
   public void readFromNBT(NBTTagCompound tag) {
      NBTTagList list = tag.getTagList("PlayerData", 10);
      for (int i = 0; i < list.tagCount(); i++) {
         NBTTagCompound playerData = list.getCompoundTagAt(i);
         UUID uuid = new UUID(playerData.getLong("UUIDMost"), playerData.getLong("UUIDLeast"));
         PlayerData data = new PlayerData(playerData);
         savedData.put(uuid, data);
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound tag) {
      NBTTagList list = new NBTTagList();
      for (Map.Entry<UUID, PlayerData> entry : savedData.entrySet()) {
         NBTTagCompound compound = new NBTTagCompound();
         compound.setLong("UUIDMost", entry.getKey().getMostSignificantBits());
         compound.setLong("UUIDLeast", entry.getKey().getLeastSignificantBits());
         entry.getValue().writeToNBT(compound);
         list.appendTag(compound);
      }

      tag.setTag("PlayerData", list);
   }

   public PlayerData getPlayerData(UUID uuid) {
      PlayerData data = savedData.get(uuid);
      if (data == null) {
         data = new PlayerData(Config.spawnSleepCounter);
         savedData.put(uuid, data);
      }

      return data;
   }

   public static UUID getUUID(EntityPlayer player) {
      return player.getUniqueID();
   }

   public PlayerData getData(EntityPlayer player) {
      return getPlayerData(getUUID(player));
   }

   public static BSSavedData instance() {
      return instance;
   }
}
