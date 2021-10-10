#version 460 core
in vec2 tex_coord;
uniform sampler2D u_texture;
uniform sampler2D u_texture1;
uniform vec2 player_pos;

void main() {
    // constants
    vec2 endPosition;
    vec2 startPosition;

    endPosition.x = round(tex_coord.x * 160.0);
    endPosition.y = round(tex_coord.y * 90.0);

    startPosition.x = round(player_pos.x);
    startPosition.y = round(player_pos.y);
    //
    vec4 color = texture2D(u_texture, tex_coord);
    vec4 height = texture2D(u_texture1, tex_coord);


    // Float;
    vec2 directionVector, rayVector;
    vec2 rayCoord;
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

        rayCoord.x = integerRayVector.x / 160;
        rayCoord.y = integerRayVector.y / 90;

        power -= 0.01;
        hidePower -= 0.01;

        float currentHeight = texture2D(u_texture1, rayCoord).g;
        if (currentHeight != 0.0) {
            if (lastHeight != currentHeight) {
                lastHeight = currentHeight;
                hidePower -= lastHeight;
            } else {
                hidePower -= lastHeight / 10.0;
                power -= lastHeight / 10.0;
            }
        }
        if (hidePower <= 0.0) {
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
    vec4 suka = vec4(color.a, color.g, color.b, 1);
    gl_FragColor = color;
}