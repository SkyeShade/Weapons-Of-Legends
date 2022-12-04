package net.skyeshade.wol.entities.spells.spellrenderer.fireelement;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.skyeshade.wol.client.ClientStatsData;
import net.skyeshade.wol.entities.spells.spellrenderer.model.FireBallModel;
import net.skyeshade.wol.entities.spells.fireelement.FireBallEntity;
import net.skyeshade.wol.stats.PlayerStatsProvider;


public class FireBallRenderer extends EntityRenderer<FireBallEntity> {
    //TODO: fix the random crash that happens sometimes when the fireball takes up the entire screen (mostly happened when in fullscreen)

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("wol:textures/entity/spark.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(TEXTURE_LOCATION);
    private final FireBallModel<FireBallEntity> model;



    public FireBallRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new FireBallModel<>(pContext.bakeLayer(ModelLayers.SHULKER_BULLET));
    }



    protected int getBlockLightLevel(FireBallEntity pEntity, BlockPos pPos) {
        return 15;
    }

    public void render(FireBallEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {




        pMatrixStack.pushPose();
        float f = Mth.rotlerp(pEntity.yRotO, pEntity.getYRot(), pPartialTicks);
        float f1 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());
        float f2 = (float) pEntity.tickCount + pPartialTicks;
        pMatrixStack.translate(0.0D, (double) 0.15F, 0.0D);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees((int) (Math.random() * 180 + 1)));
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees((int) (Math.random() * 180 + 1)));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees((int) (Math.random() * 180 + 1)));
        pMatrixStack.scale(-0.5F - ((float)pEntity.getPowerLevel()/100), -0.5F - ((float)pEntity.getPowerLevel()/100), 0.5F + ((float)pEntity.getPowerLevel()/100));
        //System.out.println(pEntity.getPowerLevel());
        this.model.setupAnim(pEntity, 0.0F, 0.0F, 0.0F, f, f1);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        this.model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pMatrixStack.scale(1.5F, 1.5F, 1.5F);
        VertexConsumer vertexconsumer1 = pBuffer.getBuffer(RENDER_TYPE);
        this.model.renderToBuffer(pMatrixStack, vertexconsumer1, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(FireBallEntity pEntity) {
        return TEXTURE_LOCATION;
    }
}
