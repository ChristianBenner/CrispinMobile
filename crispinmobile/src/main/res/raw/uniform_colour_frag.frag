#version 300 es
precision mediump float;

uniform vec4 uColour;
out vec4 FragColor;

void main()
{
    FragColor = uColour;
}