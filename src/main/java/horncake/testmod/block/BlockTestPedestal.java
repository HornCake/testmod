package horncake.testmod.block;

import horncake.testmod.block.tile.TileTestPedestal;
import horncake.testmod.init.RegisterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.jline.utils.Log;

public class BlockTestPedestal extends Block implements EntityBlock {
    public BlockTestPedestal() {
        super(Properties.of(Material.STONE));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide && pLevel.getBlockEntity(pPos) instanceof TileTestPedestal tile) {
            if(!tile.isEmpty()) {
                ItemEntity entity = new ItemEntity(pLevel, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), tile.getItem());
                pLevel.addFreshEntity(entity);
                tile.setItem(ItemStack.EMPTY);
                Log.info("waa");
            }
            if(!pPlayer.getItemInHand(pHand).isEmpty()) {
                tile.setItem(pPlayer.getInventory().removeItem(pPlayer.getInventory().selected, 1));
                Log.info("hhaa");
            }
            pLevel.sendBlockUpdated(pPos,pState,pState, 2);
        }
        return InteractionResult.SUCCESS;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegisterBlockEntity.TILE_TEST_PEDESTAL.get().create(pPos,pState);
    }
}
