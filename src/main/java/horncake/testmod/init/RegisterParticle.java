package horncake.testmod.init;

import horncake.testmod.TestMod;
import horncake.testmod.client.particle.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = TestMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegisterParticle {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, TestMod.MODID);

    public static final RegistryObject<ParticleType<ParticleTestOptions>> TEST_PARTICLE = PARTICLES.register("test_particle",ParticleTest.Type::new);
    public static final RegistryObject<SimpleParticleType> PLANE_TEST_PARTICLE = PARTICLES.register("plane_test_particle",() -> new SimpleParticleType(true));

    @SubscribeEvent
    public static void registerParticle(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(TEST_PARTICLE.get(), ParticleTest.Provider::new);
        Minecraft.getInstance().particleEngine.register(PLANE_TEST_PARTICLE.get(), ParticlePlaneTest.Provider::new);

    }
}
