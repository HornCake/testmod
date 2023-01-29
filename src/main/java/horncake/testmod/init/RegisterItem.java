package horncake.testmod.init;

import horncake.testmod.TestMod;
import horncake.testmod.item.ItemTest;
import horncake.testmod.item.ItemTest2;
import horncake.testmod.item.ItemTest3;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterItem {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TestMod.MODID);

    public static final RegistryObject<Item> ITEM_TEST = ITEMS.register("item_test",
            () -> new ItemTest(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> ITEM_TEST2 = ITEMS.register("item_test2",
            () -> new ItemTest2(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> ITEM_TEST3 = ITEMS.register("item_test3",
            () -> new ItemTest3(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final  RegistryObject<Item> BLOCK_TEST = ITEMS.register("block_test",
            () -> new BlockItem(RegisterBlock.BLOCK_TEST.get(),new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final  RegistryObject<Item> BLOCK_MAGIC_TABLE = ITEMS.register("magic_table",
            () -> new BlockItem(RegisterBlock.MAGIC_TABLE.get(),new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final  RegistryObject<Item> BLOCK_TEST_PEDESTAL = ITEMS.register("test_pedestal",
            () -> new BlockItem(RegisterBlock.TEST_PEDESTAL.get(),new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

}
