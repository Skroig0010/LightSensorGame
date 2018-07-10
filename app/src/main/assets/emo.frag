precision mediump float;

uniform sampler2D texture;
varying vec2 uv;
void main() {
  float tFlag = 1. / 128.;
  vec2 fc = gl_FragCoord.st;
  /* vec2 uv2 = uv - vec2(0.5, 0.5);
  uv2 /= 3. * (1. - distance(uv2, vec2(0., 0.)));
  uv2 += vec2(0.5, 0.5);*/
  float modx = mod(fc.x, 9.);
  float mody = mod(fc.y, 6.);
  float v = (mody < 3.)? 1. : 0.8;
  float r = (modx < 3.) ? texture2D(texture, uv + vec2(-6., 0.) * tFlag ).r * v : 0.;
  float g = (modx >= 3. && modx < 6.) ? texture2D(texture, uv).g * v : 0.;
  float b = (modx >= 6. && modx < 9.) ? texture2D(texture, uv + vec2(6., 0.) * tFlag).b * v : 0.;
  float a = texture2D(texture, uv).a;
  gl_FragColor = vec4(r, g, b, a);
}
