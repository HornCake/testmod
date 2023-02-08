package horncake.testmod.util;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Animation<T> {
    private final int length;
    private final T initObject;

    private int frame;
    private boolean isPlaying;
    private boolean isLoop;
    private T object;
    private AnimationFunction<Integer, Integer, T, T> animation;

    public Animation(int length, T object, AnimationFunction<Integer, Integer, T, T> animation) {
        this(length, object, false, animation);
    }

    public Animation(int length, T object, boolean isLoop,  AnimationFunction<Integer, Integer, T, T> animation) {
        this.length = length;
        this.object = object;
        this.initObject = object;
        this.isLoop = isLoop;
        this.animation = animation;
        this.frame = 0;
        this.isPlaying = true;
    }

    public void tick() {
        if(!this.isPlaying) return;
        if(this.frame <= this.length) {
            this.object = this.animation.apply(this.length, this.frame, this.object);
            this.frame = isLoop ? Math.floorMod(this.frame + 1, length) : this.frame + 1;
        } else {
            this.isPlaying = false;
        }
    }

    public void start() {
        this.isPlaying = true;
        this.frame = 0;
        this.object = this.initObject;
    }

    public void stop() {
        this.isPlaying = false;
    }

    public void resume() {
        this.isPlaying = true;
    }

    public T get() {
        return this.object;
    }

    private interface AnimationFunction<T, U, V, R> {
        R apply(T length, U frame, V object);
    }
}
