package org.anotherteam.render.frame;

import org.anotherteam.object.type.entity.manager.EntityManager;
import org.anotherteam.render.GameRender;
import org.anotherteam.render.RenderBatch;
import org.jetbrains.annotations.NotNull;

public final class LightFrame extends AbstractFrame {

    @NotNull
    private final HeightFrame heightFrame;

    public LightFrame(@NotNull GameRender gameRender, @NotNull RenderBatch renderBatch) {
        super(gameRender, renderBatch);
        heightFrame = gameRender.heightFrame;
    }

    public void generateLightMap() {
        if (EntityManager.player == null) return;

        /*pixmap.fill();

        val playerPosition = EntityManager.player.getPosition().toVector2().cpy().add(0, 15);

        val color = new Color();
        val directionVector = new Vector2();
        val rayVector = new Vector2();

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
                val index = (x + y * GameScreen.SCREEN_WIDTH) * 4;
                if (index >= buffer.limit() || index < 0) break;
                if (x < 0 || x >= GameScreen.SCREEN_WIDTH || y < 0 || y >= GameScreen.SCREEN_HEIGHT) break;

                Color.rgba8888ToColor(color, heightFrame.buffer.getInt(index));
                power -= color.r;
                if (power <= 0.0f) break;

                buffer.putInt(index,
                        Color.rgba8888(
                                power,
                                power,
                                power,
                                1.0f));
            }
        }
        texture.draw(pixmap, 0, 0);*/
    }

    public void testShader() {
        /*val color = new Color();
        pixmap.fill();
        Vector2 startPosition = EntityManager.player.getPosition().toVector2().cpy().add(0, 15);
        for (int y = 0; y < 90; y++) {
            for (int x = 0; x < 160; x++) {
                Vector2i endPosition = new Vector2i(x, y);

                // Float;
                Vector2 directionVector, rayVector;
                Vector2 rayCoord = new Vector2();
                //

                //Integer
                Vector2 integerRayVector = new Vector2();
                //

                directionVector = new Vector2(endPosition.x - startPosition.x, endPosition.y - startPosition.y);
                directionVector = directionVector.nor();
                rayVector = startPosition.cpy();

                float power = 1.0f;
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
                    val anotherIndex = (xIA + yIA * GameScreen.SCREEN_WIDTH) * 4;
                    Color.rgba8888ToColor(color, heightFrame.buffer.getInt(anotherIndex));
                    power -= color.r;
                    if (color.r > 0.0f) break;

                    if (integerRayVector.x == endPosition.x &&
                            (integerRayVector.y == endPosition.y || integerRayVector.y == endPosition.y * -1)) {
                        val xI = endPosition.x;
                        val yI = endPosition.y;
                        val index = (xI + yI * GameScreen.SCREEN_WIDTH) * 4;
                        if (index >= buffer.limit() || index < 0) break;
                        if (xI < 0 || xI >= GameScreen.SCREEN_WIDTH || yI < 0 || yI >= GameScreen.SCREEN_HEIGHT) break;
                        buffer.putInt(index,
                                Color.rgba8888(
                                        1.0f,
                                        1.0f,
                                        1.0f,
                                        1.0f));
                        break;
                    }
                    power -= 0.001;
                    if (power <= 0.0) {
                        break;
                    }
                }
            }
        }
        texture.draw(pixmap, 0, 0);*/
    }
}
