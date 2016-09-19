package com.gamepedia.ftb.defaultskin

import java.util

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin

class LoadingPlugin extends IFMLLoadingPlugin {
  override def getASMTransformerClass: Array[String] = Array(classOf[ASMTransformer].getName)
  override def injectData(data: util.Map[String, AnyRef]): Unit = {}
  override def getModContainerClass: String = classOf[DummyMod].getName
  override def getAccessTransformerClass: String = null
  override def getSetupClass: String = null
}
