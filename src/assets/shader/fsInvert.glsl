#version 460 core
in vec2 tex_coord;
in vec2 tex_positions;
uniform sampler2D u_texture;
layout (binding = 1, rgba8) uniform readonly image2D u_texture1;
uniform vec2 player_pos;

void main() {
    // constants
    vec2 endPosition;
    vec2 startPosition;

    endPosition.x = int(tex_positions.x);
    endPosition.y = int(tex_positions.y);

    startPosition.x = int(player_pos.x);
    startPosition.y = int(player_pos.y);
    //
    vec4 color = texture2D(u_texture, tex_coord);

    // Float;
    vec2 directionVector, rayVector;

    //Integer
    ivec2 integerRayVector;
    //

    directionVector = normalize(endPosition - startPosition);
    rayVector = startPosition;

    float power = 1.0;
    float hidePower = 1.0;
    float lastHeight = 0.0;
    color.a = 0.0;
    for (int i = 0; i < 100; i++) {
        if(startPosition == endPosition) {
            color.a = 1;
            break;
        }
        rayVector.x += directionVector.x;
        rayVector.y += directionVector.y;

        //Integer ray vector
        integerRayVector.x = int(round(rayVector.x));
        integerRayVector.y = int(round(rayVector.y));
        //

        power -= 0.01;
        hidePower -= 0.01;

        vec4 currentColor = imageLoad(u_texture1, integerRayVector);
        float currentHeight = currentColor.g;
        if (currentHeight != 0.0) {
            if (lastHeight != currentHeight) {
                lastHeight = currentHeight;
                if (currentHeight == 1.0f)
                    hidePower = 0;
            } else {
                power -= lastHeight / 10.0;
            }
        }
        if (hidePower <= 0 || power <= 0) {
            break;
        }
        if (endPosition == integerRayVector) {
            color.r *= power;
            color.g *= power;
            color.b *= power;
            color.a = 1.0;
            break;
        }
    }
    gl_FragColor = color;
}