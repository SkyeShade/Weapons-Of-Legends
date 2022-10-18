package net.skyeshade.wol.stats.event;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.client.HudOverlay;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.mana.UpdateManaC2SPacket;
import net.skyeshade.wol.networking.packet.mana.UpdateMaxManaC2SPacket;
import net.skyeshade.wol.networking.packet.manacore.UpdateManaCoreC2SPacket;
import net.skyeshade.wol.networking.packet.manacore.UpdateManaCoreExhaustionC2SPacket;
import net.skyeshade.wol.util.KeyBinding;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = WOL.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.MANASUB_KEY.consumeClick()) {
                ModMessages.sendToServer(new UpdateManaC2SPacket());

                ModMessages.sendToServer(new UpdateManaCoreC2SPacket());

                ModMessages.sendToServer(new UpdateManaCoreExhaustionC2SPacket());
            }
            if(KeyBinding.MAXMANAADD_KEY.consumeClick()) {
                ModMessages.sendToServer(new UpdateMaxManaC2SPacket());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = WOL.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {

            event.register(KeyBinding.MANASUB_KEY);

            event.register(KeyBinding.MAXMANAADD_KEY);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("stats", HudOverlay.HUD_STATS);
        }
    }
}