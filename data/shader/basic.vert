attribute vec4 a_position;
attribute float a_generic;

uniform float u_time;

varying vec2 v_texCoords;
varying float v_generic;

void main() {
    gl_Position = a_position;
    v_texCoords = a_position.xy;
    v_generic = a_generic;
}
