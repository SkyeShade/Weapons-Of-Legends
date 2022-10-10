package net.skyeshade.wol;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.skyeshade.wol.abilities.TimeStopAbility;
import net.skyeshade.wol.entities.EntityInit;
import net.skyeshade.wol.item.ModItems;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.sound.ModSounds;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(WOL.MOD_ID)
public class WOL
{
    public static final String MOD_ID = "wol";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();



    public WOL()
    {
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(eventBus);

        ModSounds.register(eventBus);
        EntityInit.ENTITY_TYPES.register(eventBus);


        eventBus.addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, TimeStopAbility::onServerTick);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, TimeStopAbility::onPlayerLoggedIn);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, TimeStopAbility::onPlayerLoggedOut);


        //MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, ShootSlashProjectileAbility::onServerTick);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            ModMessages.register();

        });

        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getName());
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }

}