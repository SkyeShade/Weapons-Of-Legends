package net.skyeshade.wol.stats.event;


import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.client.ClientStatsData;
import net.skyeshade.wol.client.HudOverlay;
import net.skyeshade.wol.client.gui.screens.stats.StatsScreen;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.destruction.UpdateDestructionActiveC2SPacket;
import net.skyeshade.wol.networking.packet.mana.UpdateManaC2SPacket;
import net.skyeshade.wol.networking.packet.mana.UpdateMaxManaC2SPacket;
import net.skyeshade.wol.networking.packet.manacore.UpdateManaBarrierC2SPacket;
import net.skyeshade.wol.networking.packet.manacore.UpdateManaBarrierReviveC2SPacket;
import net.skyeshade.wol.networking.packet.manacore.UpdateMaxManaBarrierC2SPacket;
import net.skyeshade.wol.util.KeyBinding;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = WOL.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.MANASUB_KEY.consumeClick()) {
                ModMessages.sendToServer(new UpdateManaC2SPacket(1000));

                //ModMessages.sendToServer(new UpdateManaBarrierC2SPacket(10));

                //ModMessages.sendToServer(new UpdateManaBarrierReviveC2SPacket(1));

                if (ClientStatsData.getPlayerDestructionActive()) {
                    ModMessages.sendToServer(new UpdateDestructionActiveC2SPacket(false));
                }else {
                    ModMessages.sendToServer(new UpdateDestructionActiveC2SPacket(true));
                }
                //ModMessages.sendToServer(new UpdateManaCoreLevelC2SPacket(1));
            }
            if(KeyBinding.MAXMANAADD_KEY.consumeClick()) {
                Minecraft.getInstance().setScreen(new StatsScreen());

                //ModMessages.sendToServer(new UpdateMaxManaC2SPacket(1000));
                //ModMessages.sendToServer(new UpdateMaxManaBarrierC2SPacket(1000));
            }


            if (ClientStatsData.getPlayerDestructionActive()) {
                if (Minecraft.getInstance().options.keyMappings[25].consumeClick()) {

                    System.out.println("TEST1");
                    ModMessages.sendToServer(new UpdateMaxManaC2SPacket(0));

                }
                if (Minecraft.getInstance().options.keyMappings[26].consumeClick()) {

                    System.out.println("TEST2");

                }
                if (Minecraft.getInstance().options.keyMappings[27].consumeClick()) {

                    System.out.println("TEST3");

                }
                if (Minecraft.getInstance().options.keyMappings[28].consumeClick()) {

                    System.out.println("TEST4");

                }
                if (Minecraft.getInstance().options.keyMappings[29].consumeClick()) {

                    System.out.println("TEST5");

                }
                if (Minecraft.getInstance().options.keyMappings[30].consumeClick()) {

                    System.out.println("TEST6");

                }
                if (Minecraft.getInstance().options.keyMappings[31].consumeClick()) {

                    System.out.println("TEST7");

                }
                if (Minecraft.getInstance().options.keyMappings[32].consumeClick()) {

                    System.out.println("TEST8");

                }
                if (Minecraft.getInstance().options.keyMappings[33].consumeClick()) {

                    System.out.println("TEST9");

                }



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