#version 300 es
layout (location = 0) in vec4 aPosition;

uniform mat4 uView;
uniform mat4 uModel;
uniform vec2 uLightPos;

// new
out float vAlpha;

void main() {
    float targetLength = 8.0; // todo: make uniform
    float minimumDistanceFromStart = 1.0;

    // todo: make perpendicular uniform. Have to calculate the direction between two points that can be rotated and get the perpendicular
    vec2 edgeDirection = vec2(0.0, 1.0);
    vec2 edgePerpendicular = vec2(-edgeDirection.y, edgeDirection.x);

    // The alpha component of the far component should be 0 so it fades out
    vAlpha = 1.0 - aPosition.z;

    // Calculate the position of the vertex in world space
    vec2 worldSpace = (uModel * aPosition).xy;

    // Direction from the light source to the point
    vec2 extendDirection = normalize(worldSpace - uLightPos);

    // Dot product between the edge perpendicular and the direction to extend to
    float dotProd = dot(edgePerpendicular, extendDirection);

    // Distance to make the extended point away from its current position
    float extensionDistance = targetLength / abs(dotProd);

    // Distance to make the extended point away from its current position regardless of near or far shadow
    float extensionDistanceMin = minimumDistanceFromStart / abs(dotProd);

    // Vector to extend the point from its current position. If aPosition.z is 0 that means it is on
    // the edge and does not extend
    vec2 extensionVector = (extensionDistanceMin * extendDirection) + (extensionDistance * extendDirection * aPosition.z);

    gl_Position = uView * vec4(worldSpace + extensionVector, 0.0, 1.0);
}

