//uniform float u_time;

attribute vec4 a_position;
attribute float a_generic;

varying vec2 v_texCoords;
varying float v_generic;

uniform float u_time;

void main() {
    gl_Position = a_position;
    v_texCoords = a_position.xy;

    v_texCoords.x = v_texCoords.x + abs(0.1 * sin(u_time));
    v_texCoords.y = v_texCoords.x + abs(0.1 * cos(u_time));

    v_generic = a_generic*abs(sin(u_time));
}
