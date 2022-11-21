package net.skyeshade.wol.networking;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.skyeshade.wol.WOL;

import net.skyeshade.wol.networking.packet.destruction.DestructionActiveDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.destruction.UpdateDestructionActiveC2SPacket;
import net.skyeshade.wol.networking.packet.mana.ManaDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.mana.MaxManaDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.mana.UpdateManaC2SPacket;
import net.skyeshade.wol.networking.packet.mana.UpdateMaxManaC2SPacket;
import net.skyeshade.wol.networking.packet.manacore.*;
import net.skyeshade.wol.networking.packet.menutoggle.MenuStatTabToggleDataSyncS2CPacket;
import net.skyeshade.wol.networking.packet.menutoggle.UpdateMenuStatTabToggleC2SPacket;

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

        net.messageBuilder(UpdateManaCoreC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateManaCoreC2SPacket::new)
                .encoder(UpdateManaCoreC2SPacket::toBytes)
                .consumerMainThread(UpdateManaCoreC2SPacket::handle)
                .add();

        net.messageBuilder(ManaCoreDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ManaCoreDataSyncS2CPacket::new)
                .encoder(ManaCoreDataSyncS2CPacket::toBytes)
                .consumerMainThread(ManaCoreDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(UpdateMaxManaCoreC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateMaxManaCoreC2SPacket::new)
                .encoder(UpdateMaxManaCoreC2SPacket::toBytes)
                .consumerMainThread(UpdateMaxManaCoreC2SPacket::handle)
                .add();

        net.messageBuilder(MaxManaCoreDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MaxManaCoreDataSyncS2CPacket::new)
                .encoder(MaxManaCoreDataSyncS2CPacket::toBytes)
                .consumerMainThread(MaxManaCoreDataSyncS2CPacket::handle)
                .add();

        net.messageBuilder(UpdateManaCoreExhaustionC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateManaCoreExhaustionC2SPacket::new)
                .encoder(UpdateManaCoreExhaustionC2SPacket::toBytes)
                .consumerMainThread(UpdateManaCoreExhaustionC2SPacket::handle)
                .add();

        net.messageBuilder(ManaCoreExhaustionDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ManaCoreExhaustionDataSyncS2CPacket::new)
                .encoder(ManaCoreExhaustionDataSyncS2CPacket::toBytes)
                .consumerMainThread(ManaCoreExhaustionDataSyncS2CPacket::handle)
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

        net.messageBuilder(UpdateManaCoreXpC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UpdateManaCoreXpC2SPacket::new)
                .encoder(UpdateManaCoreXpC2SPacket::toBytes)
                .consumerMainThread(UpdateManaCoreXpC2SPacket::handle)
                .add();

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
    }

    public static <MSG> void sendToServer(MSG message) {
         INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
