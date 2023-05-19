#version 300 es
layout (location = 0) in vec4 aPosition;
layout (location = 1) in vec2 aTextureCoordinates;

uniform mat4 uView;
uniform mat4 uModel;
uniform vec2 uUvMultiplier;

out vec3 vFragPos;
out vec2 vTextureCoordinates;

void main() {
    vFragPos = vec3(uModel * aPosition);
    vTextureCoordinates = uUvMultiplier * vec2(aTextureCoordinates.s, aTextureCoordinates.t);
    gl_Position = uView * uModel * aPosition;
}

