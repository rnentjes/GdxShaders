attribute vec4 a_position;

uniform mat4 u_projectionViewMatrix;
uniform vec3 cube_color;

varying vec2 v_position;
varying vec3 v_cube_color;

void main() {
    gl_Position = u_projectionViewMatrix * a_position;
    v_position = a_position.xy;
    v_cube_color = cube_color;
}
