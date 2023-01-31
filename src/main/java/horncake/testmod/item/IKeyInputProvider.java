package horncake.testmod.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IKeyInputProvider {
    @OnlyIn(Dist.CLIENT)
    void onKeyPressed(Player player, ItemStack stack);
}
