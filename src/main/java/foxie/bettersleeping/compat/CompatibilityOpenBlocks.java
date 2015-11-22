package foxie.bettersleeping.compat;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import openblocks.api.SleepingBagUseEvent;

public class CompatibilityOpenBlocks {
   @SubscribeEvent
   public void onSleepingBagEvent(SleepingBagUseEvent event) {
      if (event.status == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
         // resend the event in case any other mod wants to refuse?
         PlayerSleepInBedEvent sleep = new PlayerSleepInBedEvent(event.entityPlayer, (int) event.entityPlayer.posX, (int) event.entityPlayer
               .posY, (int) event.entityPlayer.posZ);
         if (sleep.result == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW)
            return; // some mod refuses us to sleep (blood moon for example?)

         event.setResult(Event.Result.ALLOW);
      }
   }
}
