package net.skyeshade.wol.networking.packet.affinities.water;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;

import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateWaterAffinityC2SPacket {
    private final long waterAffinityChange;


    public UpdateWaterAffinityC2SPacket(long waterAffinityChange) {
        this.waterAffinityChange = waterAffinityChange;
    }

    public UpdateWaterAffinityC2SPacket(FriendlyByteBuf buf) {
        this.waterAffinityChange = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(waterAffinityChange);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();

            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    stats.addWaterAffinity(waterAffinityChange);
                    //StatSystems.xpSystem(waterAffinityChange, player);



                ModMessages.sendToPlayer(new WaterAffinityDataSyncS2CPacket(stats.getWaterAffinity()), player);

            });


        });
        return true;
    }

}
