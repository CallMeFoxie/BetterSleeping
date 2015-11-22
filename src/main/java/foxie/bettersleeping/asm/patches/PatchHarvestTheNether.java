package foxie.bettersleeping.asm.patches;

import foxie.bettersleeping.asm.MethodToPatch;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class PatchHarvestTheNether extends ClassPatch {

   public PatchHarvestTheNether(ClassWriter writer) {
      super(writer);
      matchingMethods.add(new MethodToPatch("isSurfaceWorld", "()Z"));
      matchingMethods.add(new MethodToPatch("d", "()Z"));
   }

   @Override
   public MethodVisitor patchedVisitor(MethodVisitor parent) {
      return new PatchHarvestTheNetherVisitor(parent);
   }

   public class PatchHarvestTheNetherVisitor extends MethodVisitor {
      public PatchHarvestTheNetherVisitor(MethodVisitor visitor) {
         super(Opcodes.ASM4, visitor);
      }

      @Override
      public void visitCode() {
         mv.visitCode();
         mv.visitVarInsn(Opcodes.ALOAD, 0);
         mv.visitMethodInsn(Opcodes.INVOKESTATIC, "foxie/bettersleeping/compat/HarvestTheNether", "isSurfaceWorld",
               "(Lnet/minecraft/world/WorldProvider;)Z", false);
         mv.visitInsn(Opcodes.IRETURN);
      }
   }
}
