#version 300 es
precision mediump float;

uniform vec4 uColour;

void main()
{
    vec4 colour = uColour;
    gl_FragColor = colour;
}