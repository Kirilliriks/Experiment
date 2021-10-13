#version 460 core

in vec2 tex_coord;
uniform sampler2D sampler;

void main() {
    vec4 color = texture2D(sampler, tex_coord);
    gl_FragColor = color;
}