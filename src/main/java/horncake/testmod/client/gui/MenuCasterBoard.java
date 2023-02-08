package horncake.testmod.client.gui;

import horncake.testmod.init.RegisterMenuType;
import horncake.testmod.item.ItemCasterBoard;
import horncake.testmod.item.ItemTest3;
import horncake.testmod.util.CasterUtil;
import horncake.testmod.util.InventoryTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jline.utils.Log;

public class MenuCasterBoard extends AbstractContainerMenu {
    private static final int CONTAINER_SIZE = 8;
    public int selectedSlot;

    public final Container container = new SimpleContainer(CONTAINER_SIZE) {
        public void setChanged() {
            MenuCasterBoard.this.slotsChanged(this);
            super.setChanged();
        }
    };

    private final Container copiedContainer;

    public MenuCasterBoard(int pContainerId, Inventory inventory) {
        this(pContainerId, inventory, new CompoundTag());
        Log.info("AA");
    }

    public MenuCasterBoard(int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        this(pContainerId, inventory, buf.readNbt());
        Log.info("BB");
    }


    public MenuCasterBoard(int pContainerId, Inventory inventory, CompoundTag tag) {
        super(RegisterMenuType.MENU_CASTER_BOARD.get(), pContainerId);
        Log.info("CC");
        Log.info(tag.contains("Items"));
        if(tag.contains("Items")) {
            InventoryTag.createContainer(this.container, tag.getList("Items", Tag.TAG_COMPOUND));
        }
        Log.info(ItemStack.of(tag.getList("Items",Tag.TAG_COMPOUND).getCompound(0)));

        this.copiedContainer = container;

        for (int i = 0; i < CONTAINER_SIZE; i++) {
            this.addSlot(new Slot(this.container, i, 8 + i * 18, 64) {
                @Override
                public boolean mayPlace(ItemStack pStack) {
                    if(pStack.getItem() instanceof ItemTest3) return super.mayPlace(pStack);
                    return false;
                }
            });
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for(int k = 0; k < 9; ++k) {
            if(!(inventory.getItem(k).getItem() instanceof ItemCasterBoard)) {
                this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
            } else {
                selectedSlot = k;
                this.addSlot(new Slot(inventory, k, 8 + k * 18, 142) {
                    @Override
                    public boolean mayPickup(Player pPlayer) {
                        return false;
                    }
                });
            }
        }
    }


    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < CONTAINER_SIZE) {
                if (!this.moveItemStackTo(itemstack1, CONTAINER_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, CONTAINER_SIZE, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        this.broadcastChanges();
        return itemstack;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        Level level = pPlayer.getLevel();
        ItemStack itemStack = CasterUtil.getCaster(pPlayer);
        if(itemStack.getItem() instanceof ItemCasterBoard) {
            CompoundTag tag = itemStack.getOrCreateTag();
            tag.put("Items", InventoryTag.createNBT(container));
            itemStack.setTag(tag);
        } else {

            for(int i = 0; i < CONTAINER_SIZE; i++) {
                if(!container.getItem(i).isEmpty() && !level.isClientSide) {
                    level.addFreshEntity(new ItemEntity(level, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), container.getItem(i)));
                }
            }
        }
    }


    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}
