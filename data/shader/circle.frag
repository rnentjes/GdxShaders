#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_position;

void main() {
    float distance = v_position.x * v_position.x + v_position.y * v_position.y;

    float alpha = 1 - smoothstep(0.8, 1, distance);

    gl_FragColor = vec4(0.8, 0.2, 0.1, alpha * 0.5);
}
