#version 460 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec3 color;

uniform mat4 projection;
uniform mat4 view;

out vec3 inColor;

void main() {
    inColor = color;
    gl_Position = projection * view * vec4(position, 0.0, 1.0);
}
