package horncake.testmod.network;

import horncake.testmod.client.gui.MenuMagicTable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.jline.utils.Log;

import java.util.function.Supplier;

public class PacketMagicTableCreateResult extends PacketBase {
    public PacketMagicTableCreateResult() {}


    public PacketMagicTableCreateResult(FriendlyByteBuf buf) {
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
            Log.info("1");

            if(player.containerMenu instanceof MenuMagicTable menu) {
                menu.createResult();
                Log.info("5");

            }
        });

        return true;
    }
}
