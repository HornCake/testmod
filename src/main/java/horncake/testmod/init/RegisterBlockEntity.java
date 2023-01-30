package horncake.testmod.init;

import horncake.testmod.TestMod;
import horncake.testmod.block.tile.TileMagicTable;
import horncake.testmod.block.tile.TileTestPedestal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterBlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TestMod.MODID);

    public static final RegistryObject<BlockEntityType<TileTestPedestal>> TILE_TEST_PEDESTAL = BLOCK_ENTITIES.register("tile_test_pedestal",
            () -> BlockEntityType.Builder.of(TileTestPedestal::new, RegisterBlock.TEST_PEDESTAL.get()).build(null));
    public static final RegistryObject<BlockEntityType<TileMagicTable>> TILE_MAGIC_TABLE = BLOCK_ENTITIES.register("tile_magic_table",
            () -> BlockEntityType.Builder.of(TileMagicTable::new, RegisterBlock.MAGIC_TABLE.get()).build(null));
}
