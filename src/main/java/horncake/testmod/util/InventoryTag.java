package horncake.testmod.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jline.utils.Log;

public class InventoryTag {

    public static ListTag createNBT(Container container) {
        ListTag list = new ListTag();
        for(int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if(!itemStack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                itemStack.save(itemTag);
                list.add(itemTag);
            }
        }
        return list;
    }

    public static void createContainer(Container container, ListTag listTag) {
        for(int i = 0; i < listTag.size(); i++) {
            CompoundTag itemTag = listTag.getCompound(i);
            int slot = itemTag.getInt("Slot");
            if(slot >= 0 && slot < container.getContainerSize()) {
                container.setItem(slot, ItemStack.of(itemTag));
            }
        }
    }

    public static ItemStack getItemFromSlot(ListTag listTag, int slot) {
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag itemTag = listTag.getCompound(i);
            int slot2 = itemTag.getInt("Slot");
            if(slot == slot2 && slot >= 0) {
                return ItemStack.of(itemTag);
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean hasItem(ListTag listTag, int slot) {
        for (int i = 0; i < listTag.size(); i++) {
            int slot2 = listTag.getCompound(i).getInt("Slot");
            if(slot == slot2 && slot >= 0) {
                return true;
            }
        }
        return false;
    }
}
