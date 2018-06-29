precision mediump float;
uniform sampler2D texture;
uniform float lightValue;
uniform float darkValue;
uniform float isBright;
varying vec2 uv;
void main() {
  vec3 x = vec3(isBright * lightValue + (1. - isBright) * darkValue);
  gl_FragColor = vec4(x, texture2D(texture, uv).a);
}
