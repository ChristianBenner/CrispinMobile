#version 300 es
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

layout (location = 0) in vec4 aPosition;
layout (location = 1) in vec2 aTextureCoordinates;
layout (location = 2) in vec4 aColour;
layout (location = 3) in mat4 aModel;

uniform mat4 uProjection;
uniform mat4 uView;
uniform vec2 uUvMultiplier;

out vec3 vFragPos;
out vec2 vTextureCoordinates;
out vec4 vColour;

void main()
{
    vFragPos = vec3(aModel * aPosition);
    vTextureCoordinates = uUvMultiplier * vec2(aTextureCoordinates.s, -aTextureCoordinates.t);
    vColour = aColour;
    gl_Position = uProjection * uView * vec4(vFragPos, 1.0);
}