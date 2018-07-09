precision mediump float;

uniform sampler2D texture;
varying vec2 uv;
void main() {
  vec2 fc = gl_FragCoord.st;
  float modx = mod(fc.x, 9.);
  float mody = mod(fc.y, 4.);
  float v = (mody < 2.)? 1. : 0.7;
  float r = (modx < 3.) ? texture2D(texture, uv).r * v : 0.;
  float g = (modx >= 3. && modx < 6.) ? texture2D(texture, uv).g * v : 0.;
  float b = (modx >= 6. && modx < 9.) ? texture2D(texture, uv).b * v : 0.;
  float a = texture2D(texture, uv).a;
  gl_FragColor = vec4(r, g, b, a);
}
