package horncake.testmod.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class ParticleTestType extends ParticleType<ParticleTestOptions> {
    public ParticleTestType() {
        super(false, ParticleTestOptions.DESERIALIZER);
    }

    @Override
    public Codec<ParticleTestOptions> codec() {
        return ParticleTestOptions.CODEC;
    }
}
