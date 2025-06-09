
// Entrées depuis le vertex‐shader / Gdx2d
in vec4 vColor;
in vec2 vTexCoord;

// Texture de la balle
uniform sampler2D texture0;

// Uniforms de ton effet électrique
uniform float time;       // temps en secondes
uniform float u_radius;   // rayon (0.5 = moitié de la sprite)
uniform vec2  u_center;   // centre de l’effet (en pixels)

/** Bruit 1D simple à base de sin(). */
float noise(float x) {
    return fract(sin(x * 12.9898 + 78.233) * 43758.5453123);
}

void main(void) {
    // 1) Récupérer la couleur de base de la balle
    vec4 base = vec4(vColor);

    // Si la balle est transparente ici, on reste en base
    if (base.a < 0.01) {
        gl_FragColor = base;
        return;
    }

    // 2) Calculer la position et la distance normalisée
    vec2 fragPos = gl_FragCoord.xy;
    vec2 uv      = (fragPos - u_center) / u_radius;
    float dist   = length(uv);

    // 3) Générer un bruit animé en fonction de l’angle et du temps
    float ang = atan(uv.y, uv.x);
    float n   = noise(ang * 8.0 + time * 5.0);

    // 4) Créer l’anneau de « foudre » et le remplissage intérieur
    float edge = smoothstep(1.0, 0.9, dist + n * 0.2);
    float fill = 1.0 - smoothstep(0.0, 1.0, dist);

    // 5) Intensité finale : on combine les deux
    float intensity = max(fill * 0.5, edge);

    // 6) Si on est en dehors du cercle normalisé (dist > 1), on affiche la base
    if (dist > 1.0) {
        gl_FragColor = base;
    } else {
        // 7) Sinon on dessine la foudre en bleu‐clair
        vec3 stormColor = vec3(0.6, 0.8, 1.0) * (0.7 + 0.3 * n);
        gl_FragColor = vec4(stormColor * intensity, intensity);
    }
}
