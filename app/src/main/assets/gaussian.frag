precision mediump float;

uniform sampler2D texture;
uniform float     weight[10];
uniform bool      horizontal;
varying vec2      uv;

void main(void){
    float tFrag = 1.0 / 256.0;
    float tw = 160./200.;
    float sw = 240./200.;
    vec2  fc = uv;
    vec4  destColor = vec4(0.0);

    if(horizontal){
        fc = gl_FragCoord.st;
        destColor += texture2D(texture, (fc + vec2(-9.0, 0.0) * sw) * tFrag).rgba * weight[9];
        destColor += texture2D(texture, (fc + vec2(-8.0, 0.0) * sw) * tFrag).rgba * weight[8];
        destColor += texture2D(texture, (fc + vec2(-7.0, 0.0) * sw) * tFrag).rgba * weight[7];
        destColor += texture2D(texture, (fc + vec2(-6.0, 0.0) * sw) * tFrag).rgba * weight[6];
        destColor += texture2D(texture, (fc + vec2(-5.0, 0.0) * sw) * tFrag).rgba * weight[5];
        destColor += texture2D(texture, (fc + vec2(-4.0, 0.0) * sw) * tFrag).rgba * weight[4];
        destColor += texture2D(texture, (fc + vec2(-3.0, 0.0) * sw) * tFrag).rgba * weight[3];
        destColor += texture2D(texture, (fc + vec2(-2.0, 0.0) * sw) * tFrag).rgba * weight[2];
        destColor += texture2D(texture, (fc + vec2(-1.0, 0.0) * sw) * tFrag).rgba * weight[1];
        destColor += texture2D(texture, (fc + vec2( 0.0, 0.0) * sw) * tFrag).rgba * weight[0];
        destColor += texture2D(texture, (fc + vec2( 1.0, 0.0) * sw) * tFrag).rgba * weight[1];
        destColor += texture2D(texture, (fc + vec2( 2.0, 0.0) * sw) * tFrag).rgba * weight[2];
        destColor += texture2D(texture, (fc + vec2( 3.0, 0.0) * sw) * tFrag).rgba * weight[3];
        destColor += texture2D(texture, (fc + vec2( 4.0, 0.0) * sw) * tFrag).rgba * weight[4];
        destColor += texture2D(texture, (fc + vec2( 5.0, 0.0) * sw) * tFrag).rgba * weight[5];
        destColor += texture2D(texture, (fc + vec2( 6.0, 0.0) * sw) * tFrag).rgba * weight[6];
        destColor += texture2D(texture, (fc + vec2( 7.0, 0.0) * sw) * tFrag).rgba * weight[7];
        destColor += texture2D(texture, (fc + vec2( 8.0, 0.0) * sw) * tFrag).rgba * weight[8];
        destColor += texture2D(texture, (fc + vec2( 9.0, 0.0) * sw) * tFrag).rgba * weight[9];
    }else{
        fc = gl_FragCoord.st;
        destColor += texture2D(texture, (fc + vec2(0.0, -9.0) * tw) * tFrag).rgba * weight[9];
        destColor += texture2D(texture, (fc + vec2(0.0, -8.0) * tw) * tFrag).rgba * weight[8];
        destColor += texture2D(texture, (fc + vec2(0.0, -7.0) * tw) * tFrag).rgba * weight[7];
        destColor += texture2D(texture, (fc + vec2(0.0, -6.0) * tw) * tFrag).rgba * weight[6];
        destColor += texture2D(texture, (fc + vec2(0.0, -5.0) * tw) * tFrag).rgba * weight[5];
        destColor += texture2D(texture, (fc + vec2(0.0, -4.0) * tw) * tFrag).rgba * weight[4];
        destColor += texture2D(texture, (fc + vec2(0.0, -3.0) * tw) * tFrag).rgba * weight[3];
        destColor += texture2D(texture, (fc + vec2(0.0, -2.0) * tw) * tFrag).rgba * weight[2];
        destColor += texture2D(texture, (fc + vec2(0.0, -1.0) * tw) * tFrag).rgba * weight[1];
        destColor += texture2D(texture, (fc + vec2(0.0,  0.0) * tw) * tFrag).rgba * weight[0];
        destColor += texture2D(texture, (fc + vec2(0.0,  1.0) * tw) * tFrag).rgba * weight[1];
        destColor += texture2D(texture, (fc + vec2(0.0,  2.0) * tw) * tFrag).rgba * weight[2];
        destColor += texture2D(texture, (fc + vec2(0.0,  3.0) * tw) * tFrag).rgba * weight[3];
        destColor += texture2D(texture, (fc + vec2(0.0,  4.0) * tw) * tFrag).rgba * weight[4];
        destColor += texture2D(texture, (fc + vec2(0.0,  5.0) * tw) * tFrag).rgba * weight[5];
        destColor += texture2D(texture, (fc + vec2(0.0,  6.0) * tw) * tFrag).rgba * weight[6];
        destColor += texture2D(texture, (fc + vec2(0.0,  7.0) * tw) * tFrag).rgba * weight[7];
        destColor += texture2D(texture, (fc + vec2(0.0,  8.0) * tw) * tFrag).rgba * weight[8];
        destColor += texture2D(texture, (fc + vec2(0.0,  9.0) * tw) * tFrag).rgba * weight[9];
    }

    gl_FragColor = destColor;
}