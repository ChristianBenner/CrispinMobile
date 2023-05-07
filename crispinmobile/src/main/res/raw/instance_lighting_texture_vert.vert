#version 300 es

layout (location = 0) in vec4 aPosition;
layout (location = 1) in vec3 aNormal;
layout (location = 3) in vec2 aTextureCoordinates;
layout (location = 4) in mat4 aModel;

uniform mat4 uProjection;
uniform mat4 uView;
uniform vec2 uUvMultiplier;

out vec3 vFragPos;
out vec3 vNormal;
out vec2 vTextureCoordinates;

void main()
{
    vFragPos = vec3(aModel * aPosition);
    vNormal = mat3(transpose(inverse(aModel))) * aNormal;
    vTextureCoordinates = uUvMultiplier * vec2(aTextureCoordinates.s, -aTextureCoordinates.t);
    gl_Position = uProjection * uView * vec4(vFragPos, 1.0);
}