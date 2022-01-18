#version 300 es
precision mediump float;

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

struct PointLight {
    vec3 position;
    vec3 colour;
    float ambient;
    float diffuse;
    float specular;
};

in vec3 vFragPos;
in vec3 vNormal;
in vec2 vTextureCoordinates;

uniform vec4 uColour;
uniform vec3 uViewPosition;
uniform sampler2D uTexture;
uniform sampler2D uSpecularMap;
uniform sampler2D uNormalMap;
uniform Material uMaterial;
uniform int uNumPointLights;

#define MAX_NUM_POINT_LIGHTS 10
uniform PointLight uPointLights[MAX_NUM_POINT_LIGHTS];

out vec4 FragColor;

vec3 CalculatePointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDirection, float specularModifier);

void main()
{
    vec3 viewDirection = normalize(uViewPosition - vFragPos);
    vec3 normal = normalize(vNormal);

    // Calculate all the point lights
    vec3 lightCalc = vec3(0.0);
    for(int i = 0; i < MAX_NUM_POINT_LIGHTS && i < uNumPointLights; i++) {
        lightCalc += CalculatePointLight(uPointLights[i], normal, vFragPos, viewDirection, texture(uSpecularMap, vTextureCoordinates).r);
    }

    FragColor = vec4(lightCalc, 1.0) * texture(uTexture, vTextureCoordinates) * uColour;
}

vec3 CalculatePointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDirection, float specularModifier) {
    // Calculate the distance of the light from the point
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (1.0 + 0.09 * distance + 0.032 * (distance * distance));

    // Direction that the light is travelling from the source to the frag
    vec3 lightDirection = normalize(light.position - fragPos);

    // Ambient lighting calculation
    vec3 ambient = uMaterial.ambient * light.ambient * light.colour * attenuation;

    // Diffuse lighting calculation
    float normalDiffuseStrength = max(dot(normal, lightDirection), 0.0);
    vec3 diffuse = uMaterial.diffuse * normalDiffuseStrength * light.colour * attenuation * light.diffuse;

    // Specular lighting calculation
    vec3 reflectDirection = reflect(-lightDirection, normal);
    float viewRefractionStrength = pow(max(dot(viewDirection, reflectDirection), 0.0), uMaterial.shininess);
    vec3 specular = uMaterial.specular * light.specular * viewRefractionStrength * light.colour * attenuation * specularModifier;
    return ambient + diffuse + specular;
}