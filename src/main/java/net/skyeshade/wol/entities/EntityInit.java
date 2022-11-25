package net.skyeshade.wol.entities;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.entities.spells.fireelement.FireBall;


public class EntityInit {

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WOL.MOD_ID);


    public static final RegistryObject<EntityType<BladeSlashProjectileEntity>> BLADE_SLASH = ENTITY_TYPES.register("blade_slash",
            () -> EntityType.Builder.of((EntityType.EntityFactory<BladeSlashProjectileEntity>) BladeSlashProjectileEntity::new, MobCategory.MISC).sized(2.5F, 0.5F).build("blade_slash"));




    public static final RegistryObject<EntityType<FireBall>> FIREBALL = ENTITY_TYPES.register("fireball",
            () -> EntityType.Builder.of((EntityType.EntityFactory<FireBall>) FireBall::new, MobCategory.MISC).sized(2.5F, 0.5F).build("fireball"));
}
