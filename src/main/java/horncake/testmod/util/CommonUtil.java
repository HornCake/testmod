package horncake.testmod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class CommonUtil {
    public static Vec3 getVec3(BlockPos pos) {
        return new Vec3(pos.getX(),pos.getY(),pos.getZ());
    }

    public static float getArg(float x, float y) {
        double distance = new Vec2(x,y).length();
        return (float) (Math.asin(y / distance) > 0 ? Math.acos(x / distance) : 2 * Math.PI - Math.acos(x / distance));
    }

    public static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloat(String string) {
        try {
            Float.parseFloat(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
