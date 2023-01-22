package horncake.testmod.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;

public class ParticleTest extends TextureSheetParticle {
    private float initSize = 0.4f;
    private float initAlpha = 1.0f;
    protected ParticleTest(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xd, double yd, double zd, Vector3f color, float size, int age, float alpha) {
        super(level, x, y, z, xd, yd, zd);

        this.friction = 0.9f;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        initSize = size;
        this.quadSize = size;
        this.lifetime = age;
        this.alpha = alpha;
        initAlpha = alpha;
        this.pickSprite(spriteSet);

        this.setColor(color.x(),color.y(),color.z());
    }

    @Override
    public void tick() {
        super.tick();
        quadSize = initSize * (1f - ((float)age / (float)lifetime));
        alpha = initAlpha * (1f - ((float)age / (float)lifetime));
    }


    @Override
    public ParticleRenderType getRenderType() {
        return CUSTOM_RENDER;
    }

    static final ParticleRenderType CUSTOM_RENDER = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder buffer, TextureManager manager) {
            RenderSystem.depthMask(false); //alpha < 1のピクセルの後ろにあるパーティクルがtrueで消える(デフォルトはtrue)
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend(); //alphaを使うか
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            /*
            SRC->Source, DST/Dest->Destination ?

            SRC_ALPHAとのとき
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA -> 色を重ねない?αだけ埋めてる感じ
            GlStateManager.DestFactor.ONE -> 発光みたいに重なると白に近づく
            GlStateManager.DestFactor.CONSTANT_ALPHA -> αの代わりに黒

            とりあえずSRC_ALPHAとONEがいい
             */
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
        }

        public String toString() {
            return "CUSTOM_RENDER";
        }

    };
}
