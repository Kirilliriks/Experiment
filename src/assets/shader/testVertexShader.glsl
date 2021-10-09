#version 460 core

in vec2 vertex_pos;
in vec2 in_tex_coord;

uniform mat4 projection;
uniform mat4 view;

out vec2 tex_coord;

void main() {
    tex_coord = in_tex_coord;
    gl_Position = projection * view * vec4(vertex_pos.xy, 0.0, 1.0);
}