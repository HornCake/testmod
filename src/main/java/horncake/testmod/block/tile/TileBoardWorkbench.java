package horncake.testmod.block.tile;

import horncake.testmod.client.gui.MenuBoardWorkbench;

import horncake.testmod.init.RegisterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.state.BlockState;

public class TileBoardWorkbench extends ContainerBlockEntity implements MenuProvider {
    public TileBoardWorkbench(BlockPos pPos, BlockState pBlockState) {
        super(RegisterBlockEntity.TILE_BOARD_WORKBENCH.get(), pPos, pBlockState, MenuBoardWorkbench::new,10);

    }

}
