package horncake.testmod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class PacketBase {

    public abstract void write(FriendlyByteBuf buf);

    public abstract boolean handle(Supplier<NetworkEvent.Context> supplier);
}
