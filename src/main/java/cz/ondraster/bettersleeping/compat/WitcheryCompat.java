package cz.ondraster.bettersleeping.compat;

import cz.ondraster.bettersleeping.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class WitcheryCompat implements IExtendedEntityProperties {
   private final EntityPlayer player;
   private static int vampireLevel, werewolfLevel = 0;
   public boolean isSupernatural;

   public WitcheryCompat(EntityPlayer player, boolean isSupernatural) {
      this.player = player;
      this.isSupernatural = isSupernatural;
   }


   @Override
   public void saveNBTData(NBTTagCompound compound) {
   }

   @Override
   public void loadNBTData(NBTTagCompound compound) {
      NBTTagCompound witcheryData = (NBTTagCompound) compound.getTag("WitcheryExtendedPlayer");
      vampireLevel = witcheryData.getInteger("VampireLevel");
      werewolfLevel = witcheryData.getInteger("WerewolfLevel");
   }

   @Override
   public void init(Entity entity, World world) {
   }

   public boolean isSupernatural (){
      if (!Config.supernaturalImmunity) {
         return false;
      }
      else if ( vampireLevel > 0 || werewolfLevel > 0) {
         return true;
      }
      else {
         return false;
      }
   }
}
