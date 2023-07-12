#version 300 es
precision highp float;
precision lowp sampler2DArray;

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

struct EmissiveEdge {
    vec3 pointA;
    vec3 pointB;
    vec3 colour;
    float ambient;
    float diffuse;
    float specular;
    float constant;
    float linear;
    float quadratic;
};

struct DirectionalLight {
    vec3 direction;
    vec3 colour;
    float ambient;
    float diffuse;
    float specular;
};

#define MAX_NUM_POINT_LIGHTS 10
#define MAX_NUM_EMISSIVE_EDGES 10

in vec3 vFragPos;

uniform vec4 uColour;
uniform Material uMaterial;

uniform DirectionalLight uDirectionalLight;
uniform PointLight uPointLights[MAX_NUM_POINT_LIGHTS];
uniform int uNumPointLights;

uniform EmissiveEdge uEmissiveEdges[MAX_NUM_EMISSIVE_EDGES];
uniform int uNumEmissiveEdges;

out vec4 FragColor;

float calculateShortestDistance(vec2 pointP, vec2 pointA, vec2 pointB);
vec3 CalculateDirectionalLight(DirectionalLight light);
vec3 CalculateEdgeEmissionLight(EmissiveEdge edge, vec3 fragPos);
vec3 CalculatePointLight(PointLight light, vec3 fragPos);

void main()
{
    vec3 lightCalc = vec3(0.0);

    lightCalc = CalculateDirectionalLight(uDirectionalLight);

    for(int i = 0; i < MAX_NUM_EMISSIVE_EDGES && i < uNumEmissiveEdges; i++) {
        lightCalc += CalculateEdgeEmissionLight(uEmissiveEdges[i], vFragPos);
    }

    // Calculate all the point lights
    for(int i = 0; i < MAX_NUM_POINT_LIGHTS && i < uNumPointLights; i++) {
        lightCalc += CalculatePointLight(uPointLights[i], vFragPos);
    }

    FragColor = vec4(lightCalc, 1.0) * uColour;
}

/**
 * Calculate the effect of a directional light. Includes calculation for ambience (constantly lit
 * areas despite the direction), diffusion (strength affected by the normal/direction of the face)
 * and specular highlights (glints that reflect light). A directional light has no position and does
 * not represent an entity in world space, but instead lights objects from a given direction.
 *
 * @param light             DirectionalLight
 * @return                  vec3 containing the colour data from the directional light calculation
 */
vec3 CalculateDirectionalLight(DirectionalLight light) {
    // Direction that the light is travelling from the source to the frag
    vec3 lightDirection = normalize(-light.direction);

    // Ambient lighting calculation
    vec3 ambient = uMaterial.ambient * light.ambient * light.colour;

    // Diffuse lighting calculation
    vec3 diffuse = uMaterial.diffuse * light.colour * light.diffuse;

    return ambient + diffuse;
}

/**
 * Calculate the effect of a provided emissive edge/line
 *
 * @param light             PointLight
 * @param distance          Distance between the edge and the fragment
 * @return                  vec3 containing the colour data from the point light calculation
 */
vec3 CalculateEdgeEmissionLight(EmissiveEdge edge, vec3 fragPos) {
    float distance = calculateShortestDistance(fragPos.xy, edge.pointA.xy, edge.pointB.xy);

//    float attenuation = 1.0 / (edge.constant + (edge.linear * distance) + (edge.quadratic * (distance * distance)));
    float attenuation = max(0.0, 1.0 - (distance / edge.constant));

    // Ambient lighting calculation
    vec3 ambient = uMaterial.ambient * edge.colour * edge.ambient;

    // Diffuse lighting calculation
    vec3 diffuse = uMaterial.diffuse * edge.colour * attenuation * edge.diffuse;

    return ambient + diffuse;
}

/**
 * Calculate the effect of a provided point light. Includes calculation for attenuation (simulating
 * light intensity decreasing over distance), ambience (constantly lit areas despite the direction),
 * diffusion (strength affected by the normal/direction of the face) and specular highlights (glints
 * that reflect light).
 *
 * @param light             PointLight
 * @param fragPos           Position of the fragment
 * @return                  vec3 containing the colour data from the point light calculation
 */
vec3 CalculatePointLight(PointLight light, vec3 fragPos) {
    // Calculate the distance of the light from the point
    float distance = length(light.position - fragPos);

    //    float attenuation = 1.0 / (light.constant + (light.linear * distance) + (light.quadratic * (distance * distance)));
    float attenuation = max(0.0, 1.0 - (distance / light.constant));

    // Ambient lighting calculation
    vec3 ambient = uMaterial.ambient * light.colour * light.ambient;

    // Diffuse lighting calculation
    vec3 diffuse = uMaterial.diffuse * light.colour * attenuation * light.diffuse;

    return ambient + diffuse;
}

float calculateShortestDistance(vec2 pointP, vec2 pointA, vec2 pointB) {
    vec2 AP = pointP - pointA;
    vec2 AB = pointB - pointA;
    float magnitudeAB = length(AB);

    // Check if the line segment is degenerate (zero-length)
    if (magnitudeAB < 0.00001) {
        return length(AP);
    }

    // Calculate the parameterized position along the line segment
    float t = clamp(dot(AP, AB) / (magnitudeAB * magnitudeAB), 0.0, 1.0);

    // Calculate the closest point on the line segment
    vec2 closestPoint = pointA + t * AB;

    // Calculate the shortest distance
    float shortestDistance = length(pointP - closestPoint);

    return shortestDistance;
}