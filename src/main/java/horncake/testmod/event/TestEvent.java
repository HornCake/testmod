package horncake.testmod.event;

import horncake.testmod.TestMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TestMod.MODID)
public class TestEvent {

    @SubscribeEvent
    public static void hurtEntity(LivingHurtEvent event) {
        if (event.getEntity() instanceof Sheep) {
            if (event.getSource().getEntity() instanceof Player player) {
                player.sendSystemMessage(Component.literal("SHEEP!!!"));
            }
        }
    }


}
