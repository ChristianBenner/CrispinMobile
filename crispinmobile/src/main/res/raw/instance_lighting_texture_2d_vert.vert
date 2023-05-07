#version 300 es

layout (location = 0) in vec4 aPosition;
layout (location = 1) in vec2 aTextureCoordinates;
layout (location = 2) in mat4 aModel;

uniform mat4 uProjection;
uniform mat4 uView;
uniform vec2 uUvMultiplier;

out vec3 vFragPos;
out vec2 vTextureCoordinates;

void main()
{
    vFragPos = vec3(aModel * aPosition);
    vTextureCoordinates = uUvMultiplier * vec2(aTextureCoordinates.s, -aTextureCoordinates.t);
    gl_Position = uProjection * uView * vec4(vFragPos, 1.0);
}