#version 300 es
layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec4 aColour;
layout (location = 2) in mat4 aModel;

out vec4 vColour;

uniform mat4 uProjection;
uniform mat4 uView;

void main()
{
    vColour = aColour;
    gl_Position = uProjection * uView * aModel * vec4(aPosition, 1.0f);
}