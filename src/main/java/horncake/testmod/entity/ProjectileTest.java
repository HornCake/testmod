package horncake.testmod.entity;

import horncake.testmod.client.particle.ParticleTestOptions;
import horncake.testmod.util.ColorHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jline.utils.Log;

public class ProjectileTest extends Projectile {
    public int age;
    private Vec3 currentPos;
    private Vec3 nextPos;

    //TODO nbtデータを{Color:{R:120,G:90,B:30}}のようにする
    private static final EntityDataAccessor<Integer> DATA_RED = SynchedEntityData.defineId(ProjectileTest.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_GREEN = SynchedEntityData.defineId(ProjectileTest.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_BLUE = SynchedEntityData.defineId(ProjectileTest.class, EntityDataSerializers.INT);

    public ProjectileTest(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_RED, 255);
        this.getEntityData().define(DATA_GREEN, 255);
        this.getEntityData().define(DATA_BLUE, 255);
    }

    @Override
    public void load(CompoundTag pCompound) {
        super.load(pCompound);
        this.getEntityData().set(DATA_RED, pCompound.getInt("Red"));
        this.getEntityData().set(DATA_GREEN, pCompound.getInt("Green"));
        this.getEntityData().set(DATA_BLUE, pCompound.getInt("Blue"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Red", this.getEntityData().get(DATA_RED));
        pCompound.putInt("Green", this.getEntityData().get(DATA_GREEN));
        pCompound.putInt("Blue", this.getEntityData().get(DATA_BLUE));
    }

    public void shoot(Vec3 pos, Vec3 direction, float velocity, float inaccuracy, Entity entity) {
        Vec3 vec3 = direction.normalize().scale(velocity);
        this.setOwner(entity);
        this.setPos(pos.x,pos.y + entity.getEyeHeight(), pos.z);
        this.setDeltaMovement(vec3);
        //super.shoot(vec3.x, vec3.y, vec3.z, velocity, inaccuracy);
    }

    public void setColor(int r, int g, int b) {
        this.getEntityData().set(DATA_RED, r);
        this.getEntityData().set(DATA_GREEN, g);
        this.getEntityData().set(DATA_BLUE, b);
    }

    public void setColor(ColorHandler color) {
        this.setColor(color.getRInt(), color.getGInt(), color.getBInt());
    }

    @Override
    protected void onHit(HitResult result) {
        EntityHitResult entityhitresult = this.findHitEntity(currentPos,nextPos);
        //Minecraft.getInstance().player.sendSystemMessage(Component.literal(level.isClientSide+"CLIENT"));

        if(entityhitresult != null){
            onHitEntity(entityhitresult);
        }
        super.onHit(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        discardWithParticles();
        //super.onHitBlock(result);

    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (getOwner() != null && result.getEntity().getUUID() != getOwner().getUUID()) {
            result.getEntity().hurt(DamageSource.MAGIC,4.0f);
            discardWithParticles();
        }
        //super.onHitEntity(result);
    }

    private void discardWithParticles() {
        ColorHandler color = new ColorHandler(this.getEntityData().get(DATA_RED), this.getEntityData().get(DATA_GREEN), this.getEntityData().get(DATA_BLUE));
        if(!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;
            serverLevel.sendParticles(new ParticleTestOptions(color,0.4f,10, 1.0f)
                    , position().x,position().y, position().z,10, 0,0,0,0.15);
        }
        //Minecraft.getInstance().player.sendSystemMessage(Component.literal(level.isClientSide+"CLIENT BB"));
        //playParticleVanished();
        this.discard();
    }

    @Override
    public void tick() {
        age++;
        if(age > 60) { this.discard(); }

        //Minecraft.getInstance().player.sendSystemMessage(Component.literal(level.isClientSide+"CLIENT TT"));

        /*
        HitResult result = ProjectileUtil.getHitResult(this,this::canHitEntity);
        Minecraft.getInstance().player.sendSystemMessage(Component.literal(result.getType().toString() + level.isClientSide));
        if(result.getType() == HitResult.Type.BLOCK) this.discardWithParticles();

         */
        setPositions();

        if(getHitResult() != null) { this.onHit(getHitResult()); }

        setMovement();

        if(level.isClientSide) { playParticle(); }

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
        this.setPos(d0, d1, d2);
    }

    public HitResult getHitResult() {
        return this.level.clip(new ClipContext(currentPos, nextPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
    }

    public EntityHitResult findHitEntity(Vec3 pos1, Vec3 pos2) {
        return ProjectileUtil.getEntityHitResult(this.level, this, pos1, pos2, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    private void playParticle() {
        double x = this.currentPos.x;
        double y = this.currentPos.y;
        double z = this.currentPos.z;

        ColorHandler color = new ColorHandler(this.getEntityData().get(DATA_RED), this.getEntityData().get(DATA_GREEN), this.getEntityData().get(DATA_BLUE));


        double velocity = getDeltaMovement().length();
        Vec3 direction = getDeltaMovement();

        double count = (int)(velocity * 4);
        for (int i = 0; i <= count; i++) {

            //COLOR:0.05,0.5,0.65
            level.addParticle(new ParticleTestOptions(color,0.4f,10, 1.0f), true,
                    x + (direction.x * (i / count)),
                    y + (direction.y * (i / count)),
                    z + (direction.z * (i / count)),
                    0.1 * (random.nextDouble() - 0.5),
                    0.1 * (random.nextDouble() - 0.5) + 0.03,
                    0.1 * (random.nextDouble() - 0.5));

        }
    }

    private void playParticleVanished() {
        double x = this.position().x;
        double y = this.position().y;
        double z = this.position().z;

        int count = random.nextInt(10) + 10;

        for (int i = 0; i < count; i++) {
            level.addParticle(new ParticleTestOptions(0.05f,0.5f,0.65f,0.4f,10, 1.0f), true,
                    x, y, z,
                    1 * (random.nextDouble() - 0.5),
                    1 * (random.nextDouble() - 0.5) + 0.03,
                    1 * (random.nextDouble() - 0.5));
        }
    }

}
