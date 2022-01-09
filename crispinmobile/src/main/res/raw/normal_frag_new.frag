#version 300 es
precision mediump float;

out vec4 FragColor;

uniform vec4 uColour;
uniform vec3 uLightPosition;
uniform vec3 uViewPosition;
uniform vec3 uLightColour;

in vec3 vFragPos;
in vec3 vNormal;

void main()
{
    // ambient
    float ambientStrength = 0.1;
    vec3 ambient = ambientStrength * uLightColour;

    // diffuse
    vec3 norm = normalize(vNormal);
    vec3 lightDir = normalize(uLightPosition - vFragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * uLightColour;

    // specular
    float specularStrength = 0.5;
    vec3 viewDir = normalize(uViewPosition - vFragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32.0);
    vec3 specular = specularStrength * spec * uLightColour;

    vec4 result = vec4((ambient + diffuse + specular), 1.0) * uColour;
    FragColor = result;
}