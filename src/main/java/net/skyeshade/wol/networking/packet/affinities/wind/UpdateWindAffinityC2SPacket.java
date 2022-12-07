package net.skyeshade.wol.networking.packet.affinities.wind;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;

import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateWindAffinityC2SPacket {
    private final long windAffinityChange;


    public UpdateWindAffinityC2SPacket(long windAffinityChange) {
        this.windAffinityChange = windAffinityChange;
    }

    public UpdateWindAffinityC2SPacket(FriendlyByteBuf buf) {
        this.windAffinityChange = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(windAffinityChange);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();

            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    stats.addWindAffinity(windAffinityChange);
                    //StatSystems.xpSystem(windAffinityChange, player);



                ModMessages.sendToPlayer(new WindAffinityDataSyncS2CPacket(stats.getWindAffinity()), player);

            });


        });
        return true;
    }

}
