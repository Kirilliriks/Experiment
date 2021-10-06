#version 440 core
attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;
out vec4 v_color;
out vec2 v_texCoords;
out float texMap[90];

void main()
{
   float[90] arr;
   for (int i = 0; i < 90; i++) {
      arr[i] = 1.0f / i;
   }
   texMap = arr;
   v_color = a_color;
   v_color.a = v_color.a * (255.0/254.0);
   v_texCoords = a_texCoord0;
   gl_Position =  u_projTrans * a_position;
}