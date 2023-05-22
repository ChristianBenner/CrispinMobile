#version 300 es
layout (location = 0) in vec4 aPosition;
layout (location = 1) in mat4 aModel;

uniform mat4 uOrthographic;
uniform vec2 uLightPos;

void main() {
    // Calculate the position of the vertex in world space
    vec2 worldSpace = (aModel * aPosition).xy;

    // If the Z co-ordinate is 0, then it is not a shadow far co-ordinate, otherwise it is transformed
    vec2 transformed = worldSpace - (aPosition.z * uLightPos);

    // Use the Z co-ordinate to output a homogenous co-ordinate (infinite)
    gl_Position = uOrthographic * vec4(transformed, 0.0, 1.0 - aPosition.z);
}

