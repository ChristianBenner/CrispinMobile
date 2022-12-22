#version 300 es
layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec2 aTextureCoordinates;
layout (location = 2) in mat4 aModel;

uniform mat4 uProjection;
uniform mat4 uView;

out vec2 vTextureCoordinates;

void main()
{
    vTextureCoordinates = vec2(aTextureCoordinates.s, -aTextureCoordinates.t);
    gl_Position = uProjection * uView * aModel * vec4(aPosition, 1.0f);
}