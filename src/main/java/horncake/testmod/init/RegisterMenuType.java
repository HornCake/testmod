package horncake.testmod.init;

import horncake.testmod.TestMod;
import horncake.testmod.client.gui.MenuMagicTable;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterMenuType {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TestMod.MODID);

    public static final  RegistryObject<MenuType<MenuMagicTable>> MENU_MAGIC_TABLE = MENUS.register("magic_table",
            () -> new MenuType(MenuMagicTable::new));


}