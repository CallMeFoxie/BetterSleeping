package cz.ondraster.bettersleeping.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAlarm extends TileEntity {
   private int hour;
   private int minute;

   public TileEntityAlarm() {

   }

   @Override
   public void readFromNBT(NBTTagCompound nbt) {
      super.readFromNBT(nbt);
      hour = nbt.getInteger("hour");
      minute = nbt.getInteger("minute");
   }

   @Override
   public void writeToNBT(NBTTagCompound nbt) {
      super.writeToNBT(nbt);
      nbt.setInteger("hour", hour);
      nbt.setInteger("minute", minute);
   }

   @Override
   public boolean canUpdate() {
      return false;
   }

   public void incHour() {
      hour++;
      if (hour >= 24)
         hour = 0;
   }

   public void decHour() {
      hour--;
      if (hour < 0)
         hour = 23;
   }

   public void incMinute() {
      minute++;
      if (minute >= 60)
         minute = 0;
   }

   public void decMinute() {
      minute--;
      if (minute < 0)
         minute = 59;
   }

   public int getHour() {
      return hour;
   }

   public int getMinute() {
      return minute;
   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound nbt = new NBTTagCompound();
      this.writeToNBT(nbt);
      return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, worldObj.provider.dimensionId, nbt);
   }

   @Override
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      readFromNBT(pkt.func_148857_g());
   }
}
