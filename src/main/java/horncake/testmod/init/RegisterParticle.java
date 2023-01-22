package horncake.testmod.init;

import horncake.testmod.TestMod;
import horncake.testmod.client.particle.ParticleTestFactory;
import horncake.testmod.client.particle.ParticleTestOptions;
import horncake.testmod.client.particle.ParticleTestType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
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

    public static final RegistryObject<ParticleType<ParticleTestOptions>> TEST_PARTICLE = PARTICLES.register("test_particle",() -> new ParticleTestType());

    @SubscribeEvent
    public static void registerParticle(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(TEST_PARTICLE.get(), ParticleTestFactory::new);
    }
}
