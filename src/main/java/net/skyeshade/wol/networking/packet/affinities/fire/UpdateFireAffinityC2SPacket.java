package net.skyeshade.wol.networking.packet.affinities.fire;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateFireAffinityC2SPacket {
    private final long fireAffinityChange;


    public UpdateFireAffinityC2SPacket(long fireAffinityChange) {
        this.fireAffinityChange = fireAffinityChange;
    }

    public UpdateFireAffinityC2SPacket(FriendlyByteBuf buf) {
        this.fireAffinityChange = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(fireAffinityChange);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();

            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    stats.addFireAffinity(fireAffinityChange);
                    //StatSystems.xpSystem(fireAffinityChange, player);



                ModMessages.sendToPlayer(new FireAffinityDataSyncS2CPacket(stats.getFireAffinity()), player);

            });


        });
        return true;
    }

    /*private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }*/
}
