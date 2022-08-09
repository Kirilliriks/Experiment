package org.anotherteam.object.component.type.sprite.animation;

public record AnimationData(int yTexture, float animationSpeed, int startFrame, int endFrame, boolean cancelOnEnd) {

    public int getFramePosY() {
        return yTexture;
    }

    public float getAnimSpeed() {
        return animationSpeed;
    }
}
