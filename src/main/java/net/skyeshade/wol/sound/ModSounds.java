package net.skyeshade.wol.sound;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.skyeshade.wol.WOL;


public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WOL.MOD_ID);

    public static final RegistryObject<SoundEvent> STOP_TIME =
            registerSoundEvent("stop_time");


    public static final RegistryObject<SoundEvent> START_TIME =
            registerSoundEvent("start_time");

    public static final RegistryObject<SoundEvent> DISTANT_EXPLOSION =
            registerSoundEvent("distant_explosion");


    public static final RegistryObject<SoundEvent> EXPLOSION =
            registerSoundEvent("explosion");
    public static final RegistryObject<SoundEvent> FIRE_WAVE =
            registerSoundEvent("fire_wave");



    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, ()-> new SoundEvent(new ResourceLocation(WOL.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
