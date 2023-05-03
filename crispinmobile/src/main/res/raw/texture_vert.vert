#version 300 es

uniform mat4 uMatrix;
uniform vec2 uUvMultiplier;
uniform vec2 uUVOffset;

layout (location = 0) in vec4 aPosition;
layout (location = 1) in vec2 aTextureCoordinates;

out vec2 vTextureCoordinates;

void main()
{
    vTextureCoordinates = uUVOffset + (uUvMultiplier * vec2(aTextureCoordinates.s, -aTextureCoordinates.t));
    gl_Position = uMatrix * aPosition;
}