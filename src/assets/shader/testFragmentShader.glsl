#version 420 core

in vec2 tex_coord;
uniform sampler2D sampler;

void main() {
    gl_FragColor = texture2D(sampler, tex_coord);
}