#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_position;

void main() {
    float distance = v_position.x * v_position.x + v_position.y * v_position.y;

    float red = 0.0 + (smoothstep(0.9, 0.95, distance) * 1.0);
    float green = 0.0 + (smoothstep(0.9, 0.95, distance) * 0.2);
    float blue = 0.0; // + (smoothstep(0.9, 0.95, distance) * 0.9);

    float alpha = 1 - smoothstep(0.95, 1, distance);

    gl_FragColor = vec4(red, green, blue, alpha);
}
