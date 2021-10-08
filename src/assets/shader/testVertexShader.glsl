#version 460 core

in vec2 vertex_pos;
in vec2 in_tex_coord;

uniform mat4 model;

out vec2 tex_coord;

void main() {
    tex_coord = in_tex_coord;
    gl_Position = vec4(vertex_pos, 0, 1) * model;
}