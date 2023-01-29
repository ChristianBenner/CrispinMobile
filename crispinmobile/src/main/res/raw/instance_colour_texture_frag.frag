#version 300 es
precision mediump float;

in vec2 vTextureCoordinates;
in vec4 vColour;

out vec4 FragColor;

uniform sampler2D uTexture;

void main()
{
    FragColor =  texture(uTexture, vTextureCoordinates) * vColour;
}