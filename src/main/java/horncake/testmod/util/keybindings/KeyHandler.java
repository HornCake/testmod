package horncake.testmod.util.keybindings;

import horncake.testmod.TestMod;
import horncake.testmod.item.IKeyInputProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TestMod.MODID, value = Dist.CLIENT)
public class KeyHandler {
    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    public static void checkKey(int key) {
        Player player = MINECRAFT.player;
        ItemStack itemStack = player.getMainHandItem();
        if(itemStack == null) return;
        if(key == KeyBindings.OPEN_TABLET.getKey().getValue()) {
            if(itemStack.getItem() instanceof IKeyInputProvider provider) {
                provider.onKeyPressed(player, itemStack);
            }
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if(MINECRAFT.player == null || event.getAction() != 1) return;

        checkKey(event.getKey());
    }
}
