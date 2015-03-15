package cz.ondraster.bettersleeping;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.relauncher.Side;
import cz.ondraster.bettersleeping.compat.CompatibilityMorpheus;
import cz.ondraster.bettersleeping.proxy.ProxyCommon;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

@Mod(modid = BetterSleeping.MODID, name = BetterSleeping.NAME, version = BetterSleeping.VERSION)
public class BetterSleeping {
   public static final String MODID = "bettersleeping";
   public static final String NAME = "Better Sleeping";
   public static final String AUTHOR = "OndraSter";
   public static final String VERSION = "@VERSION@";

   @SidedProxy(clientSide = "cz.ondraster.bettersleeping.proxy.ProxyClient", serverSide = "cz.ondraster.bettersleeping.proxy.ProxyCommon")
   public static ProxyCommon proxy;

   @Mod.Instance(MODID)
   public static BetterSleeping INSTANCE;


   BSSavedData playerData;


   @EventHandler
   public void preinit(FMLPreInitializationEvent event) {
      Config c = new Config(event.getSuggestedConfigurationFile().getAbsolutePath());
      proxy.preinit(event);
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      proxy.init(event);
   }

   @EventHandler
   public void postinit(FMLPostInitializationEvent event) {
      if (Loader.isModLoaded("Morpheus")) {
         CompatibilityMorpheus morpheus = new CompatibilityMorpheus();
      }
   }


   @EventHandler
   public void onServerStarted(FMLServerStartedEvent event) {
      if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
         return;

      World world = MinecraftServer.getServer().worldServers[0];

      playerData = (BSSavedData) world.loadItemData(BSSavedData.class, BetterSleeping.MODID);
      if (playerData == null) {
         playerData = new BSSavedData();
         world.setItemData(BetterSleeping.MODID, playerData);
      }
   }


}
