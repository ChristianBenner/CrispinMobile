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
in vec2 vTextureCoordinates;
in vec4 vColour;

out vec4 FragColor;

uniform vec3 uViewPosition;
uniform sampler2D uTexture;
uniform sampler2D uSpecularMap;
uniform sampler2D uNormalMap;
uniform Material uMaterial;
uniform DirectionalLight uDirectionalLight;
uniform PointLight uPointLights[MAX_NUM_POINT_LIGHTS];
uniform int uNumPointLights;
uniform SpotLight uSpotLights[MAX_NUM_SPOT_LIGHTS];
uniform int uNumSpotLights;

vec3 CalculateDirectionalLight(DirectionalLight light, vec3 normal, vec3 viewDirection, float specularModifier);
vec3 CalculatePointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDirection, float specularModifier);
vec3 CalculateSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDirection, float specularModifier);

void main()
{
    vec3 viewDirection = normalize(uViewPosition - vFragPos);
    vec3 normal = normalize(vNormal);

    vec3 lightCalc = vec3(0.0);

    float specularModifier = texture(uSpecularMap, vTextureCoordinates).r;
    lightCalc = CalculateDirectionalLight(uDirectionalLight, normal, viewDirection, specularModifier);

    // Calculate all the point lights
    for(int i = 0; i < MAX_NUM_POINT_LIGHTS && i < uNumPointLights; i++) {
        lightCalc += CalculatePointLight(uPointLights[i], normal, vFragPos, viewDirection, specularModifier);
    }

    // Calculate all the spot lights
    for(int i = 0; i < MAX_NUM_SPOT_LIGHTS && i < uNumSpotLights; i++) {
        lightCalc += CalculateSpotLight(uSpotLights[i], normal, vFragPos, viewDirection, specularModifier);
    }

    FragColor = vec4(lightCalc, 1.0) * texture(uTexture, vTextureCoordinates) * vColour;
}

/**
 * Calculate the effect of a directional light. Includes calculation for ambience (constantly lit
 * areas despite the direction), diffusion (strength affected by the normal/direction of the face)
 * and specular highlights (glints that reflect light). A directional light has no position and does
 * not represent an entity in world space, but instead lights objects from a given direction.
 *
 * @param light             DirectionalLight
 * @param normal            Direction of the face
 * @param viewDirection     Direction of the view matrix/camera
 * @param specularModifier  Specular modifier multiplied against the specular calculation. A
 *                              specular map can provide a specularModifier
 * @return                  vec3 containing the colour data from the directional light calculation
 */
vec3 CalculateDirectionalLight(DirectionalLight light, vec3 normal, vec3 viewDirection, float specularModifier) {
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
    vec3 specular = uMaterial.specular * light.specular * viewRefractionStrength * light.colour * specularModifier;

    return ambient + diffuse + specular;
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
 * @param viewDirection     Direction of the view matrix/camera
 * @param specularModifier  Specular modifier multiplied against the specular calculation. A
 *                              specular map can provide a specularModifier
 * @return                  vec3 containing the colour data from the point light calculation
 */
vec3 CalculatePointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDirection, float specularModifier) {
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
    vec3 specular = uMaterial.specular * light.specular * viewRefractionStrength * light.colour * attenuation * specularModifier;

    return ambient + diffuse + specular;
}

/**
 * Calculate the effect of a provided spot light. Includes calculation for attenuation (simulating
 * light intensity decreasing over distance), ambience (constantly lit areas despite the direction),
 * diffusion (strength affected by the normal/direction of the face) and specular highlights (glints
 * that reflect light). A spot light is a light emitting from a single point in a specific
 * directions. The size of the affected area can be configured.
 *
 * @param light             SpotLight
 * @param normal            Direction of the face
 * @param fragPos           Position of the fragment
 * @param viewDirection     Direction of the view matrix/camera
  * @param specularModifier  Specular modifier multiplied against the specular calculation. A
 *                              specular map can provide a specularModifier
 * @return              vec3 containing the colour data from the spot light calculation
 */
vec3 CalculateSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDirection, float specularModifier) {
    // Calculate the distance of the light from the point
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + (light.linear * distance) + (light.quadratic * (distance * distance)));

    // Direction that the light is travelling from the source to the frag
    vec3 lightDirection = normalize(light.position - fragPos);

    // Calculate the spotlight intensity multiplier
    float angle = dot(lightDirection, normalize(-light.direction));
    float fadeSize = light.size - light.outerSize;
    float spotlight = smoothstep(0.0, 1.0, (angle - light.outerSize) / fadeSize);

    // Diffuse lighting calculation
    float normalDiffuseStrength = max(dot(normal, lightDirection), 0.0);
    vec3 diffuse = uMaterial.diffuse * normalDiffuseStrength * light.colour * light.diffuse * attenuation * spotlight;

    // Specular lighting calculation
    vec3 reflectDirection = reflect(-lightDirection, normal);
    float viewRefractionStrength = pow(max(dot(viewDirection, reflectDirection), 0.0), uMaterial.shininess);
    vec3 specular = uMaterial.specular * light.specular * viewRefractionStrength * light.colour * attenuation * spotlight * specularModifier;

    return diffuse + specular;
}