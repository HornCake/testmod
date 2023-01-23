package horncake.testmod.init;

import horncake.testmod.TestMod;
import horncake.testmod.network.PacketMagicTableCreateResult;
import horncake.testmod.network.PacketMagicTableSetText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class RegisterMessage {
    //そのうちenumにしたい

    private static SimpleChannel INSTANCE;

    private static final int NETWORK_VERSION = 1;
    private static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);

    private static int INIT_ID = 0;

    public static int id() {
        return ++INIT_ID;
    }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(TestMod.MODID, "network"))
                .serverAcceptedVersions(NETWORK_VERSION_STR::equals)
                .clientAcceptedVersions(NETWORK_VERSION_STR::equals)
                .networkProtocolVersion(() -> NETWORK_VERSION_STR)
                .simpleChannel();

        INSTANCE.messageBuilder(PacketMagicTableCreateResult.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketMagicTableCreateResult::new)
                .encoder(PacketMagicTableCreateResult::write)
                .consumerMainThread(PacketMagicTableCreateResult::handle)
                .add();
        INSTANCE.messageBuilder(PacketMagicTableSetText.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketMagicTableSetText::new)
                .encoder(PacketMagicTableSetText::write)
                .consumerMainThread(PacketMagicTableSetText::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
