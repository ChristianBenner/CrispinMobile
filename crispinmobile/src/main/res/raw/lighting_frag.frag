#version 300 es
precision mediump float;

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

struct Light {
    vec3 position;
    vec3 colour;
    float intensity;
    float ambient;
    float specular;
};

in vec3 vFragPos;
in vec3 vNormal;

out vec4 FragColor;

uniform vec4 uColour;
uniform vec3 uViewPosition;
uniform Material uMaterial;
uniform Light uLight;

void main()
{
    // Calculate the distance of the light from the point
    float distance = length(uLight.position - vFragPos);
    float distanceIntensityModifier = uLight.intensity / (distance * distance);

    // Direction that the light is travelling from the source to the frag
    vec3 lightDirection = normalize(uLight.position - vFragPos);

    // Ambient lighting calculation
    vec3 ambient = uMaterial.ambient * uLight.ambient * uLight.colour;

    // Diffuse lighting calculation
    vec3 normal = normalize(vNormal);
    float normalDiffuseStrength = max(dot(normal, lightDirection), 0.0);
    vec3 diffuse = uMaterial.diffuse * normalDiffuseStrength * uLight.colour * distanceIntensityModifier;

    // Specular lighting calculation
    vec3 viewDirection = normalize(uViewPosition - vFragPos);
    vec3 reflectDirection = reflect(-lightDirection, normal);
    float viewRefractionStrength = pow(max(dot(viewDirection, reflectDirection), 0.0), uMaterial.shininess);
    vec3 specular = uMaterial.specular * uLight.specular * viewRefractionStrength * uLight.colour * distanceIntensityModifier;

    FragColor = vec4((ambient + diffuse + specular), 1.0) * uColour;
}