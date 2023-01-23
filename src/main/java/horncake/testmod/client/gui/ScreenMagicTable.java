package horncake.testmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import horncake.testmod.TestMod;
import horncake.testmod.init.RegisterMessage;
import horncake.testmod.network.PacketMagicTableCreateResult;
import horncake.testmod.network.PacketMagicTableSetText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jline.utils.Log;

import java.util.function.Consumer;

public class ScreenMagicTable extends AbstractContainerScreen<MenuMagicTable> {
    private EditBox typeText;
    private EditBox countText;
    private EditBox rangeText;

    private Button createButton;

    private boolean isInitialized;

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
        this.typeText.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.countText.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.rangeText.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.createButton.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.typeText.tick();
        this.countText.tick();
        this.rangeText.tick();
        if(this.menu.isRemoved == true) {
            this.resetText();
            this.menu.setSlotRemoved(false);
            Log.info("aaa");
        } else if(this.menu.slots.get(0).hasItem() && !isInitialized) {
            this.initText();
        }
    }

    @Override
    protected void init() {
        super.init();

        int w = (this.width - this.imageWidth) / 2;
        int h = (this.height - this.imageHeight) / 2;
        this.typeText = new EditBox(this.font, w + 62, h + 24, 50, 12, Component.literal("Type"));
        this.countText = new EditBox(this.font, w + 62, h + 36, 50, 12, Component.literal("Count"));
        this.rangeText = new EditBox(this.font, w + 62, h + 48, 50, 12, Component.literal("Count"));

        this.typeText.setResponder(this::onTypeChanged);
        this.countText.setResponder(this::onCountChanged);
        this.rangeText.setResponder(this::onRangeChanged);
        initSimpleEditBox(typeText);
        initSimpleEditBox(countText);
        initSimpleEditBox(rangeText);



        this.createButton = this.addRenderableWidget(new Button(this.width / 2 - 4 - 150, this.height / 4 + 120 - 12, 40, 20, CommonComponents.GUI_DONE, (p_97691_) -> {
            this.menu.createResult();
            RegisterMessage.sendToServer(new PacketMagicTableCreateResult());
        }));
    }

    private void initSimpleEditBox(EditBox text) {
        text.setCanLoseFocus(false);
        text.setTextColor(-1);
        text.setTextColorUneditable(-1);
        text.setBordered(false);
        this.setInitialFocus(text);
        text.setValue("");
        text.setEditable(true);
        this.addWidget(text);
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

    private void initText() {
        ItemStack stack = this.menu.inputSlot.getItem(0);
        CompoundTag tag = stack.getOrCreateTag();
        this.typeText.setValue(String.valueOf(tag.getInt("Type")));
        this.countText.setValue(String.valueOf(tag.getInt("Count")));
        this.rangeText.setValue(String.valueOf(tag.getInt("Range")));

        this.isInitialized = true;
    }

    private void resetText() {
        this.typeText.setValue("");
        this.countText.setValue("");
        this.rangeText.setValue("");

        this.isInitialized = false;
    }

    //さすがに糞コード
    private void onTypeChanged(String string) {
        if (!string.isEmpty()) {
            this.menu.setData(string, this.countText.getValue(), this.rangeText.getValue());
            RegisterMessage.sendToServer(new PacketMagicTableSetText(string, this.countText.getValue(), this.rangeText.getValue()));
        }
    }

    private void onCountChanged(String string) {
        if (!string.isEmpty()) {
            this.menu.setData(this.typeText.getValue(), string, this.rangeText.getValue());
            RegisterMessage.sendToServer(new PacketMagicTableSetText(this.typeText.getValue(), string, this.rangeText.getValue()));
        }
    }

    private void onRangeChanged(String string) {
        if (!string.isEmpty()) {
            this.menu.setData(this.typeText.getValue(), this.countText.getValue(), string);
            RegisterMessage.sendToServer(new PacketMagicTableSetText(this.typeText.getValue(), this.countText.getValue(), string));
        }
    }

}
