package horncake.testmod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class CommonUtil {
    public static Vec3 getVec3(BlockPos pos) {
        return new Vec3(pos.getX(),pos.getY(),pos.getZ());
    }
}
