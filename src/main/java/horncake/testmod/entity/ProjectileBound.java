package horncake.testmod.entity;

import horncake.testmod.init.RegisterEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ProjectileBound extends ProjectileTest{

    public ProjectileBound(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    public ProjectileBound(Level level) {
        super(RegisterEntity.PROJECTILE_BOUND.get(), level);
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos blockPos = this.blockPosition();
        BlockState blockstate = this.level.getBlockState(blockPos);
        if (!blockstate.isAir()) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.level, blockPos);
            if (!voxelshape.isEmpty()) {
                Vec3 pos = this.position();
                for(AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockPos).contains(pos)) {
                        Vec3 rise = new Vec3(0,1,0);

                        this.setDeltaMovement(getDeltaMovement().add(rise));
                        this.setPos(position().add(rise));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void setMovement() {
        super.setMovement();

        Vec3 gravity = new Vec3(0,0.2,0);
        this.setDeltaMovement(getDeltaMovement().subtract(gravity));
        this.setPos(position().subtract(gravity));
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        Vec3i vec3i = result.getDirection().getNormal();
        Vec3 vec = new Vec3(Math.abs(2 * vec3i.getX()), Math.abs(2 * vec3i.getY()), Math.abs(2 * vec3i.getZ()));
        Vec3 direction = getDeltaMovement().subtract(getDeltaMovement().multiply(vec));
        setDeltaMovement(direction);
    }

    @Override
    protected void onInsideBlock(BlockState state) {
        this.discard();
    }
}
