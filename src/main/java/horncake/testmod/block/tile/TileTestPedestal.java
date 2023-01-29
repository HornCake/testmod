package horncake.testmod.block.tile;

import horncake.testmod.init.RegisterBlock;
import horncake.testmod.init.RegisterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileTestPedestal extends BlockEntity implements Container {
    protected ItemStack itemStack = ItemStack.EMPTY;
    public ItemEntity itemEntity;
    public TileTestPedestal(BlockPos pPos, BlockState pBlockState) {
        super(RegisterBlockEntity.TILE_TEST_PEDESTAL.get(), pPos, pBlockState);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return itemStack.isEmpty();
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return itemStack;
    }

    public ItemStack getItem() {
        return itemStack;
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack copy = itemStack.copy().split(pAmount);
        itemStack.shrink(pAmount);
        setChanged();
        return copy;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return itemStack;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        this.itemStack = pStack;
        setChanged();
    }

    public void setItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        setChanged();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

    @Override
    public void clearContent() {
        this.itemStack = ItemStack.EMPTY;
        setChanged();
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        //if (pTag.contains("Item",10)) {
            this.setItem(ItemStack.of(pTag.getCompound("Item")));
        //}
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("Item", this.getItem().save(new CompoundTag()));
    }
}
