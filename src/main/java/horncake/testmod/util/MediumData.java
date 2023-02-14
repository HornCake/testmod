package horncake.testmod.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MediumData {
    private ProjectileType type;
    private int count;
    private int range;
    private float velocity;
    private ColorHandler color;

    public static final int DATA_AMOUNT = 5;

    public static final String TYPE = "Type";
    public static final String COUNT = "Count";
    public static final String RANGE = "Range";
    public static final String VELOCITY = "Velocity";
    public static final String COLOR = "Color";
    public static final String R = "R";
    public static final String G = "G";
    public static final String B = "B";

    public static final List<String> NAME_LIST1 = List.of(TYPE,COUNT,RANGE,VELOCITY,COLOR);
    public static final List<String> NAME_LIST2 = List.of(TYPE,COUNT,RANGE,VELOCITY,R,G,B);


    public MediumData(ProjectileType type, int count, int range, float velocity, ColorHandler color) {
        this.type = type;
        this.count = count;
        this.range = range;
        this.velocity = velocity;
        this.color = color;
    }
    public MediumData(CompoundTag tag) {
        this(tag.contains(TYPE, Tag.TAG_INT) ? ProjectileType.getType(tag.getInt(TYPE)) : ProjectileType.BASIC,
                tag.contains(COUNT, Tag.TAG_INT) ? tag.getInt(COUNT) : 1,
                tag.contains(RANGE, Tag.TAG_INT) ? tag.getInt(RANGE) : 0,
                tag.contains(VELOCITY, Tag.TAG_FLOAT) ? tag.getFloat(VELOCITY) : 1f,
                getColor(tag));
    }

    private static ColorHandler getColor(CompoundTag tag) {
        if(tag.contains(COLOR,Tag.TAG_COMPOUND)) {
            CompoundTag tag2 = tag.getCompound(COLOR);
            return new ColorHandler(tag2.contains(R, Tag.TAG_INT) ? tag2.getInt(R) : 255,
                    tag2.contains(G,Tag.TAG_INT) ? tag2.getInt(G) : 255,
                    tag2.contains(B,Tag.TAG_INT) ? tag2.getInt(B) : 255);
        }
        return new ColorHandler(1f,1f,1f);
    }

    public CompoundTag createNBT(CompoundTag tag) {
        //CompoundTag tag = new CompoundTag();
        tag.putInt(TYPE,this.type.getId());
        tag.putInt(COUNT, this.count);
        tag.putInt(RANGE, this.range);
        tag.putFloat(VELOCITY, this.velocity);
        CompoundTag colorTag = new CompoundTag();
        colorTag.putInt(R, this.color.getRInt());
        colorTag.putInt(G, this.color.getGInt());
        colorTag.putInt(B, this.color.getBInt());
        tag.put(COLOR,colorTag);
        return tag;
    }


    public void setType(ProjectileType type) {
        this.type = type;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public void setRange(int range) {
        this.range = range;
    }
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
    public void setColor(ColorHandler color) {
        this.color = color;
    }
    public void setR(int r) {
        this.setColor(new ColorHandler(r,this.color.getGInt(),this.color.getBInt()));
    }
    public void setG(int g) {
        this.setColor(new ColorHandler(this.color.getRInt(), g,this.color.getBInt()));
    }
    public void setB(int b) {
        this.setColor(new ColorHandler(this.color.getRInt(),this.color.getGInt(), b));
    }


    public ProjectileType getType() {
        return type;
    }
    public int getCount() {
        return count;
    }
    public int getRange() {
        return range;
    }
    public float getVelocity() {
        return velocity;
    }
    public ColorHandler getColor() {
        return color;
    }
}
