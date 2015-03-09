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
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cz.ondraster.bettersleeping.api.BetterSleepingAPI;
import cz.ondraster.bettersleeping.api.PlayerData;
import cz.ondraster.bettersleeping.api.PlayerDebuff;
import cz.ondraster.bettersleeping.api.WorldSleepEvent;
import cz.ondraster.bettersleeping.client.gui.SleepOverlay;
import cz.ondraster.bettersleeping.compat.CompatibilityMorpheus;
import cz.ondraster.bettersleeping.logic.Alarm;
import cz.ondraster.bettersleeping.logic.AlternateSleep;
import cz.ondraster.bettersleeping.network.MessageUpdateTiredness;
import cz.ondraster.bettersleeping.network.Network;
import cz.ondraster.bettersleeping.proxy.ProxyCommon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

import java.util.List;

@Mod(modid = BetterSleeping.MODID, name = BetterSleeping.NAME, version = BetterSleeping.VERSION)
public class BetterSleeping {
   public static final String MODID = "bettersleeping";
   public static final String NAME = "Better Sleeping";
   public static final String AUTHOR = "OndraSter";
   public static final String VERSION = "@VERSION@";

   @SidedProxy(clientSide = "cz.ondraster.bettersleeping.proxy.ProxyClient", serverSide = "cz.ondraster.bettersleeping.proxy.ProxyCommon")
   public static ProxyCommon proxy;

   @Mod.Instance
   public static BetterSleeping INSTANCE;

   private int ticksSinceUpdate = 0;

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

   @SubscribeEvent
   public void onPreWorldTick(TickEvent.WorldTickEvent event) {
      if (!(event.world instanceof WorldServer))
         return;

      if (event.phase != TickEvent.Phase.START)
         return;

      WorldServer world = (WorldServer) event.world;

      if (world.areAllPlayersAsleep()) {
         Alarm.sleepWorld(world);
      }
   }

   @SubscribeEvent
   public void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.START)
         return;

      PlayerData data = null;
      if (event.player.worldObj.isRemote)
         return;

      if (!event.player.isEntityAlive())
         return;

      if (Config.enableSleepCounter) {
         data = BSSavedData.instance().getData(event.player);
         data.ticksSinceUpdate++;
         if (data.ticksSinceUpdate >= Config.ticksPerSleepCounter) {
            data.ticksSinceUpdate = 0;
            data.sleepCounter--;
            if (data.sleepCounter < 0)
               data.sleepCounter = 0;

         }

         if (event.player.isPlayerSleeping() && Config.giveSleepCounterOnSleep > 0) {
            data.sleepCounter += Config.giveSleepCounterOnSleep;
         }

         // send update about tiredness to the client
         if ((double) (Math.abs(data.sleepCounter - data.lastUpdate)) / Config.maximumSleepCounter >
               1.0d / SleepOverlay.MAX_OFFSET && event.player instanceof EntityPlayerMP) {
            Network.networkChannel
                  .sendTo(new MessageUpdateTiredness(data.sleepCounter), (EntityPlayerMP) event.player);
            data.lastUpdate = data.sleepCounter;
         }
      }

      if (data == null)
         return; // safety, should not happen except maybe some edge cases

      if (Config.enableDebuffs && Config.enableSleepCounter && ticksSinceUpdate > 20) {
         // check for debuffs
         List<PlayerDebuff> debuffs = BetterSleepingAPI.getDebuffs();
         for (PlayerDebuff debuff : debuffs) {
            if (debuff.enable && data.sleepCounter < debuff.tiredLevel) {
               double percentTired = (debuff.tiredLevel - data.sleepCounter) / (double) (debuff.tiredLevel);
               int scale = (int) Math.ceil(percentTired * debuff.maxScale) - 1;
               event.player.addPotionEffect(
                     new PotionEffect(debuff.potion.getId(), Config.POTION_DURATION, scale));
            }
         }

         // should fall asleep on the ground
         if (data.sleepCounter == 0 && !event.player.isPlayerSleeping() && Config.sleepOnGround) {
            boolean result = MinecraftForge.EVENT_BUS.post(new WorldSleepEvent.SleepOnGround(event.player));

            if (!result) {
               EntityPlayer.EnumStatus sleepResult = event.player.sleepInBedAt((int) event.player.posX, (int) event.player.posY, (int) event
                     .player.posZ);

               if (sleepResult == EntityPlayer.EnumStatus.OK) {
                  if (Config.enablePositionReset) {
                     ChunkCoordinates chunkCoordinates =
                           AlternateSleep.getSafePosition(event.player.worldObj, event.player.posX, event.player
                                 .posY, event.player.posZ);
                     event.player.setPosition(chunkCoordinates.posX + 0.5f, chunkCoordinates.posY + 0.1f, chunkCoordinates.posZ + 0.5f);
                  }

                  event.player.addChatMessage(new ChatComponentTranslation("msg.tooTired"));
               }
            }
         }

         ticksSinceUpdate = 0;
      }

      BSSavedData.instance().markDirty();

      ticksSinceUpdate++;
   }

   @SubscribeEvent
   public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
      if (event.entityPlayer.worldObj.isRemote)
         return;

      if (Config.enableSleepCounter) {
         PlayerData data = BSSavedData.instance().getData(event.entityPlayer);

         if (data.getSleepCounter() >= Config.maximumSleepCounter) {
            event.entityPlayer.addChatComponentMessage(new ChatComponentTranslation("msg.notTired"));
            event.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
         }
      }

      // check for amount of people sleeping in this dimension
      AlternateSleep.trySleepingWorld(event.entityPlayer.worldObj);
   }

   @SubscribeEvent
   public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
      if (event.player.worldObj == null)
         return;

      if (event.player.worldObj.isRemote)
         return;

      if (Config.percentPeopleToSleep > 1)
         return;

      AlternateSleep.trySleepingWorld(event.player.worldObj, true);
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

   @SubscribeEvent
   public void onPlayerDeath(LivingDeathEvent event) {
      if (event.entity.worldObj.isRemote)
         return;

      if (!Config.enableSleepCounter)
         return;

      if (!Config.resetCounterOnDeath)
         return;

      if (event.entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer) event.entity;
         PlayerData data = BSSavedData.instance().getPlayerData(player.getUniqueID());
         data.sleepCounter = Config.spawnSleepCounter;
         BSSavedData.instance().markDirty();
      }
   }

}
