
uniform float time;
uniform vec2 u_center;
uniform float u_radius;
uniform float u_duration;


void main( void ) {
       float dist = distance(gl_FragCoord.xy, u_center);

       if(dist > u_radius) {
              discard;
       }

       // Progression normalisée de l’explosion [0..1]
       float prog = clamp(time / u_duration, 0.0, 1.0);

       // Rayon courant = du plus petit (0) au plus grand (u_radius)
       float currR = u_radius * prog;



       float t = dist / currR;

       // Anneau de bord : on veut un ring fin autour de t≈1.0
       // smoothstep pour lisser l’intérieur et l’extérieur du ring
       float edge0 = 0.9;
       float edge1 = 1.0;
       float ring = smoothstep(edge1, edge0, t) - smoothstep(edge1, 1.05, t);

       // Remplissage intérieur : dégrade de opaque à transparent vers la frontière
       float fill = 1.0 - smoothstep(0.0, 1.0, t);

       // On combine ring + fill, et on fait disparaître le tout à la fin
       float alpha = (ring + fill * 0.5) * (1.0 - prog);

       // Couleur : on part d’un jaune chaud au centre, devient rouge vers l’extérieur
       vec3 innerColor = vec3(1.0, 0.9, 0.5);
       vec3 outerColor = vec3(1.0, 0.0, 0.0);
       vec3 color = mix(innerColor, outerColor, t);

       gl_FragColor = vec4(color, alpha);
}