package horncake.testmod.entity;

import horncake.testmod.init.RegisterEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class ProjectileStraight extends ProjectileTest {

    public ProjectileStraight(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }
    public ProjectileStraight(Level level) {
        super(RegisterEntity.PROJECTILE_STRAIGHT.get(), level);
    }


    /*
    public int age;
    private Vec3 currentPos;
    private Vec3 nextPos;



    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {

    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy, Entity entity) {
        Vec3 vec3 = entity.getLookAngle().scale(velocity);
        this.setOwner(entity);
        level.playSound(null,x, y, z, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS,2f,2f);
        this.setPos(x,y + entity.getEyeHeight(), z);
        this.setDeltaMovement(vec3);
        //super.shoot(vec3.x, vec3.y, vec3.z, velocity, inaccuracy);
    }

    @Override
    protected void onHit(HitResult result) {
        EntityHitResult entityhitresult = this.findHitEntity(currentPos,nextPos);
        if(entityhitresult != null){
            onHitEntity(entityhitresult);
        }
        super.onHit(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        if (getOwner() != null) {
            //getOwner().sendSystemMessage(Component.literal("TEST"));
        }

        if (getOwner() != null && result.getEntity().getUUID() != getOwner().getUUID()) {
            //level.addParticle(ParticleTypes.EXPLOSION,true,position().x,position().y,position().z,0,0,0);
            result.getEntity().hurt(DamageSource.MAGIC,4.0f);
            this.discard();
        }

    }

    @Override
    public void tick() {
        age++;
        if(age > 60) { this.discard(); }


        setPositions();


        if(getHitResult() != null) { this.onHit(getHitResult()); }

        if(level.isClientSide) { playParticle(); }
        setMovement();
        super.tick();
    }

    public void setPositions() {
        this.currentPos = this.position();
        this.nextPos = this.position().add(this.getDeltaMovement());
    }
    public void setMovement() {
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        //if(vec3.length() < 0.01) { vec3 = new Vec3(0,0,0); }
        //this.setDeltaMovement(vec3.scale((double)0.99F));
        this.setPos(d0, d1, d2);
    }

    public HitResult getHitResult() {
        return this.level.clip(new ClipContext(currentPos, nextPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
    }

    public EntityHitResult findHitEntity(Vec3 pos1, Vec3 pos2) {
        return ProjectileUtil.getEntityHitResult(this.level, this, pos1, pos2, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    public void playParticle() {
        double x1 = this.currentPos.x;
        double y1 = this.currentPos.y;
        double z1 = this.currentPos.z;

        double velocity = getDeltaMovement().length();
        Vec3 direction = getDeltaMovement();

        double count = velocity * 4;
        for (int i = 0; i <= count; i++) {


            level.addParticle(ParticleInit.TEST_PARTICLE.get(), true,
                    x1 + (direction.x * (i / count)),
                    y1 + (direction.y * (i / count)),
                    z1 + (direction.z * (i / count)),
                    0.1 * (random.nextDouble() - 0.5),
                    0.1 * (random.nextDouble() - 0.5) + 0.03,
                    0.1 * (random.nextDouble() - 0.5));

        }

    }

     */
}
