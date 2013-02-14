attribute vec4 a_position;

uniform mat4 u_projectionViewMatrix;
uniform vec3 u_color;

varying vec2 v_position;
varying vec3 v_color;

void main() {
    gl_Position = u_projectionViewMatrix * a_position;
    v_position = a_position.xy;
    v_color = u_color;
}
