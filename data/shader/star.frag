#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_position;
varying vec3 v_star_color;
varying float v_star_alpha;

void main() {
    float x = gl_FragCoord.x;
    float y = gl_FragCoord.y;

    float distance = v_position.x * v_position.x + v_position.y * v_position.y;

    //float alpha = 1 - smoothstep(0, 1, distance);
    float alpha = smoothstep(0, 2, 2-(distance*2));
     //* v_star_alpha;

    gl_FragColor = vec4(v_star_color, alpha);
}
