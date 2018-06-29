attribute vec4 vPosition;
attribute vec2 vUv;
uniform mat4 viewportMatrix;
uniform mat4 transMatrix;
uniform mat4 uvTransMatrix;
varying vec2 uv;
void main() {
  uv = (uvTransMatrix * vec4(vUv, 0, 1)).xy;
  gl_Position = viewportMatrix * transMatrix * vPosition;
}
