package net.skyeshade.wol.networking.packet.affinities.earth;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;

import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateEarthAffinityC2SPacket {
    private final long earthAffinityChange;


    public UpdateEarthAffinityC2SPacket(long earthAffinityChange) {
        this.earthAffinityChange = earthAffinityChange;
    }

    public UpdateEarthAffinityC2SPacket(FriendlyByteBuf buf) {
        this.earthAffinityChange = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(earthAffinityChange);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();

            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    stats.addEarthAffinity(earthAffinityChange);
                    //StatSystems.xpSystem(earthAffinityChange, player);



                ModMessages.sendToPlayer(new EarthAffinityDataSyncS2CPacket(stats.getEarthAffinity()), player);

            });


        });
        return true;
    }

}
