package horncake.testmod.client.gui;

import horncake.testmod.init.RegisterBlock;
import horncake.testmod.init.RegisterMenuType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MenuBoardWorkbench extends AbstractContainerMenu {
    private final ContainerLevelAccess access;

    public MenuBoardWorkbench(int pContainerId, Inventory inventory) {
        this(pContainerId, inventory,ContainerLevelAccess.NULL);
    }

    public MenuBoardWorkbench(int pContainerId, Inventory inventory, ContainerLevelAccess access) {
        super(RegisterMenuType.MENU_BOARD_WORKBENCH.get(), pContainerId);
        this.access = access;
    }


    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return AbstractContainerMenu.stillValid(this.access, pPlayer, RegisterBlock.BOARD_WORKBENCH.get());
    }
}
