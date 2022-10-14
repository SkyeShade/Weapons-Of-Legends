package net.skyeshade.wol.thirst.event;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.client.ThirstHudOverlay;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.DrinkWaterC2SPacket;
import net.skyeshade.wol.util.KeyBinding;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = WOL.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.DRINKING_KEY.consumeClick()) {
                ModMessages.sendToServer(new DrinkWaterC2SPacket());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = WOL.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.DRINKING_KEY);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("thirst", ThirstHudOverlay.HUD_THIRST);
        }
    }
}