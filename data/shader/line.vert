attribute vec4 a_position;
attribute float a_edge;

uniform mat4 u_projectionViewMatrix;

varying float v_edge;
varying vec2 v_position;

void main() {
    gl_Position = a_position * u_projectionViewMatrix;
    v_position = a_position.xy;
    v_edge = a_edge;
}
