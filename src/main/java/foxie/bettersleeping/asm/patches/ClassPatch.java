package foxie.bettersleeping.asm.patches;

import foxie.bettersleeping.asm.MethodToPatch;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashSet;
import java.util.Set;

public abstract class ClassPatch extends ClassVisitor {

   public Set<MethodToPatch> matchingMethods = new HashSet<MethodToPatch>();

   public ClassPatch(ClassWriter writer) {
      super(Opcodes.ASM4, writer);
   }

   public abstract MethodVisitor patchedVisitor(MethodVisitor parent);

   @Override
   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      for (MethodToPatch patch : matchingMethods) {
         if (patch.matchesMethod(name, desc))
            return patchedVisitor(super.visitMethod(access, name, desc, signature, exceptions));
      }

      return super.visitMethod(access, name, desc, signature, exceptions);
   }
}
