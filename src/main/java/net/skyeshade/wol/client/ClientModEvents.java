package net.skyeshade.wol.client;


import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.client.render.BladeSlashRenderer;
import net.skyeshade.wol.entities.EntityInit;
import net.skyeshade.wol.entities.spells.spellrenderer.fireelement.FireBallRenderer;


@Mod.EventBusSubscriber(modid = WOL.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

        EntityRenderers.register(EntityInit.BLADE_SLASH.get(), BladeSlashRenderer::new);

        EntityRenderers.register(EntityInit.FIREBALL.get(), FireBallRenderer::new);
    }
}