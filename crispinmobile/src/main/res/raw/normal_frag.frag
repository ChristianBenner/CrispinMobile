precision mediump float;

uniform vec4 uColour;

varying vec3 vNormal;
varying vec3 vLightDir;
varying vec3 vPosition;
varying vec3 vEyeDirection;

void main()
{
//    // Diffuse
//    vec3 lightColour = vec3(1.0, 1.0, 1.0);
//    vec3 ambientColour = vec3(0.5, 0.5, 0.5) * uColour.rgb;
//    vec3 diffuseColour = vec3(1.0, 1.0, 1.0);
//    vec3 specularColour = vec3(1.0, 1.0, 1.0);
//    float lightIntensity = 40.0;
//
//    float distance = length(vLightDir - vPosition);
//    vec3 n = normalize(vNormal);
//    vec3 l = normalize(vLightDir);
//    float cosTheta = clamp(dot(n, l), 0.0, 1.0);
//
//    // Diffuse
//    vec3 diffuse = diffuseColour * lightColour * lightIntensity * cosTheta / (distance*distance);
//
//    // Specular
//    float specularStrength = 0.5;
//    vec3 E = normalize(vEyeDirection);
//    vec3 R = reflect(-vLightDir, vNormal);
//    float spec = pow(max(dot(E, R), 0.0), 32.0);
//    vec3 specular = specularStrength * spec * vec3(1.0, 1.0, 1.0);
//
//    //float cosAlpha = clamp(dot(E, R), 0.0, 1.0);
//
//    // Specular map test
//    gl_FragColor = uColour * vec4(ambientColour + diffuse + specular, 1.0);



//    // Diffuse
//    vec3 lightColour = vec3(1.0, 1.0, 1.0);
//    vec3 ambientColour = vec3(0.5, 0.5, 0.5) * uColour.rgb;
//    vec3 diffuseColour = vec3(1.0, 1.0, 1.0);
//    vec3 specularColour = vec3(1.0, 1.0, 1.0);
//    float lightIntensity = 40.0;
//
//    float distance = length(vLightDir - vPosition);
//    vec3 n = normalize(vNormal);
//    vec3 l = normalize(vLightDir);
//    float cosTheta = clamp(dot(n, l), 0.0, 1.0);
//
//    // Specular
//    vec3 E = normalize(vEyeDirection);
//    vec3 R = reflect(-vLightDir, vNormal);
//    float cosAlpha = clamp(dot(E, R), 0.0, 1.0);
//
//    // Specular map test
//    gl_FragColor = uColour * vec4(ambientColour + diffuseColour * lightColour * lightIntensity *
//        cosTheta / (distance*distance) + ((specularColour * lightColour *
//        pow(cosAlpha, 5.0) / (distance*distance))), 1.0);





    //    // Diffuse
//    vec3 lightColour = vec3(1.0, 1.0, 1.0);
//    vec3 ambientColour = vec3(0.5, 0.5, 0.5) * uColour.rgb;
//    vec3 diffuseColour = vec3(1.0, 1.0, 1.0);
//    vec3 specularColour = vec3(1.0, 1.0, 1.0);
//    float lightIntensity = 1.0;
//
//    float distance = length(vLightDir - vPosition);
//    vec3 n = normalize(vNormal);
//    vec3 l = normalize(vLightDir);
//
//    float cosTheta = clamp(dot(n, l), 0.0, 1.0);
//
//    // Specular
//    vec3 E = normalize(vEyeDirection);
//    vec3 R = reflect(-vLightDir, vNormal);
//    float cosAlpha = clamp(dot(E, R), 0.0, 1.0);
//
//    vec3 colour = uColour.rgb;
//
//   gl_FragColor = vec4(ambientColour +
//        diffuseColour * lightColour * lightIntensity * cosTheta / (distance*distance) +
//        specularColour * lightColour * lightIntensity * pow(cosAlpha, 5.0) / (distance*distance), 1.0);
//
//  //  gl_FragColor = vec4((specularColour * lightColour * lightIntensity * pow(cosAlpha, 5.0) / (distance*distance), 1.0));
//   // gl_FragColor = vec4(1.0f, 0.0f, 0.0f, 1.0f);
//  //  gl_FragColor = vec4(uColour.rgb + ambientColour, uColour.a) * vec4(lightColour * lightIntensity * cosTheta / (distance*distance), 1.0);
//




    vec4 specular = vec4(1.0, 1.0, 1.0, 1.0);
    float shininess = 0.0;
    vec4 spec = vec4(0.0);
    vec3 n2 = normalize(vNormal);
    vec3 e2 = normalize(vEyeDirection);
    float intensity2 = max(dot(n2, vLightDir), 0.0);

    if(intensity2 > 0.0) {
        vec3 h2 = normalize(vLightDir + e2);
        float intSpec = max(dot(h2, n2), 0.0);
        spec = specular * pow(intSpec, shininess);
    }
    vec3 ambientColour = vec3(0.1, 0.1, 0.1);
    gl_FragColor = max(intensity2 * spec, vec4(ambientColour, 1.0)) * uColour;
}