#version 300 es
precision mediump float;

out vec4 FragColor;

uniform vec4 uColour;
uniform sampler2D uTexture;

in vec2 vTextureCoordinates;

void main()
{
    vec4 calc = uColour * texture(uTexture, vTextureCoordinates);
    if(calc.a < 0.1) {
        discard;
    }
    FragColor = calc;
}