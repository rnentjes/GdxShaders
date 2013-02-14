#ifdef GL_ES
precision mediump float;
#endif

varying vec3 v_color;
varying vec2 v_position;

void main() {
    float x = gl_FragCoord.x;
    float y = gl_FragCoord.y;

    float distance = v_position.x * v_position.x + v_position.y * v_position.y;

    float alpha = 1 - smoothstep(0, 1, distance);
    //float alpha = smoothstep(0, 2, 2-(distance*2));
    alpha = alpha * 0.1;

    gl_FragColor = vec4(v_color, alpha);
}
