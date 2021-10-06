#version 420 core

in vec3 vertices;

void main() {
    gl_Position = vec4(vertices, 1);
}