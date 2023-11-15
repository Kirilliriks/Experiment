package org.anotherteam.core;

public interface Core {

    void prepare();
    void update(float dt);
    void render(float dt);
    void destroy();
    boolean needClose();

}
