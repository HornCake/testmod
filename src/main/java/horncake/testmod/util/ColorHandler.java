package horncake.testmod.util;

public class ColorHandler {
    private int r;
    private int g;
    private int b;

    public ColorHandler(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }


    public ColorHandler(float r, float g, float b) {
        this((int)(r * 255), (int)(g * 255), (int)(b * 255));
    }

    public ColorHandler multiply(float r, float g, float b) {
        int r2 = this.r <= 255 ? ( this.r >= 0 ? (int) (this.r * r) : 0) : 255;
        int g2 = this.g <= 255 ? ( this.g >= 0 ? (int) (this.g * g) : 0) : 255;
        int b2 = this.b <= 255 ? ( this.b >= 0 ? (int) (this.b * b) : 0) : 255;
        return new ColorHandler(r2,g2,b2);
    }

    public ColorHandler scale(float size) {
        return new ColorHandler(this.r,this.g,this.b).multiply(size,size,size);
    }

    public static float int2Float(int color) {
        return color <= 255 ? ( color >= 0 ? (float) (color / 255) : 0f) : 1f;
    }
    public static int float2Int(float color) {
        return color <= 1 ? ( color >= 0 ? (int) (color * 255) : 0) : 255;
    }

    public int getRInt() {
        return r;
    }
    public int getGInt() {
        return g;
    }
    public int getBInt() {
        return b;
    }

    public float getRFloat() {
        return (float) r / 255;
    }
    public float getGFloat() {
        return (float) g / 255;
    }
    public float getBFloat() {
        return (float) b / 255;
    }
}
