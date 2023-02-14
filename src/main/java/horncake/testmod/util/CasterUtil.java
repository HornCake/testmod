package horncake.testmod.util;

import horncake.testmod.entity.ProjectileBound;
import horncake.testmod.entity.ProjectileStraight;
import horncake.testmod.entity.ProjectileTest;
import horncake.testmod.init.RegisterParticle;
import horncake.testmod.item.ItemCasterBoard;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jline.utils.Log;


public class CasterUtil {

    public static void shoot(Player player, Level level, CompoundTag tag) {
        if(tag == null) return;
        MediumData data = new MediumData(tag);

        double xRot = -player.getXRot();
        double yRot = -player.getYRot();
        double dy = Math.sin(xRot * (Math.PI / 180));

        if(data.getCount() > 1) {
            for (int i = 0; i < data.getCount(); i++) {
                double yRot2 = data.getRange() * (((double) i / (data.getCount() - 1)) - 0.5) + yRot;
                double dx = Math.sin(yRot2 * (Math.PI / 180)) * Math.cos(xRot * (Math.PI / 180));
                double dz = Math.cos(yRot2 * (Math.PI / 180)) * Math.cos(xRot * (Math.PI / 180));

                Vec3 direction = new Vec3(dx,dy,dz);

                if(level.isClientSide) {
                    Vec3 particlePos = player.getEyePosition().add(direction);
                    level.addParticle(RegisterParticle.PLANE_TEST_PARTICLE.get(),true,particlePos.x,particlePos.y,particlePos.z
                            ,direction.x,direction.y,direction.z);
                } else {
                    addProjectileEntity(level, data.getType(), 0.5, data.getVelocity() , 0.1f, direction, data.getColor(), player);

                }
            }
        } else {
            if(level.isClientSide) {
                Vec3 direction = player.getLookAngle().scale(2);
                Vec3 particlePos = player.getEyePosition().add(player.getLookAngle());

                level.addParticle(RegisterParticle.PLANE_TEST_PARTICLE.get(),true,particlePos.x,particlePos.y,particlePos.z
                        ,direction.x,direction.y,direction.z);
            } else {
                addProjectileEntity(level, data.getType(), 0.5, data.getVelocity(), 0.1f, player.getLookAngle(), data.getColor(), player);
            }
        }
        level.playSound(null,player.position().x, player.position().y, player.position().z,
                SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS,2f,2f);
    }

    private static void addProjectileEntity(Level level, ProjectileType type, double distance, float velocity, float inaccuracy, Vec3 direction, ColorHandler color, Entity entity) {
        ProjectileTest projectile = (type == ProjectileType.BASIC ? new ProjectileStraight(level) : new ProjectileBound(level));
        projectile.shoot(entity.position().add(direction.scale(distance)), direction, velocity, 0.1f, entity);
        projectile.setColor(color);
        level.addFreshEntity(projectile);
    }



    public static InteractionHand getCasterHand(Player player) {
        if(player.getMainHandItem().getItem() instanceof ItemCasterBoard) {
            return InteractionHand.MAIN_HAND;
        } else if (player.getOffhandItem().getItem() instanceof ItemCasterBoard) {
            return InteractionHand.OFF_HAND;
        }
        return null;
    }

    public static int getCasterSlot(Player player) {
        return (getCasterHand(player) != null) ?
                ((getCasterHand(player) == InteractionHand.MAIN_HAND) ? player.getInventory().selected : 40)
                : -1;
    }

    public static ItemStack getCaster(Player player) {
        if(getCasterHand(player) != null) {
            return player.getItemInHand(getCasterHand(player));
        }
        return ItemStack.EMPTY;
    }
}
