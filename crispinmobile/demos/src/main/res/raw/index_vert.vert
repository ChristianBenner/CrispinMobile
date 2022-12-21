#version 300 es
layout (location = 0) in vec3 aPosition;
layout (location = 1) in mat4 aModel;

uniform mat4 uProjection;
uniform mat4 uView;
uniform mat4 uModel;

void main()
{
    gl_Position = uProjection * uView * aModel * vec4(aPosition, 1.0f);
}