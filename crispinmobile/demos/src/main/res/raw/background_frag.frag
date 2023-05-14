#version 300 es
precision highp float;

uniform float uTime;
uniform vec2 uCenter;
out vec4 FragColor;

void main()
{
    vec2 position = gl_FragCoord.xy - uCenter;
    float angle = atan(position.y, position.x);
    float radius = length(position) / 50.0;
    vec3 color = vec3(0.5 + 0.5 * sin(angle + uTime), 0.5 + 0.5 * cos(angle + uTime), 0.5 + 0.5 * sin(radius + uTime));
    FragColor = vec4(color, 1.0);
}