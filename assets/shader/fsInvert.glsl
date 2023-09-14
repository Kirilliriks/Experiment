#version 460 core

in vec2 tex_coord;
in vec2 tex_positions;

uniform vec2 player_pos;

uniform sampler2D u_texture;
layout (binding = 1, rgba8) uniform readonly image2D u_texture1;

out vec4 out_Color;

void main() {

    // TODO refactor

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

    float lastPower = 1.0f;
    float nextPower = 1.0f; //
    float hidePower = 1.0f; // if zero pixel not visible, else visible

    color.a = 0.0f;
    for (int i = 0; i < 100; i++) {
        if(startPosition == endPosition) {
            color.a = 1.0f;
            break;
        }
        rayVector.x += directionVector.x;
        rayVector.y += directionVector.y;

        //Integer ray vector
        integerRayVector.x = int(round(rayVector.x));
        integerRayVector.y = int(round(rayVector.y));
        //

        nextPower -= 0.01f;
        hidePower -= 0.01f;

        vec4 heightColor = imageLoad(u_texture1, integerRayVector);
        float height = heightColor.g;
        if (height != 0.0) {
            if (height == 1.0f)
            hidePower = 0.0f;
            nextPower -= height / 20.0f;
        }
        if (hidePower <= 0 || nextPower <= 0) {
            break;
        }
        if (endPosition == integerRayVector) {
            color.r *= lastPower;
            color.g *= lastPower;
            color.b *= lastPower;
            color.a = 1.0f;
            break;
        }
        lastPower = nextPower;
    }
    out_Color = color;
}