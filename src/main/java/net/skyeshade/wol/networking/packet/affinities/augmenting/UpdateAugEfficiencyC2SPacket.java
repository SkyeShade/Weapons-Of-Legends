package net.skyeshade.wol.networking.packet.affinities.augmenting;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.affinities.aether.AetherAffinityDataSyncS2CPacket;
import net.skyeshade.wol.stats.PlayerStatsProvider;

import java.util.function.Supplier;

public class UpdateAugEfficiencyC2SPacket {
    private final long augEfficiencyChange;


    public UpdateAugEfficiencyC2SPacket(long augEfficiencyChange) {
        this.augEfficiencyChange = augEfficiencyChange;
    }

    public UpdateAugEfficiencyC2SPacket(FriendlyByteBuf buf) {
        this.augEfficiencyChange = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(augEfficiencyChange);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();

            player.getCapability(PlayerStatsProvider.PLAYER_STATS).ifPresent(stats -> {

                    stats.addAugmentingEfficiency(augEfficiencyChange);
                    //StatSystems.xpSystem(augEfficiencyChange, player);



                ModMessages.sendToPlayer(new AugEfficiencyDataSyncS2CPacket(stats.getAugmentingEfficiency()), player);

            });


        });
        return true;
    }

}
