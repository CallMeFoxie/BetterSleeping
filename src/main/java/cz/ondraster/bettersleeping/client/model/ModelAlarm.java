package cz.ondraster.bettersleeping.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelAlarm extends ModelBase {
   ModelRenderer base;
   ModelRenderer display;

   public ModelAlarm() {
      textureWidth = 64;
      textureHeight = 32;

      base = new ModelRenderer(this, 0, 0);
      base.addBox(-8F, 0F, -2F, 16, 2, 6);
      base.setRotationPoint(0F, 22F, 0F);
      base.setTextureSize(64, 32);
      base.mirror = true;
      setRotation(base, 0F, 0F, 0F);
      display = new ModelRenderer(this, 0, 8);
      display.addBox(-7F, -8F, 0F, 14, 8, 1);
      display.setRotationPoint(0F, 22F, -1.5F);
      display.setTextureSize(64, 32);
      display.mirror = true;
      setRotation(display, -0.418879F, 0F, 0F);
   }

   public void render() {
      float f5 = 0.0625f;
      base.render(f5);
      display.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
