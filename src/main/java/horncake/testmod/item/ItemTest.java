package horncake.testmod.item;

import horncake.testmod.util.LocalVec;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jline.utils.Log;

import java.awt.geom.Arc2D;

import static horncake.testmod.util.CommonUtil.getVec3;

public class ItemTest extends Item {
    public ItemTest(Properties properties) {
        super(properties.stacksTo(1));
    }
    private static int range = 10;
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            // この条件分岐がないとClientとServerの二つで実行される
            //player.sendSystemMessage(Component.literal(player.getLookAngle().toString()));
            /*
            Vec3 direction = player.getLookAngle();
            double distance = 0;

            while(distance < 500) {
                Vec3 pos = player.position().add(0,player.getEyeHeight(),0).add(direction.scale(distance / 10));
                level.addParticle(ParticleTypes.WITCH, pos.x, pos.y, pos.z, 0,0,0);
                if (!(level.getBlockState(new BlockPos(pos)).getBlock() instanceof AirBlock)) {
                    BlockPos BP = new BlockPos(pos);

                    //player.sendSystemMessage(Component.literal(pos.toString() + "\n" + BP + "\n" + player.getEyeHeight() + "\n"));

                    pos = pos.subtract(direction.scale(0.1));
                    pos = new Vec3(Math.floor(pos.x) + 0.5,Math.floor(pos.y),Math.floor(pos.z) + 0.5);
                    //BP = new BlockPos(pos);
                    //player.sendSystemMessage(Component.literal(pos.toString() + "\n" + BP + "\n" + player.getEyeHeight()));

                    player.setPos(pos);
                    level.addParticle(ParticleTypes.END_ROD, pos.x, pos.y, pos.z, 0,0,0);
                    break;
                }
                ++distance;
            }
            player.getCooldowns().addCooldown(this, 10);
            }
            */
            player.getCooldowns().addCooldown(this, 10);
            BlockHitResult hit = rayTrace(level,player, ClipContext.Fluid.NONE);
            Vec3 pos = getVec3(hit.getBlockPos()).add(0.5,0,0.5);
            if(level.getBlockState(hit.getBlockPos().relative(Direction.UP)).getBlock() instanceof AirBlock) {
                pos = pos.add(0,1,0);
            } else {
                pos = pos.relative(player.getDirection(),-1);
            }
            player.moveTo(pos);
            level.playSound(player, pos.x,pos.y,pos.z,
                    SoundEvents.ENDERMAN_TELEPORT,SoundSource.PLAYERS,1f,1f);
        }


        return super.use(level, player, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if(level.isClientSide) {
            if (entity.getType() == EntityType.PLAYER && level.isClientSide) {
                Player player = (Player) entity;
                if(player.isHolding(this)) {
                    BlockHitResult hit = rayTrace(level,(Player)entity, ClipContext.Fluid.NONE);
                    BlockPos pos = hit.getBlockPos();
                    showBoxParticle(level,pos);

                    Vec3 vec;
                    LocalVec localVec = new LocalVec(player.getLookAngle());
                    vec = localVec.add(1,0,0).getVec3().add(player.getEyePosition());
                    level.addParticle(ParticleTypes.CRIT,vec.x,vec.y,vec.z,0,0,0);
                    vec = localVec.add(0,1,0).getVec3().add(player.getEyePosition());
                    level.addParticle(ParticleTypes.ENCHANTED_HIT,vec.x,vec.y,vec.z,0,0,0);
                    vec = localVec.add(0,0,1).getVec3().add(player.getEyePosition());
                    level.addParticle(ParticleTypes.WITCH,vec.x,vec.y,vec.z,0,0,0);
                }
            }
        }
        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
    }


    public void showBoxParticle(Level level, BlockPos pos) {
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 4; j++) {
                level.addParticle(ParticleTypes.ELECTRIC_SPARK,true,
                        pos.getX() + Math.floor(j / 2),
                        pos.getY() + i,
                        pos.getZ() + (j % 2),
                        0,0,0);

            }
        }
    }

    public BlockHitResult rayTrace(Level level, Player player, ClipContext.Fluid fluid) {
        /*
        float f = player.getXRot();
        float f1 = player.getYRot();
        Vec3 vec3 = player.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.getReachDistance();
        Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);


         */
        Vec3 pos = player.getEyePosition();
        Vec3 pos2 = player.getLookAngle().scale(range).add(pos);
        return level.clip(new ClipContext(pos, pos2, ClipContext.Block.OUTLINE, fluid, player));
    }

    public static boolean onScroll(double value) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if(player.getMainHandItem().getItem() instanceof ItemTest && Screen.hasShiftDown()) {
            range = range + (int)value;
            if(range > 0 && range <= 40) {
                player.sendSystemMessage(Component.literal(String.valueOf(range)));
                mc.level.playSound(player,player.position().x,player.position().y,player.position().z,SoundEvents.UI_BUTTON_CLICK,SoundSource.PLAYERS,0.2f,2f);
            } else {
                range = range - (int)value;
                player.sendSystemMessage(Component.literal(String.valueOf(range)));
            }
            return true;
        } else {
            return false;
        }
    }
}
