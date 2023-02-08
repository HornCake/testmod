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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jline.utils.Log;


public class CasterUtil {
    public static void shoot(Player player, Level level, CompoundTag tag) {
        int type = (tag != null && tag.contains("Type")) ? tag.getInt("Type") : 0;
        int range = (tag != null && tag.contains("Range")) ? tag.getInt("Range") : 0;
        int count = (tag != null && tag.contains("Count")) ? tag.getInt("Count") : 0;

        Vec3 pos = player.position();
        double xRot = -player.getXRot();
        double yRot = -player.getYRot();
        double dy = Math.sin(xRot * (Math.PI / 180));

        if(count > 1) {
            for (int i = 0; i < count; i++) {
                double yRot2 = range * (((double) i / (count - 1)) - 0.5) + yRot;
                double dx = Math.sin(yRot2 * (Math.PI / 180)) * Math.cos(xRot * (Math.PI / 180));
                double dz = Math.cos(yRot2 * (Math.PI / 180)) * Math.cos(xRot * (Math.PI / 180));

                if(level.isClientSide) {
                    Vec3 direction = new Vec3(dx,dy,dz).scale(2);
                    Vec3 particlePos = player.getEyePosition().add(direction.normalize());

                    level.addParticle(RegisterParticle.PLANE_TEST_PARTICLE.get(),true,particlePos.x,particlePos.y,particlePos.z
                            ,direction.x,direction.y,direction.z);
                } else {
                    ProjectileTest projectile = (type == 0 ? new ProjectileStraight(level) : new ProjectileBound(level));
                    projectile.shoot(pos.x, pos.y, pos.z, dx, dy, dz, 2f, 0.1f, player);
                    level.addFreshEntity(projectile);
                }
            }
        } else {
            if(level.isClientSide) {
                Vec3 direction = player.getLookAngle().scale(2);
                Vec3 particlePos = player.getEyePosition().add(player.getLookAngle());

                level.addParticle(RegisterParticle.PLANE_TEST_PARTICLE.get(),true,particlePos.x,particlePos.y,particlePos.z
                        ,direction.x,direction.y,direction.z);
            } else {
                ProjectileTest projectile = (type == 0 ? new ProjectileStraight(level) : new ProjectileBound(level));
                projectile.shoot(pos.x, pos.y, pos.z, player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, 2f, 0.1f, player);
                level.addFreshEntity(projectile);
            }
        }
        level.playSound(null,player.position().x, player.position().y, player.position().z,
                SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS,2f,2f);
    }

    public static InteractionHand getCasterHand(Player player) {
        if(player.getMainHandItem().getItem() instanceof ItemCasterBoard) {
            return InteractionHand.MAIN_HAND;
        } else if (player.getOffhandItem().getItem() instanceof ItemCasterBoard) {
            return InteractionHand.OFF_HAND;
        }
        return null;
    }

    public static ItemStack getCaster(Player player) {
        if(getCasterHand(player) != null) {
            return player.getItemInHand(getCasterHand(player));
        }
        return ItemStack.EMPTY;
    }
}
