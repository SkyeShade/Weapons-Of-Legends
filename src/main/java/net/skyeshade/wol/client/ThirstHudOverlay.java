package net.skyeshade.wol.client;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.skyeshade.wol.WOL;

public class ThirstHudOverlay {
    private static final ResourceLocation FILLED_THIRST = new ResourceLocation(WOL.MOD_ID,
            "textures/thirst/filled_thirst.png");
    private static final ResourceLocation EMPTY_THIRST = new ResourceLocation(WOL.MOD_ID,
            "textures/thirst/empty_thirst.png");

    public static final IGuiOverlay HUD_THIRST = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, EMPTY_THIRST);

        /*
        for(int i = 0; i < 80; i++) {
            GuiComponent.blit(poseStack,x - 90 + (i), y - 60,0,0,1,8,
                    1,8);
        }*/
        GuiComponent.blit(poseStack,x - 91, y - 60,0,0,81,9,
                81,9);

        RenderSystem.setShaderTexture(0, FILLED_THIRST);
        for(int i = 0; i < 79; i++) {
            if(ClientThirstData.getPlayerThirst() > i) {
                GuiComponent.blit(poseStack,x - 90 + (i),y - 60,0,0,1,9,
                        1,9);
            } else {
                break;
            }
        }

        Minecraft.getInstance().font.draw(poseStack, String.valueOf(ClientThirstData.getPlayerThirst()), x - 55 - (String.valueOf(ClientThirstData.getPlayerThirst()).length()*6), y - 59, 43690);

        Minecraft.getInstance().font.draw(poseStack, "/", x - 53, y - 59, 43690);

        Minecraft.getInstance().font.draw(poseStack, "79", x - 45, y - 59, 43690);
    });
}
