package com.gamepedia.ftb.defaultskin;

import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "defaultskin")
@Mod("defaultskin")
public class DefaultSkinMod {
    private static final KeyMapping key = new KeyMapping("key.defaultskin.desc", GLFW.GLFW_KEY_RIGHT_BRACKET, "key.defaultskin.category");
    private static boolean isEnabled = false;

    public DefaultSkinMod(IEventBus bus) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            bus.addListener(this::registerKeyBinding);
        }
    }

    public void registerKeyBinding(RegisterKeyMappingsEvent event) {
        event.register(key);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onToggleKeyPress(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (key.consumeClick()) {
                isEnabled = !isEnabled;
            }
        }
    }

    public static boolean isEnabled() {
        return isEnabled;
    }
}
