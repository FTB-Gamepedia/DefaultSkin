package com.gamepedia.ftb.defaultskin

import jdk.internal.org.objectweb.asm.{ClassReader, ClassWriter, Opcodes}
import jdk.internal.org.objectweb.asm.tree.{AbstractInsnNode, ClassNode}
import net.minecraft.launchwrapper.IClassTransformer
import net.minecraftforge.fml.common.FMLLog

import scala.collection.JavaConversions.asScalaBuffer

class ASMTransformer extends IClassTransformer {
  override def transform(name: String, transformedName: String, bytes: Array[Byte]): Array[Byte] = {
    val obf = name.equals("bnk")
    if (obf || name.equals("net.minecraft.client.entity.AbstractClientPlayer")) {
      val methodName = if (obf) "o" else "getLocationSkin"
      val methodDesc = "()" + (if (obf) "Lkn" else "Lnet/minecraft/util/ResourceLocation") + ";"

      val classNode = new ClassNode
      val classReader = new ClassReader(bytes)
      classReader.accept(classNode, 0)

      val methodNodeOption = asScalaBuffer(classNode.methods).find(
        (m) => m.name.equals(methodName) && m.desc.equals(methodDesc))

      if (methodNodeOption.isEmpty) {
        FMLLog.severe("[DefaultSkin] AbstractClientPlayer#getLocation() not found! This is likely a bug!")
        return bytes
      }

      val methodNode = methodNodeOption.get

      val iterator = methodNode.instructions.iterator()
      def getNonnull: Option[AbstractInsnNode] = {
        while (iterator.hasNext) {
          val insnNode: AbstractInsnNode = iterator.next()
          if (insnNode.getOpcode == Opcodes.IFNONNULL) {
            return Some(insnNode)
          }
        }
        None
      }

      val ifNonnullOption = getNonnull
      if (ifNonnullOption.isEmpty) {
        FMLLog.severe("[DefaultSkin] IFNONNULL Check not found in AbstractClientPlayer#getLocationSkin. This is likely a bug!")
        return bytes
      }

      val ifNonnull = ifNonnullOption.get

      methodNode.instructions.remove(ifNonnull)

      val writer = new ClassWriter(ClassWriter.COMPUTE_MAXS)
      classNode.accept(writer)
      val classNodeByteArray = writer.toByteArray
      FMLLog.info("[DefaultSkin] Manipulated AbstractClientPlayer#getLocationSkin successfully.")
      return classNodeByteArray
    }

    bytes
  }
}
