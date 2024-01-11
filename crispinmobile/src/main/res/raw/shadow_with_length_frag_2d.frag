#version 300 es
precision mediump float;

out vec4 FragColor;

// new
in float vAlpha;

void main() {
    FragColor = vec4(0.0, 0.0, 0.0, vAlpha);
}