#version 300 es

precision mediump float;

in vec3 World_FS_in;
in vec2 TexCoord_FS_in;
in vec3 Normal_FS_in;

layout(location=0) out vec4 FragColor;
layout(location=1) out vec4 FragIndex;

const int MAX_NUM_LIGHTS = 10;
uniform int numLights;
uniform vec3 Ka;
uniform vec3 Kd;
uniform vec3 Ks;
uniform float Shininess;
uniform vec3 CameraPos;

uniform vec3 ambientColor;

struct lightProperties
{
    vec3 color;
    vec3 position;
};
uniform lightProperties Lights[MAX_NUM_LIGHTS];
uniform sampler2D tex;

uniform float index;

void main()
{
    vec4 color=texture(tex,TexCoord_FS_in);
    vec3 normal= normalize(Normal_FS_in);
    vec3 intensity = vec3(0,0,0);

    for (int i=0;i<numLights;i++)
    {
        vec3 direction=normalize(Lights[i].position-World_FS_in);

        vec3 ViewDirection = normalize(CameraPos - World_FS_in.xyz);
		vec3 ReflectedLight = normalize(reflect(-direction, normal));

		float diffuseFactor = max(0.0, dot(normal, direction));
		float specularFactor = max(0.0, dot(ViewDirection, ReflectedLight));

        if (diffuseFactor == 0.0 || Shininess == 0.0)
			specularFactor = 0.0;
		else
			specularFactor = pow(specularFactor, Shininess);

        intensity += (Kd * diffuseFactor + Ks * specularFactor) * Lights[i].color;
    }

    intensity+=Ka * ambientColor;

    FragColor = vec4(intensity,1.0) * color;
    FragIndex = vec4(index, index, index, index); //0~1

}