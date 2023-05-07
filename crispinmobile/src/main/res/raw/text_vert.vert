#version 300 es

layout (location = 0) in vec4 aPosition;
layout (location = 2) in vec2 aTextureCoordinates;

uniform mat4 uMatrix;

out vec2 vTextureCoordinates;

void main()
{
    vTextureCoordinates = aTextureCoordinates;
    gl_Position = uMatrix * aPosition;
}