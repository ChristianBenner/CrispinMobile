#version 300 es
#ifdef GL_ES
precision highp float;
precision mediump int;
precision mediump sampler2DArray;
#endif

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
in vec3 vNormal;
in vec2 vTextureCoordinates;

out vec4 FragColor;

uniform vec4 uColour;
uniform vec2 uViewDimension;
uniform sampler2D uTexture;
uniform sampler2DArray uShadow;
uniform Material uMaterial;
uniform PointLight uPointLights[MAX_NUM_POINT_LIGHTS];
uniform int uNumPointLights;

vec3 CalculatePointLight(PointLight light, vec3 fragPos, float shadow);

void main()
{
    vec3 lightCalc = vec3(0.0);

    vec2 shadowTexCoord = gl_FragCoord.xy / uViewDimension;

    // Calculate all the point lights
    for(int i = 0; i < MAX_NUM_POINT_LIGHTS && i < uNumPointLights; i++) {
        float shadowStrength = texture(uShadow, vec3(shadowTexCoord, i)).r;
        lightCalc += CalculatePointLight(uPointLights[i], vFragPos, shadowStrength);
    }

 //   FragColor = vec4(lightCalc, 1.0) * texture(uTexture, vTextureCoordinates) * uColour;
    FragColor = (vec4(lightCalc, 1.0) * texture(uTexture, vTextureCoordinates) * uColour);
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
vec3 CalculatePointLight(PointLight light, vec3 fragPos, float shadow) {
    // Calculate the distance of the light from the point
    float distance = length(light.position - fragPos);
//    float attenuation = 1.0 / (light.constant + (light.linear * distance) + (light.quadratic * (distance * distance)));
    float attenuation = max(0.0, 1.0 - (distance / light.constant));

    // Ambient lighting calculation
    vec3 ambient = uMaterial.ambient * light.ambient * light.colour * attenuation;

    // Diffuse lighting calculation
    vec3 diffuse = uMaterial.diffuse * light.colour * attenuation * light.diffuse;// * max(0.0, (1.0 - shadow));

    return ambient + diffuse;
}