#version 300 es
precision mediump float;

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

struct DirectionalLight {
    vec3 direction;
    vec3 colour;
    float ambient;
    float diffuse;
    float specular;
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

struct SpotLight {
    vec3 position;
    vec3 direction;
    vec3 colour;
    float ambient;
    float diffuse;
    float specular;
    float constant;
    float linear;
    float quadratic;
    float size;
    float outerSize;
};

#define MAX_NUM_POINT_LIGHTS 10
#define MAX_NUM_SPOT_LIGHTS 5

in vec3 vFragPos;
in vec3 vNormal;

out vec4 FragColor;

uniform vec4 uColour;
uniform vec3 uViewPosition;
uniform Material uMaterial;
uniform DirectionalLight uDirectionalLight;
uniform PointLight uPointLights[MAX_NUM_POINT_LIGHTS];
uniform int uNumPointLights;
uniform SpotLight uSpotLights[MAX_NUM_SPOT_LIGHTS];
uniform int uNumSpotLights;

vec3 CalculateDirectionalLight(DirectionalLight light, vec3 normal, vec3 viewDirection);
vec3 CalculatePointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDirection);
vec3 CalculateSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDirection);

void main()
{
    vec3 viewDirection = normalize(uViewPosition - vFragPos);
    vec3 normal = normalize(vNormal);

    vec3 lightCalc = vec3(0.0);

    lightCalc = CalculateDirectionalLight(uDirectionalLight, normal, viewDirection);

    // Calculate all the point lights
    for(int i = 0; i < MAX_NUM_POINT_LIGHTS && i < uNumPointLights; i++) {
        lightCalc += CalculatePointLight(uPointLights[i], normal, vFragPos, viewDirection);
    }

    // Calculate all the spot lights
    for(int i = 0; i < MAX_NUM_SPOT_LIGHTS && i < uNumSpotLights; i++) {
        lightCalc += CalculateSpotLight(uSpotLights[i], normal, vFragPos, viewDirection);
    }

    FragColor = vec4(lightCalc, 1.0) * uColour;
}

vec3 CalculateDirectionalLight(DirectionalLight light, vec3 normal, vec3 viewDirection) {
    // Direction that the light is travelling from the source to the frag
    vec3 lightDirection = normalize(-light.direction);

    // Ambient lighting calculation
    vec3 ambient = uMaterial.ambient * light.ambient * light.colour;

    // Diffuse lighting calculation
    float normalDiffuseStrength = max(dot(normal, lightDirection), 0.0);
    vec3 diffuse = uMaterial.diffuse * normalDiffuseStrength * light.colour * light.diffuse;

    // Specular lighting calculation
    vec3 reflectDirection = reflect(-lightDirection, normal);
    float viewRefractionStrength = pow(max(dot(viewDirection, reflectDirection), 0.0), uMaterial.shininess);
    vec3 specular = uMaterial.specular * light.specular * viewRefractionStrength * light.colour;

    return ambient + diffuse + specular;
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

vec3 CalculateSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir) {
    vec3 lightDir = normalize(light.position - fragPos);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), uMaterial.shininess);
    // attenuation
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    // spotlight intensity
    float theta = dot(lightDir, normalize(-light.direction));
    float epsilon = light.size - light.outerSize;
    float intensity = clamp((theta - light.outerSize) / epsilon, 0.0, 1.0);
    // combine results
    vec3 ambient = vec3(light.ambient);
    vec3 diffuse = vec3(light.diffuse) * diff;
    vec3 specular = vec3(light.specular) * spec;
    ambient *= attenuation * intensity;
    diffuse *= attenuation * intensity;
    specular *= attenuation * intensity;
    return (ambient + diffuse + specular);


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
//
//    //return ambient + diffuse + specular;
//    return lightDirection;
}