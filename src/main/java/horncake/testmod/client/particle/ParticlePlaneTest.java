package horncake.testmod.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class ParticlePlaneTest extends TextureSheetParticle {

    private final Vec3[] PLANE = {new Vec3(-1,-1,0), new Vec3(-1,1,0), new Vec3(1,1,0), new Vec3(1,-1,0)};

    private float initAlpha = 1.0f;
    private float endSize = 3.0f;
    private float rot;
    public ParticlePlaneTest(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet sprite) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.friction = 0.9f;
        this.xd = pXSpeed;
        this.yd = pYSpeed;
        this.zd = pZSpeed;
        this.pickSprite(sprite);
        this.alpha = initAlpha;
        this.lifetime = 20;
        this.quadSize = 0.0f;
        this.rot = new Random().nextFloat(360);

        this.setColor(1.0f,1.0f,1.0f);

    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = initAlpha * (1f - ((float)age / (float)lifetime));
        this.quadSize = endSize * ((float)age / (float)lifetime);
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {

        Vec3 vec3 = pRenderInfo.getPosition();
        float f = (float)(Mth.lerp(pPartialTicks, this.xo, this.x) - vec3.x());
        float f1 = (float)(Mth.lerp(pPartialTicks, this.yo, this.y) - vec3.y());
        float f2 = (float)(Mth.lerp(pPartialTicks, this.zo, this.z) - vec3.z());

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();

        int light = LightTexture.FULL_SKY;

        Quaternion quaternion = new Quaternion(new Vector3f(0,0,1),rot,true);

        for(int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                Vec3 v = PLANE[j].scale(quadSize / 2).multiply(2 * i - 1,1f,1f);
                Vector3f vf = new Vector3f(v);
                vf.transform(quaternion);
                v = new Vec3(vf).add(f, f1, f2);
                //PLANE[j] = PLANE[j].add(f,f1,f2);
                pBuffer.vertex(v.x, v.y, v.z)
                        .uv(j < 2 ? u1 : u0, Math.abs((double) j - 1.5) == 0.5 ? v0 : v1)
                        .color(rCol, gCol, bCol, alpha)
                        .uv2(light)
                        .endVertex();

            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CUSTOM_RENDER;
    }

    private static final ParticleRenderType CUSTOM_RENDER = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {

            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);



            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);

        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
        }

        public String toString() {
            return "CUSTOM_RENDER";
        }

    };

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new ParticlePlaneTest(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.spriteSet);
        }
    }
}
