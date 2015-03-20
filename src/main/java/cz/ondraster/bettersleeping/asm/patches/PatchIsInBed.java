package cz.ondraster.bettersleeping.asm.patches;

import cz.ondraster.bettersleeping.asm.MethodToPatch;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class PatchIsInBed extends ClassPatch {

   public PatchIsInBed(ClassWriter writer) {
      super(writer);
      matchingMethods.add(new MethodToPatch("isInBed", "()Z"));
      matchingMethods.add(new MethodToPatch("j", "()Z"));
   }

   @Override
   public MethodVisitor patchedVisitor(MethodVisitor parent) {
      return new PatchIsInBedVisitor(parent);
   }

   public class PatchIsInBedVisitor extends MethodVisitor {
      public PatchIsInBedVisitor(MethodVisitor visitor) {
         super(Opcodes.ASM4, visitor);
      }

      @Override
      public void visitCode() {
         mv.visitCode();
         mv.visitVarInsn(Opcodes.ALOAD, 0);
         mv.visitMethodInsn(Opcodes.INVOKESTATIC, "cz/ondraster/bettersleeping/logic/Alarm", "canSleep",
               "(Lnet/minecraft/entity/player/EntityPlayer;)Z", false);
         Label l1 = new Label();
         mv.visitJumpInsn(Opcodes.IFEQ, l1);
         mv.visitInsn(Opcodes.ICONST_1);
         mv.visitInsn(Opcodes.IRETURN);
         mv.visitLabel(l1);
      }
   }

}
