package net.skyeshade.wol.stats.event;


import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.client.ClientStatsData;
import net.skyeshade.wol.client.HudOverlay;
import net.skyeshade.wol.client.gui.screens.stats.StatsScreen;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.spellslots.UpdateSpellSlotsToggleC2SPacket;
import net.skyeshade.wol.networking.packet.spellfire.SpellCastingC2SPacket;
import net.skyeshade.wol.util.KeyBinding;

public class ClientEvents {
    static int delay = 10;

    @Mod.EventBusSubscriber(modid = WOL.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.SPELLSLOTS_TOGGLE_KEY.consumeClick()) {

                if (ClientStatsData.getPlayerSpellSlotsToggle()) {
                    ModMessages.sendToServer(new UpdateSpellSlotsToggleC2SPacket(false));
                }else {
                    ModMessages.sendToServer(new UpdateSpellSlotsToggleC2SPacket(true));
                }
                //ModMessages.sendToServer(new UpdateManaCoreLevelC2SPacket(1));
            }
            if(KeyBinding.MAXMANAADD_KEY.consumeClick()) {
                Minecraft.getInstance().setScreen(new StatsScreen());

                //ModMessages.sendToServer(new UpdateMaxManaC2SPacket(1000));
                //ModMessages.sendToServer(new UpdateMaxManaBarrierC2SPacket(1000));
            }


            if (ClientStatsData.getPlayerSpellSlotsToggle()) {

                if (Minecraft.getInstance().options.keyMappings[25].consumeClick()) {


                    ModMessages.sendToServer(new SpellCastingC2SPacket(ClientStatsData.getPlayerSpellSlots()[0]));


                }
                //Minecraft.getInstance().options.keyMappings[25].consumeClick();
                if (Minecraft.getInstance().options.keyMappings[26].consumeClick()) {

                    ModMessages.sendToServer(new SpellCastingC2SPacket(ClientStatsData.getPlayerSpellSlots()[1]));

                }
                if (Minecraft.getInstance().options.keyMappings[27].consumeClick()) {

                    ModMessages.sendToServer(new SpellCastingC2SPacket(ClientStatsData.getPlayerSpellSlots()[2]));

                }
                if (Minecraft.getInstance().options.keyMappings[28].consumeClick()) {

                    ModMessages.sendToServer(new SpellCastingC2SPacket(ClientStatsData.getPlayerSpellSlots()[3]));

                }
                if (Minecraft.getInstance().options.keyMappings[29].consumeClick()) {

                    ModMessages.sendToServer(new SpellCastingC2SPacket(ClientStatsData.getPlayerSpellSlots()[4]));

                }
                if (Minecraft.getInstance().options.keyMappings[30].consumeClick()) {

                    ModMessages.sendToServer(new SpellCastingC2SPacket(ClientStatsData.getPlayerSpellSlots()[5]));

                }
                if (Minecraft.getInstance().options.keyMappings[31].consumeClick()) {

                    ModMessages.sendToServer(new SpellCastingC2SPacket(ClientStatsData.getPlayerSpellSlots()[6]));

                }
                if (Minecraft.getInstance().options.keyMappings[32].consumeClick()) {

                    ModMessages.sendToServer(new SpellCastingC2SPacket(ClientStatsData.getPlayerSpellSlots()[7]));

                }
                if (Minecraft.getInstance().options.keyMappings[33].consumeClick()) {

                    ModMessages.sendToServer(new SpellCastingC2SPacket(ClientStatsData.getPlayerSpellSlots()[8]));

                }



            }

        }
    }
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {

        //System.out.println(isDown);
    }

    @Mod.EventBusSubscriber(modid = WOL.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {

            event.register(KeyBinding.SPELLSLOTS_TOGGLE_KEY);

            event.register(KeyBinding.MAXMANAADD_KEY);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("stats", HudOverlay.HUD_STATS);
        }
    }
}