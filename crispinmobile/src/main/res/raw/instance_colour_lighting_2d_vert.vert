#version 300 es

layout (location = 0) in vec4 aPosition;
layout (location = 1) in vec4 aColour;
layout (location = 2) in mat4 aModel;

uniform mat4 uProjection;
uniform mat4 uView;

out vec3 vFragPos;
out vec4 vColour;

void main()
{
    vFragPos = vec3(aModel * aPosition);
    vColour = aColour;
    gl_Position = uProjection * uView * vec4(vFragPos, 1.0);
}