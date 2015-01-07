package cz.ondraster.bettersleeping.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

public class BSTransformer implements IClassTransformer {
   @Override
   public byte[] transform(String className, String newClassName, byte[] origCode) {
      //System.out.println(className);
      if (className.equals("net.minecraft.entity.player.EntityPlayer") || className.equals("yz")) {
         return patchIsDaytime(className, origCode);
      }

      return origCode;
   }

   private byte[] patchIsDaytime(String className, byte[] origCode) {
      final String methodToPatch = "sleepInBedAt";
      final String methodToPatch2 = "a";
      final String methodToPatch3 = "onUpdate";
      final String methodToPatch4 = "h";

      final String methodToPatch5 = "isInBed";
      final String methodToPatch6 = "j";

      ClassReader rd = new ClassReader(origCode);
      ClassWriter wr = new ClassWriter(ClassWriter.COMPUTE_MAXS);
      ClassVisitor cv = new ClassVisitor(Opcodes.ASM4, wr) {
         @Override
         public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (name.equals(methodToPatch) || (name.equals(methodToPatch2) && desc.endsWith(")Lza;")) ||
                  name.equals(methodToPatch3) || (name.equals(methodToPatch4) && desc.equals("()V"))) {
               return new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, desc, signature, exceptions)) {
                  @Override
                  public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                     if (desc.equals("()Z") && (name.equals("isDaytime") || name.equals("w"))) {
                        super.visitInsn(Opcodes.POP);
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        super.visitMethodInsn(Opcodes.INVOKESTATIC, "cz/ondraster/bettersleeping/logic/Alarm", "canNotSleep", "(Lnet/minecraft/entity/player/EntityPlayer;)Z", false);
                     } else {
                        super.visitMethodInsn(opcode, owner, name, desc, itf);
                     }
                  }
               };
            } else if (name.equals(methodToPatch5) || (name.equals(methodToPatch6) && desc.equals("()Z"))) {
               return new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, desc, signature, exceptions)) {
                  @Override
                  public void visitCode() {
                     mv.visitCode();
                     mv.visitVarInsn(Opcodes.ALOAD, 0);
                     mv.visitMethodInsn(Opcodes.INVOKESTATIC, "cz/ondraster/bettersleeping/logic/Alarm", "canSleep", "(Lnet/minecraft/entity/player/EntityPlayer;)Z", false);
                     Label l1 = new Label();
                     mv.visitJumpInsn(Opcodes.IFEQ, l1);
                     mv.visitInsn(Opcodes.ICONST_1);
                     mv.visitInsn(Opcodes.IRETURN);
                     mv.visitLabel(l1);
                  }
               };
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
         }
      };
      rd.accept(cv, ClassReader.EXPAND_FRAMES);
      return wr.toByteArray();
   }
}
