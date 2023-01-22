package horncake.testmod.init;

import horncake.testmod.TestMod;
import horncake.testmod.block.BlockMagicTable;
import horncake.testmod.block.BlockTest;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterBlock {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TestMod.MODID);

    public static final RegistryObject<Block> BLOCK_TEST = BLOCKS.register("block_test", () -> new BlockTest());
    public static final RegistryObject<Block> BLOCK_MAGIC_TABLE = BLOCKS.register("magic_table", () -> new BlockMagicTable());

}
