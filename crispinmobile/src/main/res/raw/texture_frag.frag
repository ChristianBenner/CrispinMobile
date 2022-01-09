#version 300 es
precision mediump float;

out vec4 FragColor;

uniform vec4 uColour;
uniform sampler2D uTexture;

in vec2 vTextureCoordinates;

void main()
{
    FragColor = uColour * texture(uTexture, vTextureCoordinates);
}