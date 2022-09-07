package net.skyeshade.wol.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.skyeshade.wol.WOL;
import net.skyeshade.wol.item.custom.TimeStopSwordItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, WOL.MOD_ID);


    public static final RegistryObject<Item> KAGE_KOKOUY = ITEMS.register("kage_kokouy",
            () -> new TimeStopSwordItem(Tiers.NETHERITE, 4, 6f,
                    new Item.Properties().tab(ModCreativeModeTab.WEAPONS_OF_LEGENDS).durability(-1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
