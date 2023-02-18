#version 300 es

layout (location = 0) in vec4 aPosition;
layout (location = 1) in mat4 aModel;

uniform mat4 uProjection;
uniform mat4 uView;

out vec3 vFragPos;

void main()
{
    vFragPos = vec3(aModel * aPosition);
    gl_Position = uProjection * uView * vec4(vFragPos, 1.0);
}