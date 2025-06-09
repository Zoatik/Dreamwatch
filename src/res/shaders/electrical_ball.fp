
// Entrées depuis le vertex‐shader / Gdx2d
in vec4 vColor;
in vec2 vTexCoord;

// Texture de la balle
//uniform sampler2D texture0;

// Uniforms de ton effet électrique
uniform float time;       // temps en secondes
uniform float u_radius;   // rayon (0.5 = moitié de la sprite)
uniform vec2  u_center;   // centre de l’effet (en pixels)

/** Bruit 1D simple à base de sin(). */
float noise(float x) {
    return fract(sin(x * 12.9898 + 78.233) * 43758.5453123);
}

void main(void) {

    float dist = distance(gl_FragCoord.xy, u_center);

    if(dist > u_radius) {
        discard;
    }

    // 1) Récupérer la couleur de base de la balle
    vec4 base = vec4(vColor);


    // 2) Calculer la position et la distance normalisée
    vec2 uv      = (gl_FragCoord.xy - u_center) / u_radius;
    //float dist   = length(uv);

    // 3) Générer un bruit animé en fonction de l’angle et du temps
    float ang = atan(uv.y, uv.x);
    float n   = noise(ang * 8.0 + time * 5.0);

    // 4) Créer l’anneau de « foudre » et le remplissage intérieur
    float edge = 1.5 *smoothstep(u_radius, u_radius*0.94, dist);
    float fill = smoothstep(u_radius, 0.0, dist * 0.01);

    // 5) Intensité finale : on combine les deux
    float intensity = max(fill * 0.5, edge);


    // 7) Sinon on dessine la foudre en bleu‐clair
    vec3 stormColor = vec3(0.6, 0.8, 1.0) * (0.7 + 0.3 * n);
    gl_FragColor = vec4(stormColor * intensity, intensity);

}
