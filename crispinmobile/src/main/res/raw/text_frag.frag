#version 300 es
precision highp float;

in vec2 vTextureCoordinates;

uniform vec4 uColour;
uniform sampler2D uTexture;

out vec4 FragColor;

void main()
{
    vec4 sampledCol = vec4(1.0, 1.0, 1.0, texture(uTexture, vTextureCoordinates).r);
    FragColor = uColour * sampledCol;
}