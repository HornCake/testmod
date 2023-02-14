package horncake.testmod.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import horncake.testmod.init.RegisterParticle;
import horncake.testmod.util.ColorHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Locale;

public class ParticleTestOptions implements ParticleOptions {
    public final float r;
    public final float g;
    public final float b;
    public final float size;
    public final int age;
    public final float alpha;

    public ParticleTestOptions(float r, float g, float b, float size, int age, float alpha) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.size = size;
        this.age = age;
        this.alpha = alpha;
    }
    public ParticleTestOptions(ColorHandler color, float size, int age, float alpha) {
        this(color.getRFloat(), color.getGFloat(), color.getBFloat(), size, age, alpha);
    }
    public static final Codec<ParticleTestOptions> CODEC = RecordCodecBuilder.create(i ->
            i.group(
                    Codec.FLOAT.fieldOf("r").forGetter(p -> p.r),
                    Codec.FLOAT.fieldOf("g").forGetter(p -> p.g),
                    Codec.FLOAT.fieldOf("b").forGetter(p -> p.b),
                    Codec.FLOAT.fieldOf("size").forGetter(p -> p.size),
                    Codec.INT.fieldOf("age").forGetter(p -> p.age),
                    Codec.FLOAT.fieldOf("alpha").forGetter(p -> p.alpha))
            .apply(i, ParticleTestOptions::new));

    static final ParticleOptions.Deserializer<ParticleTestOptions> DESERIALIZER = new ParticleOptions.Deserializer<>() {

        @Override
        public ParticleTestOptions fromCommand(ParticleType<ParticleTestOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            reader.expect(' ');
            float size = reader.readFloat();
            reader.expect(' ');
            int age = reader.readInt();
            reader.expect(' ');
            float alpha = reader.readFloat();
            return new ParticleTestOptions(r,g,b,size,age,alpha);
        }

        @Override
        public ParticleTestOptions fromNetwork(ParticleType<ParticleTestOptions> type, FriendlyByteBuf buf) {
            return new ParticleTestOptions(buf.readFloat(),buf.readFloat(),buf.readFloat(),buf.readFloat(),buf.readInt(),buf.readFloat());
        }
    };

    @Override
    public ParticleType<?> getType() {
        return RegisterParticle.TEST_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(this.r);
        buf.writeFloat(this.g);
        buf.writeFloat(this.b);
        buf.writeFloat(this.size);
        buf.writeInt(this.age);
        buf.writeFloat(this.alpha);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %f %f %f %f %d %f", Registry.PARTICLE_TYPE.getKey(this.getType()),
                this.r, this.g, this.b, this.size, this.age, this.alpha);
    }

}
