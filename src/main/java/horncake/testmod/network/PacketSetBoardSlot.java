package horncake.testmod.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jline.utils.Log;

import java.util.function.Supplier;


public class PacketSetBoardSlot extends PacketBase {
    //なぜかstaticじゃないといけない
    private static int slot;

    public PacketSetBoardSlot(int slot) {
        this.slot = slot;
    }

    public PacketSetBoardSlot(FriendlyByteBuf buf) {
    }

    @Override
    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
           //ここで書いたことは鯖内処理になるらしい

            ServerPlayer player = context.getSender();
            if (player == null) return;
            ItemStack itemStack = player.getMainHandItem();
            CompoundTag tag = itemStack.getOrCreateTag();
            tag.putInt("SelectedSlot",slot);
            itemStack.setTag(tag);
            Log.info(slot);
        });

        return true;
    }
}
