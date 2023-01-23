package horncake.testmod.client.gui;

import horncake.testmod.init.RegisterBlock;
import horncake.testmod.init.RegisterMenuType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jline.utils.Log;


public class MenuMagicTable extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private String typeData;
    private String countData;
    private String rangeData;



    public final Container inputSlot = new SimpleContainer(1) {
        public void setChanged() {
            MenuMagicTable.this.slotsChanged(this);
            super.setChanged();

        }
    };


    public final Container resultSlot = new ResultContainer();

    public MenuMagicTable(int id, Inventory inventory) {
        this(id,inventory,ContainerLevelAccess.NULL);
    }

    public MenuMagicTable(int id, Inventory inventory, final ContainerLevelAccess access) {
        super(RegisterMenuType.MENU_MAGIC_TABLE.get(), id);
        this.access = access;
        this.addSlot(new Slot(this.inputSlot,0,20,33));
        this.addSlot(new Slot(this.resultSlot, 0, 20, 13) {
            @Override
            public void onTake(Player pPlayer, ItemStack pStack) {
                pPlayer.playSound(SoundEvents.GLASS_BREAK,1, 1);
                inputSlot.setItem(0,ItemStack.EMPTY);
                super.onTake(pPlayer, pStack);
                setChanged();
                broadcastChanges();
            }

            @Override
            public boolean mayPlace(ItemStack pStack) {
                return false;
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }


    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 2) {
                if (!this.moveItemStackTo(itemstack1, 2, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
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


    public void setData(String type, String count, String range) {
        this.typeData = type;
        this.countData = count;
        this.rangeData = range;
    }

    private boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public void createResult() {
        Log.info("2");
        Log.info(this.countData);
        ItemStack stack = this.inputSlot.getItem(0);
        if(countData == null) { return; }
        if(!stack.isEmpty() && isInt(typeData) && isInt(countData) && isInt(rangeData)) {
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putInt("type",Integer.parseInt(typeData));
            nbt.putInt("Count",Integer.parseInt(countData));

            nbt.putInt("Range",Integer.parseInt(rangeData));

            stack.setTag(nbt);
            Log.info(countData +"sa");
            this.resultSlot.setItem(0,inputSlot.getItem(0));
            Log.info(this.resultSlot.getItem(0));
        } else {
            Log.info("4");

        }
        this.broadcastChanges();
    }



    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.access.execute((p_39796_, p_39797_) -> this.clearContainer(pPlayer, this.inputSlot));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return AbstractContainerMenu.stillValid(this.access, pPlayer, RegisterBlock.BLOCK_MAGIC_TABLE.get());
    }
}
