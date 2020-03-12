
attribute vec4 vPosition;
attribute vec2 vTextureCoordinates;

uniform mat4 uMatrix;
uniform vec2 uUvMultiplier;
uniform vec2 uUVOffset;

varying vec2 aTextureCoordinates;

void main()
{
    aTextureCoordinates = uUVOffset + (uUvMultiplier * vTextureCoordinates);
    gl_Position = uMatrix * vPosition;
}