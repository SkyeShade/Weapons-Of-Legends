package net.skyeshade.wol.networking.packet.manacore;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateManaCoreXpC2SPacket {

    private final long manaCoreXpChange;
    public UpdateManaCoreXpC2SPacket(long manaCoreXpChange) {
        this.manaCoreXpChange = manaCoreXpChange;
    }

    public UpdateManaCoreXpC2SPacket(FriendlyByteBuf buf) {
        this.manaCoreXpChange = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(manaCoreXpChange);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();



                // increase the water level / stats level of player
                // Output the current stats level
            player.getCapability(PlayerStatsProvider.PLAYER_MANACORE_XP).ifPresent(manacore_level -> {
                manacore_level.addManaCoreXp(manaCoreXpChange);
                ModMessages.sendToPlayer(new ManaCoreXpDataSyncS2CPacket(manacore_level.getManaCoreXp()), player);
            });


        });
        return true;
    }

    /*private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }*/
}
