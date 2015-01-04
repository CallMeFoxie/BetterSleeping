package cz.ondraster.bettersleeping.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

public class BSTransformer implements IClassTransformer {
   @Override
   public byte[] transform(String className, String newClassName, byte[] origCode) {
      //System.out.println(className);
      if (className.equals("net.minecraft.entity.player.EntityPlayer") || className.equals("yz")) {
         return patchClass(className, origCode, true);
      }

      return origCode;
   }

   private byte[] patchClass(String className, byte[] origCode, boolean isDeobfEnv) {
      //final String methodToPatch = "b";
      final String methodToPatch = "sleepInBedAt";
      final String methodToPatch2 = "a";
      final String methodToPatch3 = "onUpdate";

      ClassReader rd = new ClassReader(origCode);
      ClassWriter wr = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
      ClassVisitor cv = new ClassVisitor(Opcodes.ASM4, wr) {
         @Override
         public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (name.equals(methodToPatch) || (name.equals(methodToPatch2) && desc.endsWith(")Lza;")) || name.equals(methodToPatch3)) {
               System.out.println("===============\nFound method to patch!");
               return new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, desc, signature, exceptions)) {
                  @Override
                  public void visitLineNumber(int line, Label start) {
                     if (line == 926) {
                        System.out.println("===============\nSuccesfuly found line 926!");
                        return;
                     }

                     super.visitLineNumber(line, start);
                  }

                  @Override
                  public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                     if (desc.equals("()Z") && name.equals("isDaytime")) {
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
