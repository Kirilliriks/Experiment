package org.anotherteam.core;

public interface Core {

    void init();
    void update(float dt);
    void render(float dt);
    void destroy();
    boolean needClose();

}
