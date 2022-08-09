#version 460 core
in vec3 inColor;

out vec4 out_Color;

void main() {
    out_Color = vec4(inColor, 1.0);
}