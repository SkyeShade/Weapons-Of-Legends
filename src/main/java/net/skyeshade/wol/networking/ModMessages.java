package net.skyeshade.wol.networking;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.skyeshade.wol.WOL;

import net.skyeshade.wol.networking.packet.AnimateHurtDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.destruction.DestructionActiveDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.destruction.UpdateDestructionActiveC2SPacket;
import net.skyeshade.wol.networking.packet.hp.HpDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.hp.MaxHpDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.hp.UpdateHpC2SPacket;
import net.skyeshade.wol.networking.packet.hp.UpdateMaxHpC2SPacket;
import net.skyeshade.wol.networking.packet.mana.ManaDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.mana.MaxManaDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.mana.UpdateManaC2SPacket;
import net.skyeshade.wol.networking.packet.mana.UpdateMaxManaC2SPacket;
import net.skyeshade.wol.networking.packet.manacore.*;
import net.skyeshade.wol.networking.packet.menutoggle.MenuStatTabToggleDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.spellfire.extras.CastingAmountDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.spellfire.extras.UpdateCastingAmountC2SPacket;
import net.skyeshade.wol.networking.packet.spellslots.SpellSlotsDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.spellslots.SpellSlotsToggleDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.menutoggle.UpdateMenuStatTabToggleC2SPacket;
import net.skyeshade.wol.networking.packet.spellslots.UpdateSpellSlotsC2SPacket;
import net.skyeshade.wol.networking.packet.spellslots.UpdateSpellSlotsToggleC2SPacket;
import net.skyeshade.wol.networking.packet.spellfire.SpellCastingC2SPacket;
import net.skyeshade.wol.networking.packet.spellstatupdate.SpellPowerLevelDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.spellstatupdate.SpellRangeDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.spellstatupdate.UpdateSpellPowerLevelC2SPacket;
import net.skyeshade.wol.networking.packet.spellstatupdate.UpdateSpellRangeC2SPacket;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(WOL.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;


        net.messageBuilder(UpdateManaC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateManaC2SPacket::new)
                .encoder(UpdateManaC2SPacket::toBytes)
                .consumerMainThread(UpdateManaC2SPacket::handle)
                .add();

        net.messageBuilder(ManaDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ManaDataSyncS2CPacket::new)
                .encoder(ManaDataSyncS2CPacket::toBytes)
                .consumerMainThread(ManaDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(UpdateMaxManaC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateMaxManaC2SPacket::new)
                .encoder(UpdateMaxManaC2SPacket::toBytes)
                .consumerMainThread(UpdateMaxManaC2SPacket::handle)
                .add();

        net.messageBuilder(MaxManaDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MaxManaDataSyncS2CPacket::new)
                .encoder(MaxManaDataSyncS2CPacket::toBytes)
                .consumerMainThread(MaxManaDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(UpdateManaBarrierC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateManaBarrierC2SPacket::new)
                .encoder(UpdateManaBarrierC2SPacket::toBytes)
                .consumerMainThread(UpdateManaBarrierC2SPacket::handle)
                .add();

        net.messageBuilder(ManaBarrierDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ManaBarrierDataSyncS2CPacket::new)
                .encoder(ManaBarrierDataSyncS2CPacket::toBytes)
                .consumerMainThread(ManaBarrierDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(UpdateMaxManaBarrierC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateMaxManaBarrierC2SPacket::new)
                .encoder(UpdateMaxManaBarrierC2SPacket::toBytes)
                .consumerMainThread(UpdateMaxManaBarrierC2SPacket::handle)
                .add();

        net.messageBuilder(MaxManaBarrierDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MaxManaBarrierDataSyncS2CPacket::new)
                .encoder(MaxManaBarrierDataSyncS2CPacket::toBytes)
                .consumerMainThread(MaxManaBarrierDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(UpdateManaBarrierReviveC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateManaBarrierReviveC2SPacket::new)
                .encoder(UpdateManaBarrierReviveC2SPacket::toBytes)
                .consumerMainThread(UpdateManaBarrierReviveC2SPacket::handle)
                .add();

        net.messageBuilder(ManaBarrierReviveDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ManaBarrierReviveDataSyncS2CPacket::new)
                .encoder(ManaBarrierReviveDataSyncS2CPacket::toBytes)
                .consumerMainThread(ManaBarrierReviveDataSyncS2CPacket::handle)
                .add();

        //
        net.messageBuilder(UpdateDestructionActiveC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateDestructionActiveC2SPacket::new)
                .encoder(UpdateDestructionActiveC2SPacket::toBytes)
                .consumerMainThread(UpdateDestructionActiveC2SPacket::handle)
                .add();

        net.messageBuilder(DestructionActiveDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DestructionActiveDataSyncS2CPacket::new)
                .encoder(DestructionActiveDataSyncS2CPacket::toBytes)
                .consumerMainThread(DestructionActiveDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(UpdateManaCoreLevelC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateManaCoreLevelC2SPacket::new)
                .encoder(UpdateManaCoreLevelC2SPacket::toBytes)
                .consumerMainThread(UpdateManaCoreLevelC2SPacket::handle)
                .add();

        net.messageBuilder(ManaCoreLevelDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ManaCoreLevelDataSyncS2CPacket::new)
                .encoder(ManaCoreLevelDataSyncS2CPacket::toBytes)
                .consumerMainThread(ManaCoreLevelDataSyncS2CPacket::handle)
                .add();

        /*net.messageBuilder(UpdateManaCoreXpC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateManaCoreXpC2SPacket::new)
                .encoder(UpdateManaCoreXpC2SPacket::toBytes)
                .consumerMainThread(UpdateManaCoreXpC2SPacket::handle)
                .add();
         */
        net.messageBuilder(ManaCoreXpDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ManaCoreXpDataSyncS2CPacket::new)
                .encoder(ManaCoreXpDataSyncS2CPacket::toBytes)
                .consumerMainThread(ManaCoreXpDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(UpdateMenuStatTabToggleC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateMenuStatTabToggleC2SPacket::new)
                .encoder(UpdateMenuStatTabToggleC2SPacket::toBytes)
                .consumerMainThread(UpdateMenuStatTabToggleC2SPacket::handle)
                .add();

        net.messageBuilder(MenuStatTabToggleDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MenuStatTabToggleDataSyncS2CPacket::new)
                .encoder(MenuStatTabToggleDataSyncS2CPacket::toBytes)
                .consumerMainThread(MenuStatTabToggleDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(UpdateManaBarrierAliveC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateManaBarrierAliveC2SPacket::new)
                .encoder(UpdateManaBarrierAliveC2SPacket::toBytes)
                .consumerMainThread(UpdateManaBarrierAliveC2SPacket::handle)
                .add();

        net.messageBuilder(ManaBarrierAliveDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ManaBarrierAliveDataSyncS2CPacket::new)
                .encoder(ManaBarrierAliveDataSyncS2CPacket::toBytes)
                .consumerMainThread(ManaBarrierAliveDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(UpdateHpC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateHpC2SPacket::new)
                .encoder(UpdateHpC2SPacket::toBytes)
                .consumerMainThread(UpdateHpC2SPacket::handle)
                .add();

        net.messageBuilder(HpDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(HpDataSyncS2CPacket::new)
                .encoder(HpDataSyncS2CPacket::toBytes)
                .consumerMainThread(HpDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(UpdateMaxHpC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateMaxHpC2SPacket::new)
                .encoder(UpdateMaxHpC2SPacket::toBytes)
                .consumerMainThread(UpdateMaxHpC2SPacket::handle)
                .add();

        net.messageBuilder(MaxHpDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MaxHpDataSyncS2CPacket::new)
                .encoder(MaxHpDataSyncS2CPacket::toBytes)
                .consumerMainThread(MaxHpDataSyncS2CPacket::handle)
                .add();



        net.messageBuilder(AnimateHurtDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(AnimateHurtDataSyncS2CPacket::new)
                .encoder(AnimateHurtDataSyncS2CPacket::toBytes)
                .consumerMainThread(AnimateHurtDataSyncS2CPacket::handle)
                .add();


        net.messageBuilder(SpellCastingC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SpellCastingC2SPacket::new)
                .encoder(SpellCastingC2SPacket::toBytes)
                .consumerMainThread(SpellCastingC2SPacket::handle)
                .add();







        net.messageBuilder(UpdateSpellSlotsToggleC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateSpellSlotsToggleC2SPacket::new)
                .encoder(UpdateSpellSlotsToggleC2SPacket::toBytes)
                .consumerMainThread(UpdateSpellSlotsToggleC2SPacket::handle)
                .add();

        net.messageBuilder(SpellSlotsToggleDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SpellSlotsToggleDataSyncS2CPacket::new)
                .encoder(SpellSlotsToggleDataSyncS2CPacket::toBytes)
                .consumerMainThread(SpellSlotsToggleDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(UpdateSpellSlotsC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateSpellSlotsC2SPacket::new)
                .encoder(UpdateSpellSlotsC2SPacket::toBytes)
                .consumerMainThread(UpdateSpellSlotsC2SPacket::handle)
                .add();

        net.messageBuilder(SpellSlotsDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SpellSlotsDataSyncS2CPacket::new)
                .encoder(SpellSlotsDataSyncS2CPacket::toBytes)
                .consumerMainThread(SpellSlotsDataSyncS2CPacket::handle)
                .add();



        net.messageBuilder(UpdateSpellPowerLevelC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateSpellPowerLevelC2SPacket::new)
                .encoder(UpdateSpellPowerLevelC2SPacket::toBytes)
                .consumerMainThread(UpdateSpellPowerLevelC2SPacket::handle)
                .add();

        net.messageBuilder(SpellPowerLevelDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SpellPowerLevelDataSyncS2CPacket::new)
                .encoder(SpellPowerLevelDataSyncS2CPacket::toBytes)
                .consumerMainThread(SpellPowerLevelDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(UpdateSpellRangeC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateSpellRangeC2SPacket::new)
                .encoder(UpdateSpellRangeC2SPacket::toBytes)
                .consumerMainThread(UpdateSpellRangeC2SPacket::handle)
                .add();

        net.messageBuilder(SpellRangeDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SpellRangeDataSyncS2CPacket::new)
                .encoder(SpellRangeDataSyncS2CPacket::toBytes)
                .consumerMainThread(SpellRangeDataSyncS2CPacket::handle)
                .add();



        net.messageBuilder(UpdateCastingAmountC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateCastingAmountC2SPacket::new)
                .encoder(UpdateCastingAmountC2SPacket::toBytes)
                .consumerMainThread(UpdateCastingAmountC2SPacket::handle)
                .add();

        net.messageBuilder(CastingAmountDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CastingAmountDataSyncS2CPacket::new)
                .encoder(CastingAmountDataSyncS2CPacket::toBytes)
                .consumerMainThread(CastingAmountDataSyncS2CPacket::handle)
                .add();



    }

    public static <MSG> void sendToServer(MSG message) {
         INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
