package net.skyeshade.wol.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.item.ModCreativeModeTab;
import net.skyeshade.wol.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, WOL.MOD_ID);



    public static final RegistryObject<Block> DESTRUCTION = BLOCKS.register("destruction",
            () -> new Destruction(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.FIRE)
                    .instabreak().noOcclusion().lightLevel((state) -> 15).sound(new SoundType(-100.0F,1.0F, SoundEvents.FIRE_AMBIENT,SoundEvents.FIRE_AMBIENT,SoundEvents.FIRE_AMBIENT,SoundEvents.FIRE_AMBIENT,SoundEvents.FIRE_AMBIENT))));



    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);

        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }


}
