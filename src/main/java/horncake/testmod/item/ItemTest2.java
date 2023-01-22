package horncake.testmod.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.phys.Vec3;

public class ItemTest2 extends Item {
    public ItemTest2(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

            if (level.isClientSide) {
                // この条件分岐がないとClientとServerの二つで実行される
                //player.sendSystemMessage(Component.literal(player.getLookAngle().toString()));

                Vec3 direction = player.getLookAngle();
                if(!player.isCrouching()) {
                    player.setDeltaMovement(direction.multiply(1.5,0.5,1.5));
                } else {
                    player.setDeltaMovement(direction.multiply(0.5,1.5,0.5));

                }
                player.resetFallDistance();

                player.getCooldowns().addCooldown(this, 20);

                level.addParticle(ParticleTypes.SWEEP_ATTACK,
                        player.position().x,player.position().y + 1,player.position().z, 10, 0, 0);

            }else{
/**
                ServerLevel sLevel = (ServerLevel) level;
                sLevel.sendParticles(ParticleTypes.SWEEP_ATTACK,player.position().x,player.position().y + 1,player.position().z,
                        10,3,3,3,0);
 **/
        }


        return super.use(level, player, hand);
    }

}
