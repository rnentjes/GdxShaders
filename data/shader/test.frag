#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_position;

void main() {
    float x = gl_FragCoord.x;
    float y = gl_FragCoord.y;
    float z = gl_FragCoord.z;

    float distance = v_position.x * v_position.x + v_position.y * v_position.y;

    float red = abs(sin(x*100));
    float green = abs(sin(y*100));
    float blue = abs(sin(x*100 + y*100));

    gl_FragColor = vec4(red, green, blue, 0.65);
}
