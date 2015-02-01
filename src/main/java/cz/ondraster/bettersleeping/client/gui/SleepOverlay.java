package cz.ondraster.bettersleeping.client.gui;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cz.ondraster.bettersleeping.BetterSleeping;
import cz.ondraster.bettersleeping.Config;
import cz.ondraster.bettersleeping.item.ItemClass;
import cz.ondraster.bettersleeping.logic.MinecraftTime;
import cz.ondraster.bettersleeping.player.SleepingProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class SleepOverlay extends OptionalGuiOverlay {
   public static final int BTN_WIDTH = 8;
   public static final int BAR_WIDTH = 32;
   public static final int MAX_OFFSET = BAR_WIDTH - BTN_WIDTH;
   public static final int BAR_HEIGHT = 8;

   public static final int ICON_WIDTH = 16;
   public static final int ICON_HEIGHT = 16;

   public static SleepingProperty playerProperty;

   @SubscribeEvent
   public void onGuiRender(RenderGameOverlayEvent event) {

      if ((event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE && event.type != RenderGameOverlayEvent.ElementType.JUMPBAR) || event.isCancelable()) {
         return;
      }

      if (playerProperty == null)
         return;

      TextureManager mgr = Minecraft.getMinecraft().renderEngine;
      mgr.bindTexture(new ResourceLocation(BetterSleeping.MODID, "textures/gui/bar.png"));

      drawTexturedModalRect(Config.guiOffsetLeft, Config.guiOffsetTop, 0, 0, BAR_WIDTH, BAR_HEIGHT);

      int takenPercent = (int) (((double) playerProperty.sleepCounter / Config.maximumSleepCounter) * MAX_OFFSET);
      if (takenPercent > MAX_OFFSET)
         takenPercent = MAX_OFFSET;

      drawTexturedModalRect(4 + takenPercent, Config.guiOffsetTop, 0, 8, BTN_WIDTH, BAR_HEIGHT);

      ItemStack bed = new ItemStack(Items.bed);

      mgr.bindTexture(TextureMap.locationItemsTexture);
      drawTexturedModelRectFromIcon(Config.guiOffsetLeft + BAR_WIDTH + 4, Config.guiOffsetTop - ((ICON_HEIGHT - BAR_HEIGHT) / 2), Items.bed.getIcon(bed, 1), ICON_WIDTH, ICON_HEIGHT);

      renderTimeOverlay();

   }

   @Optional.Method(modid = "Baubles|API")
   @Override
   public void renderTimeOverlay() {
      if (Config.enableRingWatch) {
         IInventory baubles = BaublesApi.getBaubles(Minecraft.getMinecraft().thePlayer);
         if (baubles == null)
            return; // no idea why that happens? When baubles is installed the player should have it... maybe sync issue? meh

         for (int i = 0; i < baubles.getSizeInventory(); i++) {
            ItemStack itemStack = baubles.getStackInSlot(i);
            if (itemStack == null)
               continue;

            if (itemStack.getItem() == ItemClass.itemRingWatch) {
               MinecraftTime time = MinecraftTime.getFromWorldTime(Minecraft.getMinecraft().theWorld.getWorldTime());
               drawCenteredString(Minecraft.getMinecraft().fontRenderer, time.toString(), Config.guiOffsetLeft + BAR_WIDTH / 2, Config.guiOffsetTop + 16, 0xFFFFFF);
               return;
            }
         }
      }
   }
}
