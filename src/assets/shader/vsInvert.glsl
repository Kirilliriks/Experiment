#version 460 core

in vec2 vertices;
in vec2 in_tex_coord;

uniform mat4 real_view;
uniform vec2 player_pos;

uniform mat4 projection;
uniform mat4 view;

out vec2 tex_coord;
out vec2 tex_positions;
out vec4 translated_player_pos;

void main() {
   translated_player_pos = real_view * vec4(player_pos.x, player_pos.y, 0.0, 1.0);
   tex_coord = in_tex_coord;
   tex_positions = vertices.xy;
   gl_Position = projection * view * vec4(tex_positions.xy, 0.0, 1.0);
}