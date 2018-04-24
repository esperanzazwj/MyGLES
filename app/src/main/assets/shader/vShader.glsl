#version 300 es

layout(location=0) in vec3 Position_VS_in;
layout(location=1) in vec2 TexCoord_VS_in;
layout(location=2) in vec3 Normal_VS_in;

out vec3 World_FS_in;
out vec2 TexCoord_FS_in;
out vec3 Normal_FS_in;

uniform mat4 gWorld;
uniform mat4 gViewProjMatrix;

void main()
{
    World_FS_in =  (gWorld * vec4(Position_VS_in,1.0)).xyz;
    TexCoord_FS_in = TexCoord_VS_in;
    Normal_FS_in= normalize(gWorld * vec4(Normal_VS_in,0.0)).xyz;
    gl_Position = gViewProjMatrix * gWorld * vec4(Position_VS_in,1.0);
}