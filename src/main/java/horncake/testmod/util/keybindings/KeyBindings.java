package horncake.testmod.util.keybindings;

import horncake.testmod.TestMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = TestMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBindings {
    private static final String CATEGORY = "key.category.testmod.general";

    public static final KeyMapping OPEN_TABLET = new KeyMapping("key.testmod.open_tablet", GLFW.GLFW_KEY_R, CATEGORY);

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.OPEN_TABLET);
    }
}
