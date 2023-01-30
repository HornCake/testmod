package horncake.testmod.block;

import horncake.testmod.block.tile.TileTestPedestal;
import horncake.testmod.init.RegisterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
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
            }
            if(!pPlayer.getItemInHand(pHand).isEmpty()) {
                tile.setItem(pPlayer.getInventory().removeItem(pPlayer.getInventory().selected, 1));
            }
            pLevel.sendBlockUpdated(pPos,pState,pState, 2);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if(!pLevel.isClientSide && pLevel.getBlockEntity(pPos) instanceof TileTestPedestal tile) {
            if(!tile.isEmpty()) {
                ItemEntity entity = new ItemEntity(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), tile.getItem());
                pLevel.addFreshEntity(entity);
            }

        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegisterBlockEntity.TILE_TEST_PEDESTAL.get().create(pPos,pState);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape shape = Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.875, 0.9375);
        return shape;
    }
}
