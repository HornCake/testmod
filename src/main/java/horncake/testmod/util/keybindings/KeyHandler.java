package horncake.testmod.util.keybindings;

import horncake.testmod.TestMod;
import horncake.testmod.client.gui.ScreenRadialMenu;
import horncake.testmod.item.IKeyInputProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static horncake.testmod.util.keybindings.KeyBindings.*;

@Mod.EventBusSubscriber(modid = TestMod.MODID, value = Dist.CLIENT)
public class KeyHandler {
    private static final Minecraft MINECRAFT = Minecraft.getInstance();

    public static void checkKey(int key) {
        Player player = MINECRAFT.player;
        ItemStack itemStack = player.getMainHandItem();
        ItemStack itemStack2 = player.getOffhandItem();
        //if((itemStack.isEmpty() && itemStack2.isEmpty()) || (!(itemStack.getItem() instanceof IKeyInputProvider) && !(itemStack2.getItem() instanceof IKeyInputProvider))) player.sendSystemMessage(Component.literal("HI"));
        if (key == OPEN_TABLET.getKey().getValue() || key == CHANGE_SLOT.getKey().getValue()) {
            if(itemStack != null && itemStack.getItem() instanceof IKeyInputProvider provider) {
                provider.onKeyPressed(player,itemStack,key);
                return;
            }
            if(itemStack2 != null && itemStack2.getItem() instanceof IKeyInputProvider provider) {
                provider.onKeyPressed(player,itemStack2,key);
                return;
            }
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if(MINECRAFT.player == null || event.getAction() != 1) return;

        if(MINECRAFT.screen == null || MINECRAFT.screen instanceof ScreenRadialMenu) {
            checkKey(event.getKey());
        }
    }
}
