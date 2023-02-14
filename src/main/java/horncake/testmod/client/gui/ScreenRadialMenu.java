package horncake.testmod.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import horncake.testmod.TestMod;
import horncake.testmod.init.RegisterItem;
import horncake.testmod.init.RegisterMessage;
import horncake.testmod.network.PacketSetBoardSlot;
import horncake.testmod.util.AnimationHelper;
import horncake.testmod.util.CommonUtil;
import horncake.testmod.util.InventoryTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jline.utils.Log;

import java.util.Map;

public class ScreenRadialMenu extends Screen {
    private final ItemStack itemStack;
    private static final int TEXTURE_SIZE = 100;
    private int previousSlot;


    private final AnimationHelper<Integer> radialAnimation = new AnimationHelper<>(12,0) {
        @Override
        public Integer process(int length, int frame, Integer object) {
            return (int) (this.getFactor() * TEXTURE_SIZE);
        }
    };
    private final AnimationHelper<Double> sectorAnimation = new AnimationHelper<>(10, 4.0) {
        @Override
        public Double process(int length, int frame, Double object) {
            double factor = this.getFactor();
            return -120 * Math.pow(factor,2) + 160 * factor + 40;
        }
    };


    public ScreenRadialMenu(ItemStack itemStack) {
        super(Component.literal(""));
        this.itemStack = itemStack;
        radialAnimation.start();
        sectorAnimation.start();
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        radialAnimation.tick();
        sectorAnimation.tick();

        int centerX = width / 2;
        int centerY = height / 2;

        RenderSystem.setShaderTexture(0, new ResourceLocation(TestMod.MODID,"textures/gui/board.png"));

        int size = radialAnimation.get();

        pPoseStack.pushPose();
        Quaternion quaternion = new Quaternion(new Vector3f(0,0,1),180 * (float) radialAnimation.getFactor() + 180, true);
        pPoseStack.translate(((double)width) / 2,((double)height) / 2,0);//原点移動
        pPoseStack.mulPose(quaternion);
        pPoseStack.translate(-(double) size/2,-(double) size/2,0);//回転後図形を移動
        blit(pPoseStack, 0, 0,0,0,size, size, size, size);

        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();

        builder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);

        //時計回りじゃないと裏面(透明)が表示される!!!
        float argument = CommonUtil.getArg((float) (pMouseX - centerX), (float) -(pMouseY - centerY));
        int slot = Math.floorMod(Math.round((argument / Math.PI) * 4),8);
        if(previousSlot != slot) {
            sectorAnimation.start();
            previousSlot = slot;
        }
        double theta =  (-2 * Math.PI *  slot / 8) + Math.PI / 8;
        double sectorSize = sectorAnimation.get();

        builder.vertex(centerX,centerY,10).color(255,255,255,100).endVertex();
        for(int i = 0 ; i <= 10; i++) {
            builder.vertex(centerX + sectorSize * Math.cos(theta - (Math.PI / 4) * i / 10),
                            centerY + sectorSize * Math.sin(theta - (Math.PI / 4) * i / 10),
                            10)
                    .color(255,255,255,100).endVertex();

        }
        builder.vertex(centerX + sectorSize * Math.cos(theta - Math.PI / 8),centerY + sectorSize * Math.sin(theta - Math.PI / 8),10).color(255,255,255,100).endVertex();
        /*
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        for(int i = 0; i < 8; i++) {
            double theta =  -2 * Math.PI * i / 8 - 2 * Math.PI * factor - Math.PI / 8;
            buffer.vertex(((double)width) / 2 + 100 * factor * Math.cos(theta), ((double)height) / 2 + 100 * factor * Math.sin(theta),0).color(100,100,100,120).endVertex();
        }
         */
        tesselator.end();
        pPoseStack.popPose();

        //this.itemRenderer.renderAndDecorateItem(itemStack,0,0);
        /*
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        BakedModel bakedmodel = this.itemRenderer.getItemModelShaper().getItemModel(itemStack);
        this.itemRenderer.render(itemStack, ItemTransforms.TransformType.GUI, false, pPoseStack, bufferSource, 1, 1, bakedmodel);
         */

        //BakedModel bakedmodel = this.itemRenderer.getItemModelShaper().getItemModel(itemStack);

        double radius = 30 * radialAnimation.getFactor();

        //this.itemRenderer.renderAndDecorateItem(new ItemStack(RegisterItem.BLOCK_BOARD_WORKBENCH.get()),-8,-8);


        if(this.itemStack.getTag() != null && this.itemStack.getTag().contains("Items")) {
            Map<Integer, ItemStack> map = InventoryTag.getItems(this.itemStack.getTag().getList("Items", Tag.TAG_COMPOUND));
            for(int i = 0; i < 8; i++) {
                if(map.get(i) != null) {
                    this.itemRenderer.renderAndDecorateItem(map.get(i),
                            (int) (centerX + radius * Math.cos(i * Math.PI / 4 - Math.PI * (1 - radialAnimation.getFactor())) - 8),
                            (int) (centerY - radius * Math.sin(i * Math.PI / 4 - Math.PI * (1 - radialAnimation.getFactor()))) - 8);
                }
            }
        }

        /*
        //RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        //RenderSystem.enableBlend();
        //RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        //RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(8.0D, 8.0D, 0.0D);
        posestack.scale(1.0F, -1.0F, 1.0F);
        posestack.translate(-10,-10,0d);
        posestack.scale(256.0F, 256.0F, 256.0F);


        Quaternion quaternion1 = new Quaternion(new Vector3f(1f,0f,0f), 90, true);
        posestack.mulPose(quaternion1);
        RenderSystem.applyModelViewMatrix();//なんかよくわかんないけどいったん記録してるっぽい？
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        this.itemRenderer.render(itemStack, ItemTransforms.TransformType.GROUND, false, new PoseStack(), bufferSource, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
        bufferSource.endBatch();
        RenderSystem.enableDepthTest();

        posestack.popPose();


        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
         */


    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        float argument = CommonUtil.getArg((float) (pMouseX - width / 2), (float) -(pMouseY - height / 2));
        int slot = Math.floorMod(Math.round((argument / Math.PI) * 4),8);
        RegisterMessage.sendToServer(new PacketSetBoardSlot(slot));
        this.onClose();
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

}
