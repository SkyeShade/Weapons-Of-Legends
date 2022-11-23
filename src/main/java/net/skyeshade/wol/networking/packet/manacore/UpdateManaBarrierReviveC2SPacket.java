package net.skyeshade.wol.networking.packet.manacore;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.stats.PlayerStatsProvider;


import java.util.function.Supplier;

public class UpdateManaBarrierReviveC2SPacket {
    private final long manaBarrierReviveChange;
    public UpdateManaBarrierReviveC2SPacket(long manaBarrierReviveChange) {
        this.manaBarrierReviveChange = manaBarrierReviveChange;
    }

    public UpdateManaBarrierReviveC2SPacket(FriendlyByteBuf buf) {
        this.manaBarrierReviveChange = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(manaBarrierReviveChange);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();



                // increase the water level / stats level of player
                // Output the current stats level
            player.getCapability(PlayerStatsProvider.PLAYER_MANABARRIERREVIVE).ifPresent(manabarrierrevive -> {
                manabarrierrevive.addManaBarrierRevive(manaBarrierReviveChange);
                ModMessages.sendToPlayer(new ManaBarrierReviveDataSyncS2CPacket(manabarrierrevive.getManaBarrierRevive()), player);
            });


        });
        return true;
    }

    /*private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }*/
}
