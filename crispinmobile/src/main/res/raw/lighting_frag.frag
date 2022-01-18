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
    float constant;
    float linear;
    float quadratic;
};

//struct Spotlight {
//    vec3 position;
//    vec3 direction;
//
//    vec3 colour;
//
//    // Strengths of lighting techniques
//    float ambient;
//    float diffuse;
//    float specular;
//
//    // Used in itensity attenuation calculation
//    float constant;
//    float linear;
//    float quadratic;
//
//    float size;
//    float outerSize;
//};

in vec3 vFragPos;
in vec3 vNormal;

out vec4 FragColor;

uniform vec4 uColour;
uniform vec3 uViewPosition;
uniform int uNumPointLights;
uniform Material uMaterial;

#define MAX_NUM_POINT_LIGHTS 10
uniform PointLight uPointLights[MAX_NUM_POINT_LIGHTS];

vec3 CalculatePointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDirection);

void main()
{
    vec3 viewDirection = normalize(uViewPosition - vFragPos);
    vec3 normal = normalize(vNormal);

    // Calculate all the point lights
    vec3 lightCalc = vec3(0.0);
    for(int i = 0; i < MAX_NUM_POINT_LIGHTS && i < uNumPointLights; i++) {
        lightCalc += CalculatePointLight(uPointLights[i], normal, vFragPos, viewDirection);
    }

    FragColor = vec4(lightCalc, 1.0) * uColour;
}

vec3 CalculatePointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDirection) {
    // Calculate the distance of the light from the point
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + (light.linear * distance) + (light.quadratic * (distance * distance)));

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
    vec3 specular = uMaterial.specular * light.specular * viewRefractionStrength * light.colour * attenuation;
    return ambient + diffuse + specular;
}

//vec3 CalculateSpotlight(Light light, vec3 normal, vec3 fragPos, vec3 viewDirection) {
//    // Calculate the distance of the light from the point
//    float distance = length(light.position - fragPos);
//    float attenuation = 1.0 / (light.constant + (light.linear * distance) + (light.quadratic * (distance * distance)));
//
//    // Direction that the light is travelling from the source to the frag
//    vec3 lightDirection = normalize(light.position - fragPos);
//
//    // Calculate the spotlight intensity multiplier
//    float angle = dot(lightDirection, normalize(-light.direction));
//    float fadeSize = light.size - light.outerSize;
//    //  float spotlight = clamp((angle - light.outerSize) / angle, 0.0, 1.0);
//    float spotlight = smoothstep(0.0, 1.0, (angle - light.outerSize) / fadeSize);
//
//    // Ambient lighting calculation
//    vec3 ambient = uMaterial.ambient * light.ambient * light.colour * attenuation;
//
//    // Diffuse lighting calculation
//    float normalDiffuseStrength = max(dot(normal, lightDirection), 0.0);
//    vec3 diffuse = uMaterial.diffuse * normalDiffuseStrength * light.colour * attenuation * light.diffuse * spotlight;
//
//    // Specular lighting calculation
//    vec3 reflectDirection = reflect(-lightDirection, normal);
//    float viewRefractionStrength = pow(max(dot(viewDirection, reflectDirection), 0.0), uMaterial.shininess);
//    vec3 specular = uMaterial.specular * light.specular * viewRefractionStrength * light.colour * attenuation * spotlight;
//    return ambient + diffuse + specular;
//}