package horncake.testmod;

import com.mojang.logging.LogUtils;
import horncake.testmod.init.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TestMod.MODID)
public class TestMod {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "testmod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public TestMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::commonSetup);

        RegisterItem.ITEMS.register(bus);
        RegisterBlock.BLOCKS.register(bus);
        RegisterEntity.ENTITY_TYPES.register(bus);
        RegisterParticle.PARTICLES.register(bus);
        RegisterMenuType.MENUS.register(bus);
        RegisterBlockEntity.BLOCK_ENTITIES.register(bus);


        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }
}
