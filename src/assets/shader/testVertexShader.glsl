#version 420 core

layout(location = 0) in vec2 vertex_pos;
layout(location = 1) in vec2 in_tex_coord;
out vec2 tex_coord;

void main() {
    tex_coord = in_tex_coord;
    gl_Position = vec4(vertex_pos, 0, 1);
}