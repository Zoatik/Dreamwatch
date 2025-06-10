
uniform vec2 resolution;
uniform float time;
uniform sampler2D texture0;

in vec4 v_color;

const float PI  = 3.141592;
const float OFF = 0.03;

float sdCircle(vec2 p, float r) {
    return length(p) - r;
}

float rand(vec2 p) {
    return fract(sin(dot(p, vec2(12.99, 78.233))) * 43758.545);
}

float shift(float r) {
    return sin(r * 2. * PI + time * 7.) * 0.5 + 0.5;
}

float noise(vec2 p) {
    vec2 f = fract(p);
    vec2 i = floor(p);
    return mix(mix(shift(rand(i + vec2(0, 0))),
                   shift(rand(i + vec2(1, 0))), f.x),
               mix(shift(rand(i + vec2(0, 1))),
                   shift(rand(i + vec2(1, 1))), f.x), f.y);
}

float fbm(vec2 p) {
    float v = 0.;
    float a = 1.;
    for (int i = 0; i < 4; ++i) {
        vec2 p1 = p * 4. / a;
        a *= 0.5;
        v += a * noise(p1);
    }
    return v;
}

void main(void) {
	vec2 p = (gl_FragCoord.xy * 2. - resolution.xy) / resolution.y;


	vec2 r = vec2(fbm(p + vec2(0,  OFF)) - fbm(p + vec2( 0, -OFF)),
                 -fbm(p + vec2(OFF,  0)) + fbm(p + vec2(-OFF,  0))) * 0.01;
    r += vec2(0.002, 0.0045);

    vec2 uv = gl_FragCoord.xy / resolution.xy;
	float dCircle = sdCircle(p, 0.1);
	vec3 col = pow(texture(texture0, uv - r).rgb, vec3(1.15))
             + vec3(smoothstep(0.01, -0.01, abs(dCircle) - 0.007)) * vec3(1., 0.6, 0.25);
	col = clamp(col, 0., 1.);
    gl_FragColor = vec4(col, 1.);
}
