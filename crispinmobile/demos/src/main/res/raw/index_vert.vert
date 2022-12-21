#version 300 es
layout (location = 0) in vec3 aPosition;
uniform mat4 uMatrixArr[625];

void main()
{
    gl_Position = uMatrixArr[gl_InstanceID] * vec4(aPosition, 1.0f);
}