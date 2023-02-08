package horncake.testmod.network;

import horncake.testmod.client.gui.MenuCasterBoard;
import horncake.testmod.util.CasterUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;


import java.util.function.Supplier;


public class PacketOpenBoardMenu extends PacketBase {

    private CompoundTag tag;

    public PacketOpenBoardMenu(CompoundTag tag) {
        this.tag = tag;
    }

    public PacketOpenBoardMenu(FriendlyByteBuf buf) {
    }

    @Override
    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
           //ここで書いたことは鯖内処理になるらしい
            ServerPlayer sPlayer = context.getSender();
            if (sPlayer == null) return;
            NetworkHooks.openScreen(sPlayer, new SimpleMenuProvider((id, inventory, player) ->
                            new MenuCasterBoard(id, inventory, CasterUtil.getCaster(player).getTag()),Component.literal("a")),
                    buf -> {
                        buf.writeNbt(CasterUtil.getCaster(sPlayer).getTag());
                    });

        });

        return true;
    }
}
