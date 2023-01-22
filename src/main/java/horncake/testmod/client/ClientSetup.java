package horncake.testmod.client;

import horncake.testmod.TestMod;
import horncake.testmod.client.gui.ScreenMagicTable;
import horncake.testmod.client.renderer.ProjectileTestRenderer;
import horncake.testmod.init.RegisterEntity;
import horncake.testmod.init.RegisterMenuType;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TestMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void registerEntity(FMLClientSetupEvent event) {
        EntityRenderers.register(RegisterEntity.PROJECTILE_STRAIGHT.get(), ProjectileTestRenderer::new);
        EntityRenderers.register(RegisterEntity.PROJECTILE_BOUND.get(), ProjectileTestRenderer::new);
        event.enqueueWork(() -> MenuScreens.register(RegisterMenuType.MENU_MAGIC_TABLE.get(), ScreenMagicTable::new));
    }

}
