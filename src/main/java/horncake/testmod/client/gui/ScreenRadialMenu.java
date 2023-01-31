package horncake.testmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import horncake.testmod.TestMod;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.jline.utils.Log;

public class ScreenRadialMenu extends Screen {
    private final ItemStack itemStack;
    public ScreenRadialMenu(ItemStack itemStack) {
        super(Component.literal(""));
        this.itemStack = itemStack;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        ResourceLocation resourceLocation = new ResourceLocation(TestMod.MODID,"textures/gui/radial_menu.png");
        pPoseStack.pushPose();
        RenderSystem.setShaderTexture(0, resourceLocation);
        blit(pPoseStack,(width-100)/2,(height-100)/2,0,0,100,100,100,100);
        pPoseStack.popPose();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        double distance = new Vec2((float) (pMouseX - width / 2), (float) (pMouseY - height / 2)).length();

        Log.info(Math.acos((pMouseX - width / 2) / distance) + "," + Math.asin((pMouseY - height / 2) / distance));
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
