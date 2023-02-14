package horncake.testmod.item;

import horncake.testmod.block.tile.TileTestPedestal;
import horncake.testmod.entity.ProjectileBound;
import horncake.testmod.entity.ProjectileStraight;
import horncake.testmod.entity.ProjectileTest;
import horncake.testmod.init.RegisterParticle;
import horncake.testmod.util.CasterUtil;
import horncake.testmod.util.MediumData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StoneButtonBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.jline.utils.Log;

import java.util.List;

public class ItemTest3 extends Item {

    public ItemTest3(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if(pStack.getTag() != null) {
            pTooltipComponents.add(Component.literal("Type : " + pStack.getTag().getInt("Type")));
            pTooltipComponents.add(Component.literal("Count : " + pStack.getTag().getInt("Count")));
            pTooltipComponents.add(Component.literal("Range : " + pStack.getTag().getInt("Range")));
            pTooltipComponents.add(Component.literal("Velocity : " + pStack.getTag().getInt("Velocity")));
            pTooltipComponents.add(Component.literal("Color : "
                    + pStack.getTag().getCompound("Color").getInt("R") + ","
                    + pStack.getTag().getCompound("Color").getInt("G") + ","
                    + pStack.getTag().getCompound("Color").getInt("B")));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!Screen.hasShiftDown()) {
            player.getCooldowns().addCooldown(this, 5);
            ItemStack stack = player.getItemInHand(hand);
            /*
            CompoundTag tag = new CompoundTag();
            tag.putInt("A",1);
            tag.putInt("B",-1);
            ListTag listtag = new ListTag();
            listtag.add(tag);
            listtag.add(tag);
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.put("Test",listtag);
            stack.setTag(nbt);
            */


            CasterUtil.shoot(player, level,stack.getTag());
                //player.sendSystemMessage(Component.literal("Shoot!"));
        }

        return super.use(level, player, hand);

    }
}
