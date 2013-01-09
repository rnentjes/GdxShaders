#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
varying float v_generic;
uniform float u_time;

void main() {
    float dummy = u_time ;
    float red = abs(cos(17*u_time));
    float blue = abs(sin(u_time*v_texCoords.x));
    float green = abs(cos(u_time*v_texCoords.y));

    gl_FragColor = vec4(red,green,blue,0.75f);
}
