package horncake.testmod.network;

import horncake.testmod.client.gui.MenuMagicTable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketMagicTableSetText extends PacketBase{
    private static String type;
    private static String count;
    private static String range;

    public PacketMagicTableSetText(String type, String count, String range) {
        this.type = type;
        this.count = count;
        this.range = range;
    }

    public PacketMagicTableSetText(FriendlyByteBuf buf) {}

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

            if(player.containerMenu instanceof MenuMagicTable menu) {
                menu.setData(type,count,range);

            }
        });

        return true;
    }
}
