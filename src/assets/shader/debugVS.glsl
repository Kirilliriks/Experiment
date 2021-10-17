#version 460 core
in vec2 pos;
in vec3 color;

uniform mat4 projection;
uniform mat4 view;

out vec3 inColor;

void main() {
    inColor = color;
    gl_Position = projection * view * vec4(pos, 0.0, 1.0);
}
