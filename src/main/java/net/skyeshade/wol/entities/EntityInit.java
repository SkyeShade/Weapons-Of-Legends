package net.skyeshade.wol.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.skyeshade.wol.WOL;

public class EntityInit {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, WOL.MOD_ID);


    public static final RegistryObject<EntityType<BladeSlashProjectileEntity>> BLADE_SLASH = ENTITY_TYPES.register("blade_slash",
            () -> EntityType.Builder.of((EntityType.EntityFactory<BladeSlashProjectileEntity>) BladeSlashProjectileEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).build("blade_slash"));
}
