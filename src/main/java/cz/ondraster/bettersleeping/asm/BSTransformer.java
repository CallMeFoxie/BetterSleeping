package cz.ondraster.bettersleeping.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

public class BSTransformer implements IClassTransformer {
   @Override
   public byte[] transform(String className, String newClassName, byte[] origCode) {
      //System.out.println(className);
      if (className.equals("net.minecraft.entity.player.EntityPlayer") || className.equals("yz")) {
         return patchClass(className, origCode);
      }

      return origCode;
   }

   private byte[] patchClass(String className, byte[] origCode) {
      //final String methodToPatch = "b";
      final String methodToPatch = "sleepInBedAt";
      final String methodToPatch2 = "a";
      final String methodToPatch3 = "onUpdate";
      final String methodToPatch4 = "h";

      ClassReader rd = new ClassReader(origCode);
      ClassWriter wr = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
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
                        super.visitMethodInsn(Opcodes.INVOKESTATIC, "cz/ondraster/bettersleeping/logic/Alarm", "canNotSleep", desc, false);
                     } else {
                        super.visitMethodInsn(opcode, owner, name, desc, itf);
                     }
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
