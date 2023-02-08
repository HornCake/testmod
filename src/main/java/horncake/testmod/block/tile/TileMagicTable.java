package horncake.testmod.block.tile;

import horncake.testmod.client.gui.MenuMagicTable;
import horncake.testmod.init.RegisterBlockEntity;
import horncake.testmod.init.RegisterMessage;
import horncake.testmod.network.PacketMagicTableCloseMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import org.jline.utils.Log;

public class TileMagicTable extends BaseContainerBlockEntity implements Container, MenuProvider {
    protected ItemStack itemStack = ItemStack.EMPTY;

    public TileMagicTable(BlockPos pPos, BlockState pBlockState) {
        super(RegisterBlockEntity.TILE_MAGIC_TABLE.get(), pPos, pBlockState);
    }

    @Override
    public void stopOpen(Player pPlayer) {
        setItem(pPlayer.containerMenu.slots.get(0).getItem());
        Log.info(pPlayer.containerMenu.slots.get(0).getItem());
        BlockState state = getBlockState();
        level.sendBlockUpdated(worldPosition,state,state,2);
        RegisterMessage.sendToPlayer(new PacketMagicTableCloseMenu(pPlayer.containerMenu.slots.get(0).getItem(), this), (ServerPlayer) pPlayer);
        super.stopOpen(pPlayer);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("TEST");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new MenuMagicTable(pContainerId, pInventory, this);
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
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        if(pkt.getTag() != null) {
            load(pkt.getTag());
        }
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
