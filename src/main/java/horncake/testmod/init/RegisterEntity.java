package horncake.testmod.init;

import horncake.testmod.TestMod;
import horncake.testmod.entity.ProjectileStraight;
import horncake.testmod.entity.ProjectileBound;
import horncake.testmod.entity.ProjectileTest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterEntity {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TestMod.MODID);

    public static final RegistryObject<EntityType<ProjectileTest>> PROJECTILE_STRAIGHT = ENTITY_TYPES.register("projectile_straight",
            () -> EntityType.Builder.of((EntityType.EntityFactory<ProjectileTest>) ProjectileStraight::new, MobCategory.MISC)
                    .sized(0.8f,0.8f).build("projectile_straight"));
    public static final RegistryObject<EntityType<ProjectileTest>> PROJECTILE_BOUND = ENTITY_TYPES.register("projectile_bound",
            () -> EntityType.Builder.of((EntityType.EntityFactory<ProjectileTest>) ProjectileBound::new, MobCategory.MISC)
                    .sized(0.8f,0.8f).build("projectile_bound"));
}
