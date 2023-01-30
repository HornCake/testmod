package horncake.testmod.client.renderer.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import horncake.testmod.block.tile.TileMagicTable;
import horncake.testmod.item.ItemTest3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class MagicTableRenderer implements BlockEntityRenderer<TileMagicTable> {
    private final BlockEntityRendererProvider.Context context;
    private final ItemRenderer itemRenderer;
    private ItemEntity itemEntity;
    private float frame;

    public MagicTableRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.itemRenderer = this.context.getItemRenderer();
    }

    @Override
    public void render(TileMagicTable pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        double x = pBlockEntity.getBlockPos().getX();
        double y = pBlockEntity.getBlockPos().getY();
        double z = pBlockEntity.getBlockPos().getZ();
        if(itemEntity == null || !ItemStack.matches(itemEntity.getItem(),pBlockEntity.getItem())) {
            itemEntity = new ItemEntity(pBlockEntity.getLevel(), x, y, z, pBlockEntity.getItem());
        }

        if(pBlockEntity.isEmpty() || !(pBlockEntity.getItem().getItem() instanceof ItemTest3)) return;

        frame = (frame + 3 * pPartialTick) % 360;

        BakedModel bakedmodel = this.itemRenderer.getModel(pBlockEntity.getItem(), itemEntity.level,null, itemEntity.getId());


        pPoseStack.pushPose();

        pPoseStack.translate(0.5,1.5, 0.5);
        Quaternion quaternion = new Quaternion(new Vector3f(0,1,0), frame, true);
        pPoseStack.mulPose(quaternion);

        this.itemRenderer.render(pBlockEntity.getItem(), ItemTransforms.TransformType.GROUND, false, pPoseStack, pBufferSource, pPackedLight, pPackedOverlay, bakedmodel);

        pPoseStack.popPose();

    }
}
