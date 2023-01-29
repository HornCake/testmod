package horncake.testmod.client.renderer.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import horncake.testmod.block.tile.TileTestPedestal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.EmptyModel;
import net.minecraftforge.client.model.data.ModelData;
import org.jline.utils.Log;


public class TestPedestalRenderer implements BlockEntityRenderer<TileTestPedestal> {
    private final BlockEntityRendererProvider.Context context;

    public TestPedestalRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(TileTestPedestal pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        double x = pBlockEntity.getBlockPos().getX();
        double y = pBlockEntity.getBlockPos().getY();
        double z = pBlockEntity.getBlockPos().getZ();

        final BlockRenderDispatcher dispatcher = this.context.getBlockRenderDispatcher();

        pBlockEntity.itemEntity = new ItemEntity(pBlockEntity.getLevel(), x, y, z, pBlockEntity.getItem());
        ItemEntity itemEntity = pBlockEntity.itemEntity;
        pPoseStack.pushPose();
        Minecraft.getInstance().getEntityRenderDispatcher().render(itemEntity, 0.5, 0.5, z, itemEntity.yRotO,
                pPartialTick, pPoseStack, pBufferSource, pPackedLight);
        pPoseStack.translate(0,1,0);
        Log.info(x + "," + y + "," + z);
        dispatcher.renderSingleBlock(Blocks.GLASS.defaultBlockState(), pPoseStack, pBufferSource, pPackedOverlay, pPackedLight);

        pPoseStack.popPose();

    }
}
