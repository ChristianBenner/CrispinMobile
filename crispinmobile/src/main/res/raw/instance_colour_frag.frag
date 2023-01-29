#version 300 es
precision mediump float;

in vec4 vColour;

out vec4 FragColor;

void main()
{
    FragColor = vColour;
}