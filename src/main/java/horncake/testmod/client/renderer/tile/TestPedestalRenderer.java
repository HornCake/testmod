package horncake.testmod.client.renderer.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import horncake.testmod.block.tile.TileTestPedestal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.EmptyModel;
import net.minecraftforge.client.model.data.ModelData;
import org.jline.utils.Log;


public class TestPedestalRenderer implements BlockEntityRenderer<TileTestPedestal> {
    private final BlockEntityRendererProvider.Context context;
    private final ItemRenderer itemRenderer;

    public TestPedestalRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.itemRenderer = this.context.getItemRenderer();
    }

    @Override
    public void render(TileTestPedestal pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        double x = pBlockEntity.getBlockPos().getX();
        double y = pBlockEntity.getBlockPos().getY();
        double z = pBlockEntity.getBlockPos().getZ();

        pBlockEntity.frame = (pBlockEntity.frame + 3 * pPartialTick) % 360;

        if(pBlockEntity.isEmpty()) return;

        if(pBlockEntity.itemEntity == null || !ItemStack.matches(pBlockEntity.itemEntity.getItem(), pBlockEntity.getItem())) {
            pBlockEntity.itemEntity = new ItemEntity(pBlockEntity.getLevel(), x, y, z, pBlockEntity.getItem());
        }
        ItemEntity itemEntity = pBlockEntity.itemEntity;

        BakedModel bakedmodel = this.itemRenderer.getModel(pBlockEntity.getItem(), itemEntity.level,null, itemEntity.getId());

        pPoseStack.pushPose();

        pPoseStack.translate(0.5,1, 0.5);
        Quaternion quaternion = new Quaternion(new Vector3f(0,1,0), pBlockEntity.frame, true);
        pPoseStack.mulPose(quaternion);
        pPoseStack.scale(1.2f,1.2f,1.2f);

        this.itemRenderer.render(pBlockEntity.getItem(), ItemTransforms.TransformType.GROUND, false, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, bakedmodel);

        pPoseStack.popPose();

    }
}
