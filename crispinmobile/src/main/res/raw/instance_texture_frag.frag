#version 300 es
precision mediump float;

in vec2 vTextureCoordinates;

out vec4 FragColor;

uniform sampler2D uTexture;
uniform vec4 uColour;

void main()
{
    FragColor = texture(uTexture, vTextureCoordinates) * uColour;
}