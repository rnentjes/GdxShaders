#ifdef GL_ES
precision mediump float;
#endif

varying float v_edge;
varying vec2 v_position;

void main() {
    float x = gl_FragCoord.x;
    float y = gl_FragCoord.y;

    float distance = v_position.x * v_position.x + v_position.y * v_position.y;

    float alpha = 0;

    if (distance <= 1) {
        alpha = (1 - distance) * (1 - distance);
    }

    gl_FragColor = vec4(0.2, 0.5, 1.0, alpha);
}
