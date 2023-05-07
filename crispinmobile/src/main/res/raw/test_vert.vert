#version 300 es
precision mediump float;

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTextureCoordinates;
layout (location = 3) in vec3 aTangent;

uniform mat4 uProjection;
uniform mat4 uView;
uniform mat4 uModel;

uniform vec3 uLightPos;
uniform vec3 uViewPosition;

out vec3 FragPos;
out vec2 TexCoords;
out vec3 TangentLightPos;
out vec3 TangentViewPos;
out vec3 TangentFragPos;

void main()
{
    FragPos = vec3(uModel * vec4(aPosition, 1.0));
    TexCoords = aTextureCoordinates * 5.0;

    mat3 normalMatrix = transpose(inverse(mat3(uModel)));
    vec3 T = normalize(normalMatrix * aTangent);
    vec3 N = normalize(normalMatrix * aNormal);
    T = normalize(T - dot(T, N) * N);
    vec3 B = cross(N, T);

    mat3 TBN = transpose(mat3(T, B, N));
    TangentLightPos = TBN * uLightPos;
    TangentViewPos  = TBN * uViewPosition;
    TangentFragPos  = TBN * FragPos;

    gl_Position = uProjection * uView * uModel * vec4(aPosition, 1.0);
}