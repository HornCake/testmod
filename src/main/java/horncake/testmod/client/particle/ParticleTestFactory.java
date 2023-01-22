package horncake.testmod.client.particle;

import com.mojang.math.Vector3f;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public class ParticleTestFactory implements ParticleProvider<ParticleTestOptions> {
    private final SpriteSet spriteSet;


    public ParticleTestFactory(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(ParticleTestOptions type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
        return new ParticleTest(level, x, y, z, this.spriteSet, xd, yd, zd, new Vector3f(type.r,type.g,type.b), type.size, type.age, type.alpha);
    }

}
