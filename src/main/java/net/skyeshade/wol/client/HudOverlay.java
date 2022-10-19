package net.skyeshade.wol.client;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.skyeshade.wol.WOL;
import net.skyeshade.wol.util.LongHudFormatter;

public class HudOverlay {
    private static final ResourceLocation FILLED_MANA = new ResourceLocation(WOL.MOD_ID, "textures/stats/filled_mana.png");
    private static final ResourceLocation FILLED_MANACORE = new ResourceLocation(WOL.MOD_ID, "textures/stats/filled_manacore.png");
    private static final ResourceLocation FILLED_MANACORE_EXHAUSTION = new ResourceLocation(WOL.MOD_ID, "textures/stats/filled_manacore_exhaustion.png");
    private static final ResourceLocation EMPTY_MANA_START = new ResourceLocation(WOL.MOD_ID, "textures/stats/empty_mana_start2.png");
    private static final ResourceLocation EMPTY_MANA_SEGMENT = new ResourceLocation(WOL.MOD_ID, "textures/stats/empty_mana_segment2.png");
    private static final ResourceLocation EMPTY_MANA_END = new ResourceLocation(WOL.MOD_ID, "textures/stats/empty_mana_end2.png");
    public static final IGuiOverlay HUD_STATS = ((gui, poseStack, partialTick, width, height) -> {
        //long x = width / 2;
        int xManaBar = 5;
        int xManaCoreBar = 5;
        //long y = height;
        int yManaBar = height - 14;
        int yManaCoreBar = height - 24;

        int xScaleManaBar = (int)(width/4);
        int xScaleManaCoreBar = (int)(width/4);
        int endStartCapPixelWidth = 4;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.setShaderTexture(0, FILLED_MANA);
        for(int i = 0; i < xScaleManaBar+1; i++) {
            if(((float)ClientStatsData.getPlayerMana()/(float)ClientStatsData.getPlayerMaxMana())*100 > (i/(float)(xScaleManaBar+1))*100) {
                GuiComponent.blit(poseStack,xManaBar +1 + (i),yManaBar ,0,0,1,9,
                        1,9);

            } else {
                break;
            }
        }
        //manacore progress1
        RenderSystem.setShaderTexture(0, FILLED_MANACORE);
        for(int i = 0; i < xScaleManaCoreBar+1; i++) {

            if(((float)ClientStatsData.getPlayerManaCore()/(float)ClientStatsData.getPlayerMaxManaCore())*100 > (i/(float)(xScaleManaCoreBar+1))*100) {
                GuiComponent.blit(poseStack,xManaCoreBar +1 + (i),yManaCoreBar ,0,0,1,9,
                        1,9);

            } else {
                break;
            }

        }
        //manacore progress2

        RenderSystem.setShaderTexture(0, FILLED_MANACORE_EXHAUSTION);
        for(int i = 0; i < xScaleManaCoreBar+1; i++) {
            if(((float)ClientStatsData.getPlayerManaCoreExhaustion()/(float)ClientStatsData.getPlayerMaxManaCore())*100 > (i/(float)(xScaleManaCoreBar+1))*100) {
                GuiComponent.blit(poseStack,xManaCoreBar +1 + (i),yManaCoreBar ,0,0,1,9,
                        1,9);

            } else {
                break;
            }
        }
        RenderSystem.setShaderTexture(0, EMPTY_MANA_START);
        GuiComponent.blit(poseStack,xManaBar, yManaBar,0,0,endStartCapPixelWidth,9, endStartCapPixelWidth,9);
        RenderSystem.setShaderTexture(0, EMPTY_MANA_SEGMENT);
        GuiComponent.blit(poseStack,xManaBar+endStartCapPixelWidth, yManaBar,0,0,xScaleManaBar-endStartCapPixelWidth,9, xScaleManaBar-endStartCapPixelWidth,9);
        RenderSystem.setShaderTexture(0, EMPTY_MANA_END);
        GuiComponent.blit(poseStack,xManaBar+3+xScaleManaBar-endStartCapPixelWidth, yManaBar ,0,0,endStartCapPixelWidth,9, endStartCapPixelWidth,9);

        //manacore bar
        RenderSystem.setShaderTexture(0, EMPTY_MANA_START);
        GuiComponent.blit(poseStack,xManaCoreBar, yManaCoreBar,0,0,endStartCapPixelWidth,9, endStartCapPixelWidth,9);
        RenderSystem.setShaderTexture(0, EMPTY_MANA_SEGMENT);
        GuiComponent.blit(poseStack,xManaCoreBar+endStartCapPixelWidth, yManaCoreBar,0,0,xScaleManaCoreBar-endStartCapPixelWidth,9, xScaleManaCoreBar-endStartCapPixelWidth,9);
        RenderSystem.setShaderTexture(0, EMPTY_MANA_END);
        GuiComponent.blit(poseStack,xManaCoreBar+3+xScaleManaCoreBar-endStartCapPixelWidth, yManaCoreBar,0,0,endStartCapPixelWidth,9, endStartCapPixelWidth,9);

        int uiManaTextLength;
        uiManaTextLength = (LongHudFormatter.format(ClientStatsData.getPlayerMana()).length()*6);
        if (LongHudFormatter.format(ClientStatsData.getPlayerMana()).contains("."))
            uiManaTextLength = (LongHudFormatter.format(ClientStatsData.getPlayerMana()).length()*6)-4;

        long manaTextCentre = (xScaleManaBar-3)/2;

        //Minecraft.getInstance().font.draw(poseStack, String.valueOf(ClientStatsData.getPlayerMana()), xManaBar + manaTextCentre-2 - (String.valueOf(ClientStatsData.getPlayerMana()).length()*6), yManaBar+1 , 43690);
        Minecraft.getInstance().font.draw(poseStack, LongHudFormatter.format(ClientStatsData.getPlayerMana()), xManaBar + manaTextCentre-2 - (uiManaTextLength), yManaBar+1 , 43690);
        Minecraft.getInstance().font.draw(poseStack, "/", xManaBar + manaTextCentre, yManaBar+1 , 43690);
        Minecraft.getInstance().font.draw(poseStack, LongHudFormatter.format(ClientStatsData.getPlayerMaxMana()), xManaBar + manaTextCentre+8, yManaBar+1 , 43690);
        //Minecraft.getInstance().font.draw(poseStack, String.valueOf(ClientStatsData.getPlayerMaxMana()), xManaBar + manaTextCentre+8, yManaBar+1 , 43690);

        int uiManaCoreTextLength;
        uiManaCoreTextLength = (LongHudFormatter.format(ClientStatsData.getPlayerManaCore()).length()*6);
        if (LongHudFormatter.format(ClientStatsData.getPlayerManaCore()).contains("."))
            uiManaCoreTextLength = (LongHudFormatter.format(ClientStatsData.getPlayerManaCore()).length()*6)-4;
        long manaCoreTextCentre = (xScaleManaCoreBar-3)/2;
        //Minecraft.getInstance().font.draw(poseStack, String.valueOf(ClientStatsData.getPlayerManaCore()), xManaCoreBar + manaCoreTextCentre-2 - (String.valueOf(ClientStatsData.getPlayerManaCore()).length()*6), yManaCoreBar+1 , 12878902);
        Minecraft.getInstance().font.draw(poseStack,LongHudFormatter.format(ClientStatsData.getPlayerManaCore()), xManaCoreBar + manaCoreTextCentre-2 - (uiManaCoreTextLength), yManaCoreBar+1 , 12878902);
        Minecraft.getInstance().font.draw(poseStack, "/", xManaCoreBar + manaCoreTextCentre, yManaCoreBar+1 , 12878902);
        Minecraft.getInstance().font.draw(poseStack, LongHudFormatter.format(ClientStatsData.getPlayerMaxManaCore()), xManaCoreBar + manaCoreTextCentre+8, yManaCoreBar+1 , 12878902);
        //Minecraft.getInstance().font.draw(poseStack, String.valueOf(ClientStatsData.getPlayerMaxManaCore()), xManaCoreBar + manaCoreTextCentre+8, yManaCoreBar+1 , 12878902);
    });
}
