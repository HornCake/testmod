package horncake.testmod.util;

import net.minecraft.world.phys.Vec3;

public class LocalVec {
    public final double x;
    public final double y;
    public final double z;
    public final Vec3 base;

    public LocalVec(Vec3 base){
        this(base,new Vec3(0,0,0));
    }

    public LocalVec(Vec3 base, double x, double y, double z){
        this.base = base.normalize();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LocalVec(Vec3 base, Vec3 pos){
        this(base, pos.x, pos.y, pos.z);
    }

    public Vec3 getVec3(){
        if(this.base.equals(new Vec3(0,1,0))) {
            return new Vec3(this.x,this.z,this.y);
        }
        return new Vec3(0,0,0)
                .add(createXAxis(this.base).scale(this.x))
                .add(createYAxis(this.base).scale(this.y))
                .add(this.base.scale(this.z));
    }

    public LocalVec add(Vec3 vec) {
        return new LocalVec(this.base, this.x + vec.x, this.y + vec.y, this.z + vec.z);
    }

    public LocalVec add(double x, double y, double z) {
        return this.add(new Vec3(x,y,z));
    }

    public LocalVec multiply(Vec3 vec) {
        return new LocalVec(this.base, this.x * vec.x, this.y * vec.y, this.z * vec.z);
    }
    public LocalVec multiply(double x, double y, double z) {
        return this.multiply(new Vec3(x,y,z));
    }

    public LocalVec scale(double scale){
        return this.multiply(new Vec3(scale, scale, scale));
    }


    private Vec3 createXAxis(Vec3 vec) {
        return new Vec3(0,1,0).cross(vec).normalize();
    }
    private Vec3 createYAxis(Vec3 vec) {
        return vec.cross(this.createXAxis(vec));
    }

}
