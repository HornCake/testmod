package horncake.testmod.network;

import horncake.testmod.client.gui.MenuCasterBoard;
import horncake.testmod.util.CasterUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;


import java.util.function.Supplier;


public class PacketOpenBoardMenu extends PacketBase {

    private static ItemStack stack;

    public PacketOpenBoardMenu(ItemStack stack) {
        this.stack = stack;
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
                            new MenuCasterBoard(id, inventory, stack, CasterUtil.getCasterSlot(player)),Component.literal("a")),
                    buf -> {
                        buf.writeItemStack(stack,false);
                        buf.writeInt(CasterUtil.getCasterSlot(sPlayer));
                    });

        });

        return true;
    }
}
