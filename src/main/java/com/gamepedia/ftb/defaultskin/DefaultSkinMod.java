package com.gamepedia.ftb.defaultskin;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = "defaultskin")
@Mod("defaultskin")
public class DefaultSkinMod {
    private static final KeyMapping key = new KeyMapping("key.defaultskin.desc", GLFW.GLFW_KEY_RIGHT_BRACKET, "key.defaultskin.category");
    private static boolean isEnabled = false;

    public DefaultSkinMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> (DistExecutor.SafeRunnable) () -> bus.addListener(this::registerKeyBinding));
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
