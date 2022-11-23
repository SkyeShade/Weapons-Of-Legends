package net.skyeshade.wol;

import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.skyeshade.wol.abilities.TimeStopAbility;
import net.skyeshade.wol.block.Destruction;
import net.skyeshade.wol.block.ModBlocks;
import net.skyeshade.wol.entities.EntityInit;
import net.skyeshade.wol.item.ModItems;
import net.skyeshade.wol.networking.ModMessages;
import net.skyeshade.wol.sound.ModSounds;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(WOL.MOD_ID)
public class WOL {
    public static final String MOD_ID = "wol";
    private static final Logger LOGGER = LogUtils.getLogger();

    // Very Important Comment
    public WOL() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();



        ModItems.register(modEventBus);

        ModSounds.register(modEventBus);
        EntityInit.ENTITY_TYPES.register(modEventBus);

        ModBlocks.register(modEventBus);



        //MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, TimeStopAbility::onServerTick);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, TimeStopAbility::onPlayerLoggedIn);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, TimeStopAbility::onPlayerLoggedOut);
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

        });

        ModMessages.register();
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
