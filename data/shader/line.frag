#ifdef GL_ES
precision mediump float;
#endif

varying float v_edge;
varying vec2 v_position;

void main() {
    float x = gl_FragCoord.x;
    float y = gl_FragCoord.y;

    float alpha = 0.6;

    // distance from 0.9, 0.0
    float d1 = abs(v_position.x) - 0.9;
    if (d1 < 0) {
        d1 = 0;
    }
    float d2 = abs(v_position.y);

    float d = sqrt(d1*d1+d2*d2);
    if (d>0.1) {
        alpha = 0.4 - ((d - 0.1) * 10);
        if (alpha < 0) {
            alpha = 0;
        }
    }
    //alpha = alpha - (0.6 * ((abs(v_position.x) - 0.9) * 10));

    gl_FragColor = vec4(x / 1000, 0.6 + y / 1000, 0.4, alpha);
}
