precision mediump float;

uniform vec4 uColour;
uniform sampler2D uTexture;

varying vec2 aTextureCoordinates;

void main()
{
    vec4 sampledCol = vec4(1.0, 1.0, 1.0, texture2D(uTexture, aTextureCoordinates).r);
    gl_FragColor = uColour * sampledCol;
}