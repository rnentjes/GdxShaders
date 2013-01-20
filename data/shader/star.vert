attribute vec4 a_position;

uniform mat4 u_projectionViewMatrix;
uniform vec3 star_color;
//uniform float star_alpha;

varying vec2 v_position;
varying vec3 v_star_color;
varying float v_star_alpha;

void main() {
    gl_Position = u_projectionViewMatrix * a_position;
    v_position = a_position.xy;
    v_star_color = star_color;
    //v_star_alpha = star_alpha;
}
