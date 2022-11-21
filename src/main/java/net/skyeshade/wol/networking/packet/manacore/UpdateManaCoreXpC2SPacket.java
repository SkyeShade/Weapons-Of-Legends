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

    private final long core1XPRequired = 1000;
    private final long core2XPRequired = 2000;
    private final long core3XPRequired = 3000;
    private final long core4XPRequired = 4000;
    private final long core5XPRequired = 5000;
    private final long core6XPRequired = 6000;
    private final long core7XPRequired = 7000;
    private final long core8XPRequired = 8000;
    private final long core9XPRequired = 9000;
    private final long core10XPRequired = 10000;
    private final long core11XPRequired = 11000;
    private final long core12XPRequired = 12000;
    private final long core13XPRequired = 13000;
    private final long core14XPRequired = 14000;


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
            player.getCapability(PlayerStatsProvider.PLAYER_MANACORE_XP).ifPresent(manacore_xp -> {

                manacore_xp.addManaCoreXp(manaCoreXpChange);
                ModMessages.sendToPlayer(new ManaCoreXpDataSyncS2CPacket(manacore_xp.getManaCoreXp()), player);
            });


        });
        return true;
    }

    /*private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }*/
}
