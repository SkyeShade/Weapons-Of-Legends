package net.skyeshade.wol.networking.packet.hp;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.stats.PlayerStatsProvider;
import net.skyeshade.wol.util.StatSystems;

import java.util.function.Supplier;

public class UpdateHpC2SPacket {
    private final long hpChange;

    StatSystems statSystems = new StatSystems();
    public UpdateHpC2SPacket(long hpChange) {
        this.hpChange = hpChange;
    }

    public UpdateHpC2SPacket(FriendlyByteBuf buf) {
        this.hpChange = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(hpChange);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();



                // increase the water level / stats level of player
                // Output the current stats level
            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    stats.addHp(hpChange);
                    statSystems.xpSystem(hpChange, player);



                ModMessages.sendToPlayer(new HpDataSyncS2CPacket(stats.getHp()), player);

            });


        });
        return true;
    }

    /*private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }*/
}
