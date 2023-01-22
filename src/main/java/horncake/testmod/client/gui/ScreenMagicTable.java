package horncake.testmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import horncake.testmod.TestMod;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ScreenMagicTable extends AbstractContainerScreen<MenuMagicTable> {
    private EditBox text;
    private Button createButton;

    public ScreenMagicTable(MenuMagicTable pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        // Background is typically rendered first
        this.renderBackground(pPoseStack);

        // Render things here before widgets (background textures)

        // Then the widgets if this is a direct child of the Screen
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.text.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.createButton.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.text.tick();
    }

    @Override
    protected void init() {
        super.init();

        int w = (this.width - this.imageWidth) / 2;
        int h = (this.height - this.imageHeight) / 2;
        this.text = new EditBox(this.font, w + 62, h +24, 50, 12, Component.literal("HI"));
        this.text.setCanLoseFocus(false);
        this.text.setTextColor(-1);
        this.text.setTextColorUneditable(-1);
        this.text.setBordered(false);
        this.text.setValue("3");
        this.text.setResponder(this::onTextChanged);
        this.addWidget(this.text);
        this.setInitialFocus(this.text);
        this.text.setEditable(true);

        this.createButton = this.addRenderableWidget(new Button(this.width / 2 - 4 - 150, this.height / 4 + 120 - 12, 40, 20, CommonComponents.GUI_DONE, (p_97691_) -> {
            this.menu.createResult();
        }));
    }


    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0,new ResourceLocation(TestMod.MODID,"textures/gui/magic_table.png"));

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    private void onTextChanged(String string) {
        if (!string.isEmpty()) {
            menu.setText(string);
            String s = string;
            Slot slot = this.menu.getSlot(0);
            if (slot != null && slot.hasItem()) {
                s = "";
            }
        }
    }

}
