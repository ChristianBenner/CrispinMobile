#version 300 es
uniform mat4 uMatrix;
layout (location = 0) in vec4 aPosition;

void main() {
    gl_Position = uMatrix * aPosition;
}