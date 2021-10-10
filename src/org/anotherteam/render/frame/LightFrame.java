package org.anotherteam.render.frame;

import lombok.val;
import org.anotherteam.object.type.entity.manager.EntityManager;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.RenderBatch;
import org.anotherteam.screen.GameScreen;
import org.anotherteam.util.Color;
import org.anotherteam.util.GameMath;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector2i;

public final class LightFrame extends AbstractFrame {

    @NotNull
    private final HeightFrame heightFrame;

    public LightFrame(@NotNull GameRender gameRender, @NotNull RenderBatch renderBatch) {
        super(gameRender, renderBatch);
        heightFrame = gameRender.heightFrame;
    }

    public void generateLightMap() {
        if (EntityManager.player == null) return;

        pixmap.clear();

        val playerPosition = new Vector2i(EntityManager.player.getPosition().x, EntityManager.player.getPosition().y + 15);

        val color = new Color(0, 0, 0, 0);
        val directionVector = new Vector2f();
        val rayVector = new Vector2f();

        for (short r = 0; r < 720; r++) {
            val rayAngle = r / 2.0f;
            GameMath.rotate(directionVector.set(1.0f, 0), rayAngle);
            rayVector.set(playerPosition.x, playerPosition.y);
            float power = 1.0f;
            for (short i = 0; i < 160; i++) {
                rayVector.add(directionVector);
                power -= 0.001f;

                val x = (int)rayVector.x;
                val y = (int)rayVector.y;
                val index = (x + y * GameScreen.WIDTH) * 4;
                if (index >= buffer.limit() || index < 0) break;
                if (x < 0 || x >= GameScreen.WIDTH || y < 0 || y >= GameScreen.HEIGHT) break;

                Color.toColor(color, heightFrame.buffer.getInt(index));
                power -= color.r;
                if (power <= 0.0f) break;

                buffer.putInt(index,
                        Color.fromRGBA(
                                (int)power * 255,
                                (int)power * 255,
                                (int)power * 255,
                                255));
            }
        }
        texture.drawPixmap(pixmap, 0, 0);
    }

    public void testShader() {
        val color = new Color(0, 0, 0, 0);
        pixmap.clear();

        Vector2i startPosition = new Vector2i(EntityManager.player.getPosition().x, EntityManager.player.getPosition().y + 15);
        for (int y = 0; y < 90; y++) {
            for (int x = 0; x < 160; x++) {
                Vector2i endPosition = new Vector2i(x, y);

                // Float;
                Vector2f directionVector, rayVector;
                Vector2f rayCoord = new Vector2f();
                //

                //Integer
                Vector2f integerRayVector = new Vector2f();
                //

                directionVector = new Vector2f(endPosition.x - startPosition.x, endPosition.y - startPosition.y);
                directionVector = directionVector.normalize();
                rayVector = new Vector2f(startPosition.x, startPosition.y);

                int power = 255;
                for (int i = 0; i < 100; i++) {
                    rayVector.x += directionVector.x;
                    rayVector.y += directionVector.y;

                    //Integer
                    integerRayVector.x = Math.round(rayVector.x);
                    integerRayVector.y = Math.round(rayVector.y);
                    //

                    rayCoord.x = integerRayVector.x / 160.0f;
                    rayCoord.y = integerRayVector.y / 90.0f;

                    val xIA = (int)integerRayVector.x;
                    val yIA = (int)integerRayVector.y;
                    val anotherIndex = (xIA + yIA * GameScreen.WIDTH) * 4;
                    Color.toColor(color, heightFrame.buffer.getInt(anotherIndex));
                    power -= color.g;
                    if (color.g > 0.0f) break;

                    if (integerRayVector.x == endPosition.x &&
                            (integerRayVector.y == endPosition.y || integerRayVector.y == endPosition.y * -1)) {
                        val xI = endPosition.x;
                        val yI = endPosition.y;
                        val index = (xI + yI * GameScreen.WIDTH) * 4;
                        if (index >= buffer.limit() || index < 0) break;
                        if (xI < 0 || xI >= GameScreen.WIDTH || yI < 0 || yI >= GameScreen.HEIGHT) break;
                        buffer.putInt(index,
                                Color.fromRGBA(
                                        255,
                                        255,
                                        255,
                                        255));
                        break;
                    }
                    power -= 0.001;
                    if (power <= 0.0) {
                        break;
                    }
                }
            }
        }
        texture.drawPixmap(pixmap, 0, 0);
    }
}
