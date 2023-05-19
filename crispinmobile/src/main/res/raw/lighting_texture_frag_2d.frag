#version 300 es
precision mediump float;

struct Material {
    vec3 ambient;
    vec3 diffuse;
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

#define MAX_NUM_POINT_LIGHTS 10

in vec3 vFragPos;
in vec2 vTextureCoordinates;

uniform vec4 uColour;
uniform sampler2D uTexture;
uniform sampler2D uShadow;
uniform sampler2D uSpecularMap;
uniform Material uMaterial;
uniform PointLight uPointLights[MAX_NUM_POINT_LIGHTS];
uniform int uNumPointLights;

out vec4 FragColor;

vec3 CalculatePointLight(PointLight light, vec3 normal, vec3 fragPos, float specularModifier, float shadow);

void main()
{
    vec3 lightCalc = vec3(0.0);

    // the normal is straight upwards for 2D
    vec3 normal = vec3(0.0, 0.0, 1.0);

    float specularModifier = texture(uSpecularMap, vTextureCoordinates).r;

    // Frag pos on screen / texture size to determine what pixel in the shadow texture to compare against
    // 1080, 2138
    vec2 shadowTexCoord = gl_FragCoord.xy / vec2(1080, 2138);
    float shadowStrength = texture(uShadow, shadowTexCoord).r;

    // Calculate all the point lights
    for(int i = 0; i < MAX_NUM_POINT_LIGHTS && i < uNumPointLights; i++) {
        lightCalc += CalculatePointLight(uPointLights[i], normal, vFragPos, specularModifier, shadowStrength);
    }

    FragColor = vec4(lightCalc, 1.0) * texture(uTexture, vTextureCoordinates) * uColour;
}

/**
 * Calculate the effect of a provided point light. Includes calculation for attenuation (simulating
 * light intensity decreasing over distance), ambience (constantly lit areas despite the direction),
 * diffusion (strength affected by the normal/direction of the face) and specular highlights (glints
 * that reflect light).
 *
 * @param light             PointLight
 * @param normal            Direction of the face
 * @param fragPos           Position of the fragment
 * @param specularModifier  Specular modifier multiplied against the specular calculation. A
 *                              specular map can provide a specularModifier
 * @return                  vec3 containing the colour data from the point light calculation
 */
vec3 CalculatePointLight(PointLight light, vec3 normal, vec3 fragPos, float specularModifier, float shadow) {
    // Calculate the distance of the light from the point
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + (light.linear * distance) + (light.quadratic * (distance * distance)));

    // Direction that the light is travelling from the source to the frag
    vec3 lightDirection = normalize(light.position - fragPos);

    // Ambient lighting calculation
    vec3 ambient = uMaterial.ambient * light.ambient * light.colour * attenuation;

    // Diffuse lighting calculation
    vec3 diffuse = uMaterial.diffuse * light.colour * attenuation * light.diffuse * (1.0 - shadow);

    return ambient + diffuse;
}