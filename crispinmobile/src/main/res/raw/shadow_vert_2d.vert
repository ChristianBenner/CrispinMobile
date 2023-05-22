#version 300 es
layout (location = 0) in vec4 aPosition;

uniform mat4 uView;
uniform mat4 uModel;
uniform vec2 uLightPos;

void main() {
    // Calculate the position of the vertex in world space
    vec2 worldSpace = (uModel * aPosition).xy;

    // If the Z co-ordinate is 0, then it is not a shadow far co-ordinate, otherwise it is transformed
    vec2 transformed = worldSpace - (aPosition.z * uLightPos);

    // Use the Z co-ordinate to output a homogenous co-ordinate (infinite)
    gl_Position = uView * vec4(transformed, 0.0, 1.0 - aPosition.z);
}

