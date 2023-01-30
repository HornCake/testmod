package horncake.testmod.network;

import horncake.testmod.block.tile.TileMagicTable;
import horncake.testmod.client.gui.MenuMagicTable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jline.utils.Log;

import java.util.function.Supplier;

public class PacketMagicTableCloseMenu extends PacketBase {
    private static TileMagicTable tile;
    private static ItemStack itemStack;
    public PacketMagicTableCloseMenu(ItemStack itemStack, TileMagicTable tile) {
        this.tile = tile;
        this.itemStack = itemStack;
    }


    public PacketMagicTableCloseMenu(FriendlyByteBuf buf) {
    }

    @Override
    public void write(FriendlyByteBuf buf) {

    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            tile.setItem(itemStack);
        });

        return true;
    }
}
