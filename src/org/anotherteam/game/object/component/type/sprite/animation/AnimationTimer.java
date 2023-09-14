package org.anotherteam.game.object.component.type.sprite.animation;

public final class AnimationTimer {

    private final int maxCycles;
    private final float speed;
    private float tick;
    private int cycles;

    private boolean finish;

    public AnimationTimer(float speed, int maxCycles) {
        this.maxCycles = maxCycles;
        this.speed = speed;
        tick = 0;
        cycles = 0;
        finish = false;
    }

    public void cancel() {
        finish = true;
    }

    public boolean tick(float dt) {
        if (finish) return false;

        tick += dt;
        if (tick < speed) return false;
        tick = 0;
        cycles++;
        if (cycles == maxCycles) {
            finish = true;
        }
        return true;
    }

    public boolean isFinish() {
        return finish;
    }
}
