package com.gamepedia.ftb.defaultskin;

import com.google.common.collect.Maps;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.Map;
import java.util.UUID;

@Mod(modid = "defaultskin", name = "DefaultSkin", version = "2.0.0", clientSideOnly = true, acceptedMinecraftVersions = "[1.9,1.12.2]")
public class DefaultSkinMod {
    private KeyBinding key;
    private final Map<UUID, ResourceLocation> skinCache = Maps.newHashMap();
    private boolean isEnabled = false;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        key = new KeyBinding("key.defaultskin.desc", Keyboard.KEY_RBRACKET, "key.defaultskin.category");
        ClientRegistry.registerKeyBinding(key);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onToggleKeyPress(InputEvent.KeyInputEvent event) {
        if (key.isPressed()) {
            NetHandlerPlayClient connection = Minecraft.getMinecraft().getConnection();
            if (connection != null) {
                if (isEnabled) {
                    for (Map.Entry<UUID, ResourceLocation> cache : skinCache.entrySet()) {
                        NetworkPlayerInfo info = connection.getPlayerInfo(cache.getKey());
                        //noinspection ConstantValue -- info can in fact be null.
                        if (info != null) {
                            info.playerTextures.put(MinecraftProfileTexture.Type.SKIN, cache.getValue());
                        }
                    }
                    isEnabled = false;
                } else {
                    for (NetworkPlayerInfo info : connection.getPlayerInfoMap()) {
                        UUID id = info.getGameProfile().getId();
                        if (id != null) {
                            if (!skinCache.containsKey(id)) {
                                skinCache.put(id, info.getLocationSkin());
                            }
                            info.playerTextures.put(MinecraftProfileTexture.Type.SKIN, DefaultPlayerSkin.getDefaultSkin(id));
                        }
                    }
                    isEnabled = true;
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (isEnabled && event.getEntity() instanceof AbstractClientPlayer) {
            AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();
            NetworkPlayerInfo info = player.getPlayerInfo();
            if (info != null) {
                UUID id = info.getGameProfile().getId();
                if (id != null) {
                    skinCache.put(id, info.getLocationSkin());
                    info.playerTextures.put(MinecraftProfileTexture.Type.SKIN, DefaultPlayerSkin.getDefaultSkin(id));
                }
            }
        }
    }
}
