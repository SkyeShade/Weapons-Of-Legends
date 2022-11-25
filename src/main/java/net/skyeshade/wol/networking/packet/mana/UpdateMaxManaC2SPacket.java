package net.skyeshade.wol.networking.packet.mana;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.entities.EntityInit;
import net.skyeshade.wol.entities.spells.fireelement.FireBall;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateMaxManaC2SPacket {

    private final long maxManaChange;
    public UpdateMaxManaC2SPacket(long maxManaChange) {
        this.maxManaChange = maxManaChange;
    }

    public UpdateMaxManaC2SPacket(FriendlyByteBuf buf) {
        this.maxManaChange = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(maxManaChange);
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
                stats.addMaxMana(maxManaChange);
                ModMessages.sendToPlayer(new MaxManaDataSyncS2CPacket(stats.getMaxMana()), player);
            });



            FireBall fireball = new FireBall(EntityInit.FIREBALL.get(), player,player.level);
            fireball.shootFromRotation(player, player.getXRot(), player.getYRot(), -1.0F, 3.01f, 1.0F);
            player.level.addFreshEntity(fireball);

        });
        return true;
    }

    /*private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }*/
}
