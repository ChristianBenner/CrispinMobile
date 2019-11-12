precision mediump float;

uniform vec4 uColour;
uniform sampler2D uTexture;
uniform sampler2D uSpecularMap;
uniform sampler2D uNormalMap;

varying vec3 vNormal;
varying vec3 vLightDir;
varying vec3 vPosition;
varying vec3 vEyeDirection;
varying vec2 vTextureCoordinates;

void main()
{
    // Diffuse
    vec3 lightColour = vec3(1.0, 1.0, 1.0);
    vec3 ambientColour = vec3(0.5, 0.5, 0.5) * uColour.rgb;
    vec3 diffuseColour = vec3(1.0f, 1.0f, 1.0f);
    vec3 specularColour = vec3(1.0, 1.0, 1.0);
    float lightIntensity = 200.0f;

    float distance = length(vLightDir - vPosition);
    vec3 n = normalize(vNormal);
  //  vec3 rgb_normal = n * 0.5 + 0.5;

   // vec3 normal = texture2D(uNormalMap, vTextureCoordinates).rgb;
  //  normal = normalize(normal * 2.0 - 1.0);
   // n = ((2 * texture2D(uNormalMap, vTextureCoordinates)) - 1).rgb;
    vec3 l = normalize(vLightDir);

    float cosTheta = clamp(dot(n, l), 0.0f, 1.0f);

    // Specular
    vec3 E = normalize(vEyeDirection);
    vec3 R = reflect(-vLightDir, vNormal);
    float cosAlpha = clamp(dot(E, R), 0.0, 1.0);

   // vec3 colour = uColour.rgba;

    // Multi texture test
 //   gl_FragColor = vec4((texture2D(uTexture, vTextureCoordinates).rgb + texture2D(uSpecularMap, vTextureCoordinates).rgb) / 2.0, 1.0);

    vec2 flippedTex = vec2(vTextureCoordinates.x, -vTextureCoordinates.y);
    // Specular map test
  gl_FragColor = uColour * texture2D(uTexture, flippedTex) * vec4(ambientColour +
        diffuseColour * lightColour * lightIntensity * cosTheta / (distance*distance) +
        ((specularColour * lightColour * lightIntensity * pow(cosAlpha, 5.0) / (distance*distance)) * texture2D(uSpecularMap, flippedTex).r), 1.0);


  //  gl_FragColor = texture2D(uNormalMap, vTextureCoordinates);
    //gl_FragColor = vec4(((specularColour * lightColour * lightIntensity * pow(cosAlpha, 5.0) / (distance*distance))), 1.0);

}