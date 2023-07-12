#version 300 es
layout (location = 0) in vec4 aPosition;

uniform mat4 uView;
uniform mat4 uModel;

out vec3 vFragPos;

void main() {
    vFragPos = vec3(uModel * aPosition);
    gl_Position = uView * uModel * aPosition;
}

