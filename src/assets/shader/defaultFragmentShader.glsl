#version 460 core

in vec2 tex_coord;
in vec3 colorRGB;
uniform sampler2D sampler;

out vec4 out_Color;

void main() {
    vec4 color = texture2D(sampler, tex_coord);
    out_Color = vec4(color.rgb * colorRGB, color.a);
}