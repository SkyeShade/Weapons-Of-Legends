package net.skyeshade.wol.networking.packet.affinities.aether;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;

import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateAetherAffinityC2SPacket {
    private final long aetherAffinityChange;


    public UpdateAetherAffinityC2SPacket(long aetherAffinityChange) {
        this.aetherAffinityChange = aetherAffinityChange;
    }

    public UpdateAetherAffinityC2SPacket(FriendlyByteBuf buf) {
        this.aetherAffinityChange = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(aetherAffinityChange);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();

            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    stats.addAetherAffinity(aetherAffinityChange);
                    //StatSystems.xpSystem(aetherAffinityChange, player);



                ModMessages.sendToPlayer(new AetherAffinityDataSyncS2CPacket(stats.getAetherAffinity()), player);

            });


        });
        return true;
    }

}
