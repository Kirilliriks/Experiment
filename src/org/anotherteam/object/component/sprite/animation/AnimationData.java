package org.anotherteam.object.component.sprite.animation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public final class AnimationData {

    int yTexture;
    float animationSpeed;
    int startFrame;
    int endFrame;
    boolean cancelOnEnd;

    public int getYTexture() {
        return yTexture;
    }

    public float getAnimSpeed() {
        return animationSpeed;
    }

    public int getStartFrame() {
        return startFrame;
    }

    public int getEndFrame() {
        return endFrame;
    }

    public boolean isCancelOnEnd() {
        return cancelOnEnd;
    }
}
