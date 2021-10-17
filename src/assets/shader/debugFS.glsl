#version 460 core
in vec3 inColor;

void main() {
    gl_FragColor = vec4(inColor, 1);
}