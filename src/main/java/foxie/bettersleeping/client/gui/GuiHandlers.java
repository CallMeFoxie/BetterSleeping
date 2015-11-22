package foxie.bettersleeping.client.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import foxie.bettersleeping.tileentity.TileEntityAlarm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandlers implements IGuiHandler {
   public static final int GUI_ALARM = 1;

   @Override
   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      switch (ID) {
         case GUI_ALARM:
            return null;
      }

      return null;
   }

   @Override
   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      switch (ID) {
         case GUI_ALARM:
            return new GuiAlarm((TileEntityAlarm) world.getTileEntity(x, y, z));
      }

      return null;
   }
}
