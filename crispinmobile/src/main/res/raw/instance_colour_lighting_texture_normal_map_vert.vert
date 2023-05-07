#version 300 es
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define MAX_NUM_POINT_LIGHTS 10

layout (location = 0) in vec4 aPosition;
layout (location = 1) in vec3 aNormal;
layout (location = 3) in vec2 aTextureCoordinates;
layout (location = 4) in vec3 aTangent;
layout (location = 5) in vec3 aBiTangent;
layout (location = 6) in vec4 aColour;
layout (location = 7) in mat4 aModel;

struct PointLight {
    vec3 position;
    vec3 colour;
    float ambient;
    float diffuse;
    float specular;
    float constant;
    float linear;
    float quadratic;
};

uniform mat4 uProjection;
uniform mat4 uView;
uniform vec2 uUvMultiplier;
uniform vec3 uViewPosition;
uniform int uNumPointLights;
uniform PointLight uPointLights[MAX_NUM_POINT_LIGHTS];

out vec3 vFragPos;
out vec2 vTextureCoordinates;
out vec3 vTangentViewPos;
out vec3 vTangentFragPos;
out vec3 vTangentPointLightPos[MAX_NUM_POINT_LIGHTS];
out vec4 vColour;

void main()
{
    vFragPos = vec3(aModel * aPosition);
    vTextureCoordinates = uUvMultiplier * vec2(aTextureCoordinates.s, aTextureCoordinates.t);

    mat3 normalMatrix = transpose(inverse(mat3(aModel)));
    vec3 tangent = normalize(normalMatrix * aTangent);
    vec3 normal = normalize(normalMatrix * aNormal);
    tangent = normalize(tangent - dot(tangent, normal) * normal);
    vec3 biTangent = cross(normal, tangent);

    mat3 tbn = transpose(mat3(tangent, biTangent, normal));
    vTangentViewPos = tbn * uViewPosition;
    vTangentFragPos = tbn * vFragPos;

    for(int i = 0; i < MAX_NUM_POINT_LIGHTS && i < uNumPointLights; i++) {
        vTangentPointLightPos[i] = tbn * uPointLights[i].position;
    }

    vColour = aColour;
    gl_Position = uProjection * uView * vec4(vFragPos, 1.0);
}