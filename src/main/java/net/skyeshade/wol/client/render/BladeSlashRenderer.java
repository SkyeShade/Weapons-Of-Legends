package net.skyeshade.wol.client.render;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.entities.BladeSlashProjectileEntity;


public class BladeSlashRenderer extends ArrowRenderer<BladeSlashProjectileEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(WOL.MOD_ID, "textures/entity/empty.png");

    public BladeSlashRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    public ResourceLocation getTextureLocation(BladeSlashProjectileEntity arrow) {
        return TEXTURE;
    }
}
