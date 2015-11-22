package foxie.bettersleeping.proxy;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import foxie.bettersleeping.Config;
import foxie.bettersleeping.client.gui.SleepOverlay;
import foxie.bettersleeping.client.renderer.AlarmRenderer;
import foxie.bettersleeping.tileentity.TileEntityAlarm;
import foxie.bettersleeping.client.renderer.AlarmRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class ProxyClient extends ProxyCommon {

   public void registerTESR() {
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAlarm.class, new AlarmRenderer());
   }

   @Override
   public void preinit(FMLPreInitializationEvent event) {
      super.preinit(event);
   }

   @Override
   public void init(FMLInitializationEvent event) {
      super.init(event);
      registerTESR();
      if (Config.enableSleepyBar)
         MinecraftForge.EVENT_BUS.register(new SleepOverlay());
   }

   @Override
   public EntityPlayer getPlayer() {
      return FMLClientHandler.instance().getClientPlayerEntity();
   }
}
