package horncake.testmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import horncake.testmod.TestMod;
import horncake.testmod.client.gui.widget.DataEditBoxes;
import horncake.testmod.client.gui.widget.MultipleEditBoxes;
import horncake.testmod.init.RegisterMessage;
import horncake.testmod.network.PacketMagicTableCreateResult;
import horncake.testmod.network.PacketMagicTableSetText;
import horncake.testmod.util.MediumData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jline.utils.Log;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ScreenMagicTable extends AbstractContainerScreen<MenuMagicTable> {
    private static final List<String> KEYS = MediumData.NAME_LIST2;
    private static final int BOX_SIZE = KEYS.size();
    private static final DataEditBoxes BOXES = new DataEditBoxes(KEYS);
    /*
    private EditBox typeText;
    private EditBox countText;
    private EditBox rangeText;
     */
    private Button createButton;
    private boolean isInitialized;

    private final int BOX_X_OFFSET = 62;
    private final int BOX_Y_OFFSET = 14;
    private final int BOX_GAP = 8;

    public ScreenMagicTable(MenuMagicTable pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        int initX = (this.width - this.imageWidth) / 2;
        int initY = (this.height - this.imageHeight) / 2;
        // Background is typically rendered first
        this.renderBackground(pPoseStack);

        // Render things here before widgets (background textures)

        // Then the widgets if this is a direct child of the Screen

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        for(int i = 0; i < KEYS.size(); i++) {
            String key = KEYS.get(i);
            if(BOXES.getBox(key).isFocused()) {
                BOXES.render(key, pPoseStack, pMouseX, pMouseY, pPartialTick);
            } else {
                drawString(pPoseStack, this.font, BOXES.getValue(key), initX + BOX_X_OFFSET, initY + BOX_Y_OFFSET + BOX_GAP * i, 1);
            }
        }
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
        /*
        this.typeText.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.countText.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.rangeText.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

         */
        this.createButton.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        /*
        this.typeText.tick();
        this.countText.tick();
        this.rangeText.tick();
         */
        BOXES.tick();
        if(this.menu.isRemoved == true) {
            this.resetText();
            this.menu.setSlotRemoved(false);
            //Log.info("aaa");
        } else if(this.menu.slots.get(0).hasItem() && !isInitialized) {
            this.initText();
        }
    }

    @Override
    protected void init() {
        super.init();
        int initX = (this.width - this.imageWidth) / 2;
        int initY = (this.height - this.imageHeight) / 2;

        for (int i = 0; i < KEYS.size(); i++) {
            BOXES.setEditBox(KEYS.get(i),new EditBox(this.font, initX + BOX_X_OFFSET, initY + BOX_Y_OFFSET + (BOX_GAP * i), 50, 12, Component.literal(KEYS.get(i))) {
                @Override
                public void onRelease(double pMouseX, double pMouseY) {
                    Log.info("hi");
                    BOXES.resetFocus();
                    setFocus(true);
                    super.onRelease(pMouseX, pMouseY);
                }
                @Override
                public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
                    String key = BOXES.getFocus();
                    Log.info("HAI");
                    if(key == null) return super.keyPressed(pKeyCode, pScanCode, pModifiers);
                    int i = BOXES.getIndex(key);
                    double x = Minecraft.getInstance().mouseHandler.xpos();
                    double y = Minecraft.getInstance().mouseHandler.ypos();
                    if(pKeyCode == GLFW.GLFW_KEY_UP) {
                        BOXES.setFocus(KEYS.get(Math.floorMod(i - 1, BOX_SIZE)));
                    } else if (pKeyCode == GLFW.GLFW_KEY_DOWN) {
                        BOXES.setFocus(KEYS.get(Math.floorMod(i + 1, BOX_SIZE)));
                    }
                    return super.keyPressed(pKeyCode, pScanCode, pModifiers);
                }
            });
        }
        /*
        this.typeText = new EditBox(this.font, w + 62, h + 24, 50, 12, Component.literal("Type"));
        this.countText = new EditBox(this.font, w + 62, h + 36, 50, 12, Component.literal("Count"));
        this.rangeText = new EditBox(this.font, w + 62, h + 48, 50, 12, Component.literal("Count"));
         */
        /*
        BOXES.getBox("Type").setResponder(this::onTypeChanged);
        BOXES.getBox("Count").setResponder(this::onCountChanged);
        BOXES.getBox("Range").setResponder(this::onRangeChanged);
         */
        BOXES.setResponders();
        KEYS.forEach(key -> initSimpleEditBox(BOXES.getBox(key)));



        this.createButton = this.addRenderableWidget(new Button(this.width / 2 - 4 - 150, this.height / 4 + 120 - 12, 40, 20, CommonComponents.GUI_DONE, (p_97691_) -> {
            this.menu.createResult();
            RegisterMessage.sendToServer(new PacketMagicTableCreateResult());
        }));
    }

    private void initSimpleEditBox(EditBox text) {
        text.setCanLoseFocus(true);
        text.setTextColor(-1);
        text.setBordered(false);
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
        //nbtタグとEditBoxのKEYは一致させること
        KEYS.forEach(key -> BOXES.setValueFromTag(key,tag));
        /*
        this.typeText.setValue(String.valueOf(tag.getInt("Type")));
        this.countText.setValue(String.valueOf(tag.getInt("Count")));
        this.rangeText.setValue(String.valueOf(tag.getInt("Range")));
         */
        this.isInitialized = true;
    }

    private void resetText() {
        BOXES.setAllValue("");

        this.isInitialized = false;
    }

    /*
    //さすがに糞コード
    private void onTypeChanged(String string) {
        if (!string.isEmpty()) {
            this.menu.setData(string, BOXES.getBox("Count").getValue(), BOXES.getBox("Range").getValue());
            RegisterMessage.sendToServer(new PacketMagicTableSetText(string, BOXES.getBox("Count").getValue(), BOXES.getBox("Range").getValue()));
        }
    }

    private void onCountChanged(String string) {
        if (!string.isEmpty()) {
            this.menu.setData(BOXES.getBox("Type").getValue(), string, BOXES.getBox("Range").getValue());
            RegisterMessage.sendToServer(new PacketMagicTableSetText(BOXES.getBox("Type").getValue(), string, BOXES.getBox("Range").getValue()));
        }
    }

    private void onRangeChanged(String string) {
        if (!string.isEmpty()) {
            this.menu.setData(BOXES.getBox("Type").getValue(), BOXES.getBox("Count").getValue(), string);
            RegisterMessage.sendToServer(new PacketMagicTableSetText(BOXES.getBox("Type").getValue(), BOXES.getBox("Count").getValue(), string));
        }
    }

     */

}
