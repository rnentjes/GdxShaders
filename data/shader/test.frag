#ifdef GL_ES
precision mediump float;
#endif

varying float v_edge;
varying vec2 v_position;

void main() {
    float x = gl_FragCoord.x;
    float y = gl_FragCoord.y;
    float z = gl_FragCoord.z;

    float distance = v_position.x * v_position.x + v_position.y * v_position.y;

    float alpha = smoothstep(0, 2, 2-(distance*2));

    gl_FragColor = vec4(alpha, abs(y), abs(z), 1.0);
}
