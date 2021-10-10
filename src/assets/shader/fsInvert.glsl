#version 460 core
in vec2 tex_coord;
in vec2 tex_positions;
uniform sampler2D u_texture;
layout (binding = 1, rgba8ui) uniform readonly uimage2D u_texture1;
uniform vec2 player_pos;

void main() {
    // constants
    vec2 endPosition;
    vec2 startPosition;

    endPosition.x = round(tex_positions.x);
    endPosition.y = round(tex_positions.y);

    startPosition.x = round(player_pos.x);
    startPosition.y = round(player_pos.y);
    //
    vec4 color = texture2D(u_texture, tex_coord);

    // Float;
    vec2 directionVector, rayVector;
    ivec2 rayCoord;
    //

    //Integer
    vec2 integerRayVector;
    //

    directionVector = endPosition - startPosition;
    directionVector = normalize(directionVector);
    rayVector = startPosition;

    float power = 1.0;
    float hidePower = 1.0;
    float lastHeight = 0.0;
    color.a = 0.0;
    for (int i = 0; i < 100; i++) {
        rayVector.x += directionVector.x;
        rayVector.y += directionVector.y;

        //Integer
        integerRayVector.x = round(rayVector.x);
        integerRayVector.y = round(rayVector.y);
        //

        rayCoord.x = int(integerRayVector.x);
        rayCoord.y = int(integerRayVector.y);

        power -= 0.01;
        hidePower -= 0.01;

        vec4 currentColor = imageLoad(u_texture1, ivec2(rayCoord));
        if (currentColor.g != 0 && rayCoord == endPosition)
            color = currentColor;
        float currentHeight = currentColor.g;
        if (currentHeight != 0.0) {
            if (lastHeight != currentHeight) {
                lastHeight = currentHeight;
                hidePower -= lastHeight;
            } else {
                hidePower -= lastHeight / 10.0;
                power -= lastHeight / 10.0;
            }
        }
        if (false) {
            break;
        }
        if (endPosition == integerRayVector) {
//            color.r *= power;
//            color.g *= power;
//            color.b *= power;
//            color.a = 1.0;

            break;
        }
    }
    gl_FragColor = color;
}