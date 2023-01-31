package horncake.testmod.item;

import horncake.testmod.block.tile.TileTestPedestal;
import horncake.testmod.entity.ProjectileBound;
import horncake.testmod.entity.ProjectileStraight;
import horncake.testmod.entity.ProjectileTest;
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
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!Screen.hasShiftDown()) {
            player.getCooldowns().addCooldown(this, 5);
            ItemStack stack = player.getItemInHand(hand);
            int type = stack.getTag() != null && stack.getTag().contains("Type") ? stack.getTag().getInt("Type") : 0;
            int range = stack.getTag() != null && stack.getTag().contains("Range") ? stack.getTag().getInt("Range") : 0;
            int count = stack.getTag() != null && stack.getTag().contains("Count") ? stack.getTag().getInt("Count") : 0;
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

            BlockEntity entity = level.getBlockEntity(new BlockPos(player.position()));
            if(entity instanceof TileTestPedestal tile && level.isClientSide) {
                Log.info(tile.getItem());
            }
            if (!level.isClientSide) {
                shootProjectiles(level, player,type,range,count);
                //player.sendSystemMessage(Component.literal("Shoot!"));
                level.playSound(null,player.position().x, player.position().y, player.position().z,
                        SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS,2f,2f);
            }
        }

        return super.use(level, player, hand);

    }

    public void shootProjectiles(Level level, Player player, int type, int range, int count) {
        double rotMax = range;

        Vec3 pos = player.position();
        double xRot = -player.getXRot();
        double yRot = -player.getYRot();
        double dy = Math.sin(xRot * (Math.PI / 180));

        //player.sendSystemMessage(Component.literal("X: "+ xRot + "\nY: " + yRot));
        if(count > 1) {
            for (int i = 0; i < count; i++) {
                double yRot2 = rotMax * (((double) i / (count - 1)) - 0.5) + yRot;
                double dx = Math.sin(yRot2 * (Math.PI / 180)) * Math.cos(xRot * (Math.PI / 180));
                double dz = Math.cos(yRot2 * (Math.PI / 180)) * Math.cos(xRot * (Math.PI / 180));

                ProjectileTest projectile = (type == 0 ? new ProjectileStraight(level) : new ProjectileBound(level));
                projectile.shoot(pos.x, pos.y, pos.z, dx, dy, dz, 2f, 0.1f, player);
                level.addFreshEntity(projectile);
            }
        } else {
            ProjectileTest projectile = (type == 0 ? new ProjectileStraight(level) : new ProjectileBound(level));
            projectile.shoot(pos.x, pos.y, pos.z, player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, 2f, 0.1f, player);
            level.addFreshEntity(projectile);
        }
    }
}
