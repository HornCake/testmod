package horncake.testmod.event;

import horncake.testmod.TestMod;
import horncake.testmod.item.ItemTest;
import horncake.testmod.item.ItemTest3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TestMod.MODID,value = Dist.CLIENT)
public class ClientEvent {
    @SubscribeEvent
    public static void onMouseScrolled(InputEvent.MouseScrollingEvent event) {
        event.setCanceled(ItemTest.onScroll(event.getScrollDelta()));
    }
}
