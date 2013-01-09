#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
varying float v_generic;
uniform float u_time;

void main() {
    float dummy = u_time ;
    float red = v_generic; //abs(cos(17f*u_time));
    float blue = abs(sin(13*u_time*v_texCoords.x*v_texCoords.x));
    float green = abs(-sin(11*u_time*v_texCoords.y*v_texCoords.y));

    gl_FragColor = vec4(red,green,blue,v_generic);
}
