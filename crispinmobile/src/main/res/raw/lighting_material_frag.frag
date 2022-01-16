#version 300 es
precision mediump float;

in vec3 vFragPos;
in vec3 vNormal;
in vec2 vTextureCoordinates;

uniform sampler2D uTexture;
uniform sampler2D uSpecularMap;
uniform sampler2D uNormalMap;
uniform vec4 uColour;
uniform vec3 uLightPosition;
uniform vec3 uViewPosition;
uniform vec3 uLightColour;
uniform float uLightIntensity;
uniform float uLightAmbienceStrength;
uniform float uSpecularStrength;

out vec4 FragColor;

void main()
{
    // Calculate the distance of the light from the point
    float distance = length(uLightPosition - vFragPos);
    float distanceIntensityModifier = uLightIntensity / (distance * distance);

    // Direction that the light is travelling from the source to the frag
    vec3 lightDirection = normalize(uLightPosition - vFragPos);

    // Ambient lighting calculation
    vec3 ambient = uLightAmbienceStrength * uLightColour;

    // Diffuse lighting calculation
    vec3 normal = normalize(vNormal);
    float normalDiffuseStrength = max(dot(normal, lightDirection), 0.0);
    vec3 diffuse = normalDiffuseStrength * uLightColour * distanceIntensityModifier;

    // Specular lighting calculation
    vec3 viewDirection = normalize(uViewPosition - vFragPos);
    vec3 reflectDirection = reflect(-lightDirection, normal);
    float viewRefractionStrength = pow(max(dot(viewDirection, reflectDirection), 0.0), 32.0) * texture(uSpecularMap, vTextureCoordinates).r;
    vec3 specular = uSpecularStrength * viewRefractionStrength * uLightColour * distanceIntensityModifier;

    vec4 lightingResult = vec4((ambient + diffuse + specular), 1.0);
    FragColor = texture(uTexture, vTextureCoordinates) * lightingResult * uColour;
}