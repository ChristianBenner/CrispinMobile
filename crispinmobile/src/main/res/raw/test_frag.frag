#version 300 es
precision mediump float;

out vec4 FragColor;

uniform sampler2D theTexture;
uniform sampler2D diffuseMap;
uniform sampler2D normalMap;

in vec3 FragPos;
in vec2 TexCoords;
in vec3 TangentLightPos;
in vec3 TangentViewPos;
in vec3 TangentFragPos;

void main()
{
     // obtain normal from normal map in range [0,1]
    vec3 normal = texture(normalMap, TexCoords).rgb;

    // transform normal vector to range [-1,1]
    normal = normalize(normal * 2.0 - 1.0);  // this normal is in tangent space

    // get diffuse color
    vec3 color = texture(diffuseMap, TexCoords).rgb;
    // ambient
    vec3 ambient = 0.4 * color;
    // diffuse
    vec3 lightDir = normalize(TangentLightPos - TangentFragPos);
    float diff = max(dot(lightDir, normal), 0.0);
    vec3 diffuse = diff * color * 3.0;
    // specular
    vec3 viewDir = normalize(TangentViewPos - TangentFragPos);
    vec3 reflectDir = reflect(-lightDir, normal);
    vec3 halfwayDir = normalize(lightDir + viewDir);
    float spec = pow(max(dot(normal, halfwayDir), 0.0), 32.0);

    vec3 specular = vec3(1.0) * spec;
    //FragColor = vec4(ambient + diffuse + specular, 1.0) * texture(theTexture, TexCoords);
    FragColor = vec4(diffuse.x, diffuse.y, diffuse.z, 1.0);
}