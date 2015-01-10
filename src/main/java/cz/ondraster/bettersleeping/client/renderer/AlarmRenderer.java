package cz.ondraster.bettersleeping.client.renderer;

import cz.ondraster.bettersleeping.BetterSleeping;
import cz.ondraster.bettersleeping.client.model.ModelAlarm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class AlarmRenderer extends TileEntitySpecialRenderer {

   private final ModelAlarm modelAlarm;

   public AlarmRenderer() {
      modelAlarm = new ModelAlarm();
   }

   @Override
   public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {

      TextureManager tm = Minecraft.getMinecraft().getTextureManager();

      GL11.glPushMatrix();

      int meta = te.getBlockMetadata();
      int rotation = 0, rotationText = 180;
      if (meta == 3) {
         rotation = 180;
         rotationText = 0;
      } else if (meta == 4) {
         rotation = 90;
         rotationText = 180;
      } else if (meta == 5) {
         rotation = -90;
         rotationText = 180;
      }


      GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
      GL11.glRotatef(180f, 0f, 0f, 1f);
      GL11.glRotatef(-rotation, 0f, 1.0f, 0f);
      tm.bindTexture(new ResourceLocation(BetterSleeping.MODID, "textures/alarm.png"));
      modelAlarm.render();


/*      tm.bindTexture(new ResourceLocation(BetterSleeping.MODID, "textures/gui/alarm.png"));
      MinecraftTime time = MinecraftTime.getFromWorldTime(te.getWorldObj().getWorldTime());

      // render 10s of hours
      int nbr = time.getRealHour() % 10;
      GL11.glTranslatef((float) x, (float) y + 4f, (float) z);
      GL11.glRotatef(180f, 0f, 1.0f, 0f);
      drawCharacter(x, y, z, nbr);*/
      GL11.glPopMatrix();
   }

   private void drawCharacter(double x, double y, double z, int character) {
      double width = 8 / 128d;
      double height = 8 / 64d;
      double u = 16 / 256d;
      double v = 103 / 256d;
      double nmrWidth = 9 / 256d;
      Tessellator tessellator = Tessellator.instance;

      tessellator.startDrawingQuads();

      u += nmrWidth * 4;
      tessellator.addVertexWithUV(x, y, z, u + width, v);
      tessellator.addVertexWithUV(x + 0.5, y, z, u, v);
      tessellator.addVertexWithUV(x + 0.5, y + 0.5, z, u, v + height);
      tessellator.addVertexWithUV(x, y + 0.5, z, u + width, v + 0.5);

      tessellator.draw();
   }
}
