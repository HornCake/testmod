package horncake.testmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import horncake.testmod.TestMod;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScreenCasterBoard extends AbstractContainerScreen<MenuCasterBoard> {
    private MenuCasterBoard menu;

    public ScreenCasterBoard(MenuCasterBoard pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.menu = pMenu;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        //下層
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderSelectedBox(pPoseStack);

        this.renderTooltip(pPoseStack,pMouseX,pMouseY);
        //上層
    }

    private void renderSelectedBox(PoseStack poseStack) {
        ResourceLocation resourceLocation = new ResourceLocation(TestMod.MODID,"textures/gui/selected.png");
        RenderSystem.setShaderTexture(0, resourceLocation);
        //なんかこれすると重なりがいい感じに
        RenderSystem.disableDepthTest();
        int size = 24;
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height + this.imageHeight) / 2;
        this.blit(poseStack, i + 4 + menu.selectedSlot * 18, j - 28, 0, 0, size, size, size, size);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0,new ResourceLocation(TestMod.MODID,"textures/gui/board.png"));

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }
}
