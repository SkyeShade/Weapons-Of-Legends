package net.skyeshade.wol.entities.spells.spellrenderer;


import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.entities.spells.fireelement.FireBall;


public class FireBallRenderer extends EntityRenderer<FireBall> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(WOL.MOD_ID, "textures/entity/empty.png");

    public FireBallRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    @Override
    public ResourceLocation getTextureLocation(FireBall pEntity) {
        return TEXTURE;
    }


}
