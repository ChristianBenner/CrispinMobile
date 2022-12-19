#version 300 es
layout (location = 0) in vec3 aPosition;
uniform mat4 uMatrix;

void main()
{
    gl_Position = uMatrix * vec4(aPosition, 1.0f);
}