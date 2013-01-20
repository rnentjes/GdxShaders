#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_position;
varying vec3 v_cube_color;

void main() {
    gl_FragColor = vec4(v_cube_color, 1);
}
