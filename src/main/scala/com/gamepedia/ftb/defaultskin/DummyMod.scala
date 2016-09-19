package com.gamepedia.ftb.defaultskin

import java.util

import com.google.common.eventbus.EventBus
import net.minecraftforge.fml.common.{DummyModContainer, LoadController, ModMetadata}

class DummyMod extends DummyModContainer(new ModMetadata) {
  val meta = getMetadata
  meta.modId = "defaultskin"
  meta.name = "DefaultSkin"
  meta.version = "1.0.0"
  meta.authorList = util.Arrays.asList("SatanicSanta")
  meta.url = "https://minecraft.curseforge.com/projects/defaultskin"
  meta.description = "A small client-side mod that disables custom player skins."

  override def registerBus(bus: EventBus, controller: LoadController): Boolean = {
    bus.register(this)
    true
  }
}
