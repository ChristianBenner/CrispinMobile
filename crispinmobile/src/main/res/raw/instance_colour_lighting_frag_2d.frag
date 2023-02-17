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
in vec4 vColour;

out vec4 FragColor;

uniform Material uMaterial;
uniform DirectionalLight uDirectionalLight;
uniform PointLight uPointLights[MAX_NUM_POINT_LIGHTS];
uniform int uNumPointLights;
uniform SpotLight uSpotLights[MAX_NUM_SPOT_LIGHTS];
uniform int uNumSpotLights;

vec3 CalculatePointLight(PointLight light, vec3 fragPos);

void main()
{
    vec3 lightCalc = vec3(0.0);

    // Calculate all the point lights
    for(int i = 0; i < MAX_NUM_POINT_LIGHTS && i < uNumPointLights; i++) {
        lightCalc += CalculatePointLight(uPointLights[i], vFragPos);
    }

    FragColor = vec4(lightCalc, 1.0) * vColour;
}

/**
 * Calculate the effect of a provided point light. Includes calculation for attenuation (simulating
 * light intensity decreasing over distance), ambience (constantly lit areas despite the direction),
 * diffusion (strength affected by the normal/direction of the face) and specular highlights (glints
 * that reflect light). A point light is a light emitting from a single point in all directions.
 *
 * @param light         PointLight
 * @param fragPos       Position of the fragment
 * @return              vec3 containing the colour data from the point light calculation
 */
vec3 CalculatePointLight(PointLight light, vec3 fragPos) {
    // Calculate the distance of the light from the point
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + (light.linear * distance) + (light.quadratic * (distance * distance)));

    // Ambient lighting calculation
    vec3 ambient = uMaterial.ambient * light.ambient * light.colour * attenuation;

    // Diffuse lighting calculation
    vec3 diffuse = uMaterial.diffuse * light.colour * attenuation * light.diffuse;

    return ambient + diffuse;
}