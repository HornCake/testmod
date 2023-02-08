package horncake.testmod.item;

import horncake.testmod.client.gui.MenuCasterBoard;
import horncake.testmod.client.gui.ScreenRadialMenu;
import horncake.testmod.init.RegisterMessage;
import horncake.testmod.network.PacketOpenBoardMenu;
import horncake.testmod.util.CasterUtil;
import horncake.testmod.util.InventoryTag;
import horncake.testmod.util.keybindings.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ItemCasterBoard extends Item implements IKeyInputProvider, MenuProvider {
    public ItemCasterBoard(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void onKeyPressed(Player player, ItemStack stack, int key) {
        if(player.containerMenu instanceof MenuCasterBoard) return;
        if(key == KeyBindings.OPEN_TABLET.getKey().getValue()) {
            RegisterMessage.sendToServer(new PacketOpenBoardMenu(CasterUtil.getCaster(player).getTag()));
        } else if(key == KeyBindings.CHANGE_SLOT.getKey().getValue()) {
            if(!(Minecraft.getInstance().screen instanceof ScreenRadialMenu)){
                Minecraft.getInstance().setScreen(new ScreenRadialMenu(new ItemStack(this)));
            } else {
                player.closeContainer();
            }
        }

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(CasterUtil.getCaster(pPlayer).getTag() == null || !CasterUtil.getCaster(pPlayer).getTag().contains("SelectedSlot")) {
            return super.use(pLevel, pPlayer, pUsedHand);
        }

        int slot = CasterUtil.getCaster(pPlayer).getTag().getInt("SelectedSlot");

        /*
        ItemStack itemStack = pPlayer.getMainHandItem();
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putInt("SelectedSlot",Math.floorMod(slot + 1,8));
        itemStack.setTag(tag);

         */

        ListTag listTag = CasterUtil.getCaster(pPlayer).getTag().getList("Items", Tag.TAG_COMPOUND);

        if(!InventoryTag.hasItem(listTag, slot)) {
            return super.use(pLevel, pPlayer, pUsedHand);
        }

        CasterUtil.shoot(pPlayer, pLevel, InventoryTag.getItemFromSlot(listTag,slot).getTag());
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public Component getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new MenuCasterBoard(pContainerId, pPlayerInventory);
    }
}
