precision mediump float;

uniform sampler2D texture;
varying vec2 uv;
void main() {
  float tFrag = 1.0 / 128.0;
  vec2 fc = gl_FragCoord.st;
  float r = texture2D(texture, uv + vec2(-1.0, 0.0) * tFrag).r;
  float g = texture2D(texture, uv).g;
  float b = texture2D(texture, uv + vec2(1.0, 0.0) * tFrag).b;
  float a = texture2D(texture, uv).a;
  gl_FragColor = vec4(r, g, b, a);
}
