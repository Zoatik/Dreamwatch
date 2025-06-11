// Original by localthunk (https://www.playbalatro.com)

uniform float time;
uniform vec2 resolution;



// Configuration (modify these values to change the effect)
const float SPIN_ROTATION = -0.5;
const float SPIN_SPEED = 7.0;
const vec2 OFFSET = vec2(0.0);
const vec4 COLOUR_1 = vec4(0.871, 0.267, 0.231, 1.0);
const vec4 COLOUR_2 = vec4(0.0, 0.42, 0.706, 1.0);
const vec4 COLOUR_3 = vec4(0.086, 0.137, 0.145, 1.0);
const float CONTRAST = 5.0;
const float LIGTHING = 0.4;
const float SPIN_AMOUNT = 0.25;
const float PIXEL_FILTER = 10000.0;
const float SPIN_EASE = 1.0;
const float PI = 3.14159265359;
const int IS_ROTATE = 0;
const float ALPHA = 0.35;

vec4 effect(vec2 screenSize, vec2 screen_coords) {
    float pixel_size = length(screenSize.xy) / PIXEL_FILTER;
    vec2 uv = (floor(screen_coords.xy*(1./pixel_size))*pixel_size - 0.5*screenSize.xy)/length(screenSize.xy) - OFFSET;
    float uv_len = length(uv);

    float speed = (SPIN_ROTATION*SPIN_EASE*0.2);
    if(IS_ROTATE == 1){
        speed = time * speed;
    }
    speed += 302.2;
    float new_pixel_angle = atan(uv.y, uv.x) + speed - SPIN_EASE*20.*(1.*SPIN_AMOUNT*uv_len + (1. - 1.*SPIN_AMOUNT));
    vec2 mid = (screenSize.xy/length(screenSize.xy))/2.;
    uv = (vec2((uv_len * cos(new_pixel_angle) + mid.x), (uv_len * sin(new_pixel_angle) + mid.y)) - mid);

    uv *= 30.;
    speed = time*(SPIN_SPEED);
    vec2 uv2 = vec2(uv.x+uv.y);

    for(int i=0; i < 5; i++) {
        uv2 += sin(max(uv.x, uv.y)) + uv;
        uv  += 0.5*vec2(cos(5.1123314 + 0.353*uv2.y + speed*0.131121),sin(uv2.x - 0.113*speed));
        uv  -= 1.0*cos(uv.x + uv.y) - 1.0*sin(uv.x*0.711 - uv.y);
    }

    float contrast_mod = (0.25*CONTRAST + 0.5*SPIN_AMOUNT + 1.2);
    float paint_res = min(2., max(0.,length(uv)*(0.035)*contrast_mod));
    float c1p = max(0.,1. - contrast_mod*abs(1.-paint_res));
    float c2p = max(0.,1. - contrast_mod*abs(paint_res));
    float c3p = 1. - min(1., c1p + c2p);
    float light = (LIGTHING - 0.2)*max(c1p*5. - 4., 0.) + LIGTHING*max(c2p*5. - 4., 0.);
    return (0.3/CONTRAST)*COLOUR_1 + (1. - 0.3/CONTRAST)*(COLOUR_1*c1p + COLOUR_2*c2p + vec4(c3p*COLOUR_3.rgb, c3p*COLOUR_1.a)) + light;
}

void main(void) {
    vec2 uv = gl_FragCoord.xy/resolution.xy;

    gl_FragColor = effect(resolution.xy, uv * resolution.xy);
    gl_FragColor.a = ALPHA;
}