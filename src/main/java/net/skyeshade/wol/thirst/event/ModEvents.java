package net.skyeshade.wol.thirst.event;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.networking.packet.ThirstDataSyncS2CPacket;
import net.skyeshade.wol.thirst.PlayerThirst;
import net.skyeshade.wol.thirst.PlayerThirstProvider;

@Mod.EventBusSubscriber(modid = WOL.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerThirstProvider.PLAYER_THIRST).isPresent()) {
                event.addCapability(new ResourceLocation(WOL.MOD_ID, "properties"), new PlayerThirstProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().invalidateCaps();

        }
    }


    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerThirst.class);
    }
    static int ticks;
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.side == LogicalSide.SERVER) {

            if (event.player.tickCount % 20 == 0) {
                ticks++;
                event.player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                    if (ticks == 1) {
                        thirst.addThirst(1);

                    }

                    if (ticks == 2)
                        ticks = 0;


                    ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(thirst.getThirst()), ((ServerPlayer) event.player));

                });
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                    ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(thirst.getThirst()), player);
                });
            }
        }
    }


}
