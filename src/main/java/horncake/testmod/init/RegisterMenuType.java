package horncake.testmod.init;

import horncake.testmod.TestMod;
import horncake.testmod.client.gui.MenuCasterBoard;
import horncake.testmod.client.gui.MenuBoardWorkbench;
import horncake.testmod.client.gui.MenuMagicTable;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterMenuType {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TestMod.MODID);

    public static final  RegistryObject<MenuType<MenuMagicTable>> MENU_MAGIC_TABLE = MENUS.register("magic_table",
            () -> new MenuType(MenuMagicTable::new));
    public static final  RegistryObject<MenuType<MenuBoardWorkbench>> MENU_BOARD_WORKBENCH = MENUS.register("board_workbench",
            () -> new MenuType(MenuBoardWorkbench::new));
    public static final  RegistryObject<MenuType<MenuCasterBoard>> MENU_CASTER_BOARD = MENUS.register("caster_board",
            () -> IForgeMenuType.create(MenuCasterBoard::new));

}