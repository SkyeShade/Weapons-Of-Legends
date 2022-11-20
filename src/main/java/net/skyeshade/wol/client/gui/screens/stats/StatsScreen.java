package net.skyeshade.wol.client.gui.screens.stats;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.stringtemplate.v4.ST;

import javax.annotation.Nullable;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class StatsScreen extends Screen {
   private static final ResourceLocation WINDOW_LOCATION = new ResourceLocation("wol:textures/gui/window.png");
   private static final ResourceLocation WINDOW_BACKGROUND = new ResourceLocation("wol:textures/gui/stone.png");
   private static final ResourceLocation FIRE_ICON = new ResourceLocation("wol:textures/gui/fire_icon.png");
   private static final ResourceLocation FIRE_ICON_HIGH = new ResourceLocation("wol:textures/gui/fire_icon_high.png");
   private static final ResourceLocation WATER_ICON = new ResourceLocation("wol:textures/gui/water_icon.png");
   private static final ResourceLocation WATER_ICON_HIGH = new ResourceLocation("wol:textures/gui/water_icon_high.png");
   private static final ResourceLocation EARTH_ICON = new ResourceLocation("wol:textures/gui/earth_icon.png");
   private static final ResourceLocation EARTH_ICON_HIGH = new ResourceLocation("wol:textures/gui/earth_icon_high.png");
   private static final ResourceLocation WIND_ICON = new ResourceLocation("wol:textures/gui/wind_icon.png");
   private static final ResourceLocation WIND_ICON_HIGH = new ResourceLocation("wol:textures/gui/wind_icon_high.png");

   private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/advancements/widgets.png");
   public static final int WINDOW_WIDTH = 252;
   public static final int WINDOW_HEIGHT = 234;
   private static final int WINDOW_INSIDE_X = 9;
   private static final int WINDOW_INSIDE_Y = 18;
   public static final int WINDOW_INSIDE_WIDTH = 234;
   public static final int WINDOW_INSIDE_HEIGHT = 207;
   private static final int WINDOW_TITLE_X = 8;
   private static final int WINDOW_TITLE_Y = 6;
   public static final int BACKGROUND_TILE_WIDTH = 16;
   public static final int BACKGROUND_TILE_HEIGHT = 16;
   public static final int BACKGROUND_TILE_COUNT_X = 14;
   public static final int BACKGROUND_TILE_COUNT_Y = 7;

   public boolean hoverElementFire = false;
   public boolean hoverElementWater = false;
   public boolean hoverElementEarth = false;
   public boolean hoverElementWind = false;

   StatsIcons statsIcons = new StatsIcons();
   private double scrollX;
   private double scrollY;

   //private final StatsWidget root;
   private boolean centered;

   private final Map<Advancement, StatsTab> tabs = Maps.newLinkedHashMap();
   @Nullable
   //private StatsTab selectedTab;
   private boolean isScrolling;


   public StatsScreen() {
      super(GameNarrator.NO_TITLE);
   }



   protected void init() {

   }

   public void removed() {

   }

   public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
      if (pButton == 0) {
         if (hoverElementFire) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            Minecraft.getInstance().setScreen(new FireElementScreen());
         }
         if (hoverElementWater) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            Minecraft.getInstance().setScreen(new WaterElementScreen());
         }
         if (hoverElementEarth) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            Minecraft.getInstance().setScreen(new EarthElementScreen());
         }
         if (hoverElementWind) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            Minecraft.getInstance().setScreen(new WindElementScreen());
         }
      }

      return super.mouseClicked(pMouseX, pMouseY, pButton);

   }




   public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
      if (this.minecraft.options.keyAdvancements.matches(pKeyCode, pScanCode)) {
         this.minecraft.setScreen((Screen)null);
         this.minecraft.mouseHandler.grabMouse();
         return true;
      } else {
         return super.keyPressed(pKeyCode, pScanCode, pModifiers);
      }
   }

   public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
      int i = (this.width - WINDOW_WIDTH) / 2;
      int j = (this.height - WINDOW_HEIGHT) / 2;
      this.renderBackground(pPoseStack);
      this.renderInside(pPoseStack, pMouseX, pMouseY, i, j);


      this.renderWindow(pPoseStack, i, j);


   }




   private void renderInside(PoseStack pPoseStack, int pMouseX, int pMouseY, int pOffsetX, int pOffsetY) {

         PoseStack posestack = RenderSystem.getModelViewStack();
         posestack.pushPose();
         posestack.translate((double)(pOffsetX + 9), (double)(pOffsetY + 18), 0.0D);
         RenderSystem.applyModelViewMatrix();

         posestack.popPose();
         RenderSystem.applyModelViewMatrix();
         RenderSystem.depthFunc(515);
         RenderSystem.disableDepthTest();



         ///////
      if (!this.centered) {
         this.scrollX = (-117.0D );
         this.scrollY = (-103.5D );
         this.centered = true;
      }


      pPoseStack.pushPose();
      pPoseStack.translate(pOffsetX+9, pOffsetY+18, 0.0D);
      RenderSystem.enableDepthTest();
      RenderSystem.colorMask(false, false, false, false);
      fill(pPoseStack, 4680, 2260, -4680, -2260, -16777216);
      RenderSystem.colorMask(true, true, true, true);
      pPoseStack.translate(0.0D, 0.0D, -950.0D);
      RenderSystem.depthFunc(518);
      fill(pPoseStack, 234, 207, 0, 0, -16777216);
      RenderSystem.depthFunc(515);
      ResourceLocation resourcelocation = WINDOW_BACKGROUND;
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderTexture(0, resourcelocation);

      int i = Mth.floor(this.scrollX);
      int j = Mth.floor(this.scrollY);
      int k = i % 16;
      int l = j % 16;

      for(int i1 = -1; i1 <= 15; ++i1) {
         for(int j1 = -1; j1 <= 15; ++j1) {
            blit(pPoseStack, k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
         }
      }

      //RenderSystem.setShader(GameRenderer::getPositionTexShader);


      //Fire icon
      int fireDisplacementX = -78;
      int fireDisplacementY = 0;
      if (statsIcons.isMouseOver(i+fireDisplacementX+WINDOW_INSIDE_WIDTH+pOffsetX+26,j+fireDisplacementY+WINDOW_INSIDE_HEIGHT+pOffsetY+26,pMouseX+18,pMouseY+9,26,26)) {
         RenderSystem.setShaderTexture(0, FIRE_ICON_HIGH);
         blit(pPoseStack, i+fireDisplacementX-13+WINDOW_INSIDE_WIDTH,j+fireDisplacementY-13+WINDOW_INSIDE_HEIGHT,0,0,26,26,26,26);
         hoverElementFire = true;
      } else {
         hoverElementFire = false;
         RenderSystem.setShaderTexture(0, FIRE_ICON);
         blit(pPoseStack, i+fireDisplacementX-13+WINDOW_INSIDE_WIDTH,j+fireDisplacementY-13+WINDOW_INSIDE_HEIGHT,0,0,26,26,26,26);
      }
      //Water icon
      int waterDisplacementX = 0;
      int waterDisplacementY = 78;
      if (statsIcons.isMouseOver(i+waterDisplacementX+WINDOW_INSIDE_WIDTH+pOffsetX+26,j+waterDisplacementY+WINDOW_INSIDE_HEIGHT+pOffsetY+26,pMouseX+18,pMouseY+9,26,26)) {
         RenderSystem.setShaderTexture(0, WATER_ICON_HIGH);
         blit(pPoseStack, i+waterDisplacementX-13+WINDOW_INSIDE_WIDTH,j+waterDisplacementY-13+WINDOW_INSIDE_HEIGHT,0,0,26,26,26,26);
         hoverElementWater = true;
      } else {
         hoverElementWater = false;
         RenderSystem.setShaderTexture(0, WATER_ICON);
         blit(pPoseStack, i+waterDisplacementX-13+WINDOW_INSIDE_WIDTH,j+waterDisplacementY-13+WINDOW_INSIDE_HEIGHT,0,0,26,26,26,26);
      }
      //Earth icon
      int earthDisplacementX = 0;
      int earthDisplacementY = -78;
      if (statsIcons.isMouseOver(i+earthDisplacementX+WINDOW_INSIDE_WIDTH+pOffsetX+26,j+earthDisplacementY+WINDOW_INSIDE_HEIGHT+pOffsetY+26,pMouseX+18,pMouseY+9,26,26)) {
         RenderSystem.setShaderTexture(0, EARTH_ICON_HIGH);
         blit(pPoseStack, i+earthDisplacementX-13+WINDOW_INSIDE_WIDTH,j+earthDisplacementY-13+WINDOW_INSIDE_HEIGHT,0,0,26,26,26,26);
         hoverElementEarth = true;
      } else {
         hoverElementEarth = false;
         RenderSystem.setShaderTexture(0, EARTH_ICON);
         blit(pPoseStack, i+earthDisplacementX-13+WINDOW_INSIDE_WIDTH,j+earthDisplacementY-13+WINDOW_INSIDE_HEIGHT,0,0,26,26,26,26);
      }
      //Wind icon
      int windDisplacementX = 78;
      int windDisplacementY = 0;
      if (statsIcons.isMouseOver(i+windDisplacementX+WINDOW_INSIDE_WIDTH+pOffsetX+26,j+windDisplacementY+WINDOW_INSIDE_HEIGHT+pOffsetY+26,pMouseX+18,pMouseY+9,26,26)) {
         RenderSystem.setShaderTexture(0, WIND_ICON_HIGH);
         blit(pPoseStack, i+windDisplacementX-13+WINDOW_INSIDE_WIDTH,j+windDisplacementY-13+WINDOW_INSIDE_HEIGHT,0,0,26,26,26,26);
         hoverElementWind = true;
      } else {
         hoverElementWind = false;
         RenderSystem.setShaderTexture(0, WIND_ICON);
         blit(pPoseStack, i+windDisplacementX-13+WINDOW_INSIDE_WIDTH,j+windDisplacementY-13+WINDOW_INSIDE_HEIGHT,0,0,26,26,26,26);
      }




      pPoseStack.popPose();

   }

   public void renderWindow(PoseStack pPoseStack, int pOffsetX, int pOffsetY) {

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.enableBlend();
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderTexture(0, WINDOW_LOCATION);
      this.blit(pPoseStack, pOffsetX, pOffsetY, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
      this.font.draw(pPoseStack, "Main Mod Menu UWU", (float)(pOffsetX + 8), (float)(pOffsetY + 6), 16753920);

   }

   public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
      if (pButton != 0) {
         this.isScrolling = false;
         return false;
      } else {
         if (!this.isScrolling) {
            this.isScrolling = true;
         } else {
            this.scroll(pDragX, pDragY);
         }
         return true;
      }
   }
   public void scroll(double pDragX, double pDragY) {
      this.scrollX = Mth.clamp(this.scrollX + pDragX, -234.0D, 0.0D);
      this.scrollY = Mth.clamp(this.scrollY + pDragY, -207.0D, 0.0D);
   }

}
