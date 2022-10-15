package net.skyeshade.wol.networking.packet;


import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkEvent;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.thirst.PlayerThirstProvider;

import java.util.function.Supplier;

public class DrinkWaterC2SPacket {
    private static final String MESSAGE_DRINK_WATER = "message.wol.drink_water";
    private static final String MESSAGE_NO_WATER = "message.wol.no_water";

    public DrinkWaterC2SPacket() {

    }

    public DrinkWaterC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();



                // increase the water level / thirst level of player
                // Output the current thirst level
            player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                thirst.subThirst(1);
                ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(thirst.getThirst()), player);
            });


        });
        return true;
    }

    private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }
}
