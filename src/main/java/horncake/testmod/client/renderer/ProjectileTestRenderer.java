package horncake.testmod.client.renderer;

import horncake.testmod.entity.ProjectileTest;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ProjectileTestRenderer extends EntityRenderer<ProjectileTest> {
    //public static final ResourceLocation TEXTURE = new ResourceLocation(TestMod.MODID, "textures/item/fire.png");

    public ProjectileTestRenderer(EntityRendererProvider.Context renderer) {
        super(renderer);
    }

    @Override
    public ResourceLocation getTextureLocation(ProjectileTest entity) {
        return null;
    }
}
