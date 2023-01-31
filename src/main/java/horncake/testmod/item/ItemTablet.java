package horncake.testmod.item;

import horncake.testmod.client.gui.ScreenRadialMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemTablet extends Item implements IKeyInputProvider {
    public ItemTablet(Properties properties) {
        super(properties);
    }

    @Override
    public void onKeyPressed(Player player, ItemStack stack) {
        player.sendSystemMessage(Component.literal("HI!"));
        Minecraft.getInstance().setScreen(new ScreenRadialMenu(new ItemStack(this)));
    }
}
