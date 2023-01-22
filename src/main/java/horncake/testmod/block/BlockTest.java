package horncake.testmod.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BlockTest extends Block {
    public BlockTest() {
        super(Properties.of(Material.STONE)
                .sound(SoundType.AMETHYST)
                .strength(1.5f));
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.isClientSide) {
            player.sendSystemMessage(Component.literal("LL"));
            level.addParticle(ParticleTypes.WITCH,pos.getX() + 0.5,pos.getY() + 1,pos.getZ() + 0.5,100,100,100 );

        }

        if(!level.isClientSide) {
            level.explode(null, pos.getX(),pos.getY() + 1,pos.getZ(), 1.0f,false, Explosion.BlockInteraction.BREAK);
        }
        super.attack(state, level, pos, player);
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Vec3 playerPos = player.position();
        Vec3 vector = new Vec3(playerPos.x-pos.getX(),playerPos.y-pos.getY(),playerPos.y-pos.getY());
        if(level.isClientSide) return super.use(state, level, pos, player, hand, hit);

        player.sendSystemMessage(Component.literal(player.getName().getString()
                + ": " + pos.getX()+ "," + pos.getY() + "," + pos.getZ()
                + "\n" + vector.length() + "\n" + hit.getDirection() + "\n"));
        int height = 1;
        while(height < 100) {
            BlockPos targetPos = new BlockPos(pos.getX(),pos.getY() + height,pos.getZ());
            if ((level.getBlockState(targetPos).getBlock() instanceof AirBlock)) {
                player.moveTo(new Vec3(targetPos.getX() + 0.5,targetPos.getY(),targetPos.getZ() + 0.5));
                break;
            }
            ++height;
        }
        return super.use(state, level, pos, player, hand, hit);
    }
}
