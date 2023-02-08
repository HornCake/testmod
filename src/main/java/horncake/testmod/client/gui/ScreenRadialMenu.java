package horncake.testmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import horncake.testmod.TestMod;
import horncake.testmod.init.RegisterMessage;
import horncake.testmod.network.PacketSetBoardSlot;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.jline.utils.Log;

public class ScreenRadialMenu extends Screen {
    private final ItemStack itemStack;
    private int radialFrame;
    private static final int RADIAL_ANIMATION_LENGTH = 10;
    private static final int TEXTURE_SIZE = 100;
    private int previousSlot;
    private double sectorSize;
    private int sectorFrame;
    private static final int SECTOR_ANIMATION_LENGTH = 10;

    public ScreenRadialMenu(ItemStack itemStack) {
        super(Component.literal(""));
        this.itemStack = itemStack;
        this.radialFrame = 0;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        if(radialFrame < RADIAL_ANIMATION_LENGTH) {
            radialFrame += 1;
        }
        ResourceLocation resourceLocation = new ResourceLocation(TestMod.MODID,"textures/gui/board.png");
        RenderSystem.setShaderTexture(0, resourceLocation);

        double factor = (double) radialFrame / RADIAL_ANIMATION_LENGTH;
        int size = (int)(factor * TEXTURE_SIZE);
        int centerX = width / 2;
        int centerY = height / 2;

        pPoseStack.pushPose();
        //BakedModel bakedmodel = this.itemRenderer.getItemModelShaper().getItemModel(itemStack);

        Quaternion quaternion = new Quaternion(new Vector3f(0,0,1),180 * (float)factor + 180, true);
        pPoseStack.translate(((double)width) / 2,((double)height) / 2,0);//原点移動
        pPoseStack.mulPose(quaternion);
        pPoseStack.translate(-size/2,-size/2,0);//回転後図形を移動
        blit(pPoseStack, 0, 0,0,0,size, size, size, size);

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);

        //時計回りじゃないと裏面(透明)が表示される!!!
        float argument = getArg((float) (pMouseX - centerX), (float) -(pMouseY - centerY));
        int slot = Math.floorMod(Math.round((argument / Math.PI) * 4),8);
        double theta =  (-2 * Math.PI *  slot / 8) + Math.PI / 8;

        if(previousSlot != slot) {
            previousSlot = slot;
            sectorSize = 40;
            sectorFrame = 0;
        }else {
            if(sectorFrame < SECTOR_ANIMATION_LENGTH) {
                sectorFrame += 1;
            }
            double factor2 = (double) sectorFrame / SECTOR_ANIMATION_LENGTH;
            sectorSize = -120 * Math.pow(factor2,2) + 160 * factor2 + 40;
        }

        buffer.vertex(centerX,centerY,10).color(255,255,255,100).endVertex();
        for(int i = 0 ; i <= 10; i++) {
            buffer.vertex(centerX + sectorSize * Math.cos(theta - (Math.PI / 4) * i / 10),
                            centerY + sectorSize * Math.sin(theta - (Math.PI / 4) * i / 10),
                            10)
                    .color(255,255,255,100).endVertex();

        }
        buffer.vertex(centerX + sectorSize * Math.cos(theta - Math.PI / 8),centerY + sectorSize * Math.sin(theta - Math.PI / 8),10).color(255,255,255,100).endVertex();


        /*
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        for(int i = 0; i < 8; i++) {
            double theta =  -2 * Math.PI * i / 8 - 2 * Math.PI * factor - Math.PI / 8;
            buffer.vertex(((double)width) / 2 + 100 * factor * Math.cos(theta), ((double)height) / 2 + 100 * factor * Math.sin(theta),0).color(100,100,100,120).endVertex();
        }
         */
        tesselator.end();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();

        //this.itemRenderer.render(itemStack, ItemTransforms.TransformType.GROUND, false, pPoseStack, null, 1, 1, bakedmodel);

        pPoseStack.popPose();
    }

    private float getArg(float x, float y) {
        double distance = new Vec2(x,y).length();
        return (float) (Math.asin(y / distance) > 0 ? Math.acos(x / distance) : 2 * Math.PI - Math.acos(x / distance));
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        float argument = getArg((float) (pMouseX - width / 2), (float) -(pMouseY - height / 2));
        int slot = Math.floorMod(Math.round((argument / Math.PI) * 4),8);
        RegisterMessage.sendToServer(new PacketSetBoardSlot(slot));
        this.onClose();
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

}
