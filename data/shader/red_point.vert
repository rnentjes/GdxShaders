attribute vec4 a_position;

uniform mat4 u_projectionViewMatrix;

varying vec2 v_position;

void main() {
    gl_Position = u_projectionViewMatrix * a_position;
    v_position = a_position.xy;
}
