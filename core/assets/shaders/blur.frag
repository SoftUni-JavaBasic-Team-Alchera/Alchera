varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float resolution;
uniform float radius;
uniform vec2 dir;
uniform float fade;
uniform bool u_grayscale;
uniform bool u_blur;

vec4 blur(){
    vec4 sum = vec4(0.0);

    vec2 tc = v_texCoords;
    float blur = radius/resolution;

    sum += texture2D(u_texture, vec2(tc.x - 4.0*blur*dir.x, tc.y - 4.0*blur*dir.y)) * 0.016;
    sum += texture2D(u_texture, vec2(tc.x - 3.0*blur*dir.x, tc.y - 3.0*blur*dir.y)) * 0.054;
    sum += texture2D(u_texture, vec2(tc.x - 2.0*blur*dir.x, tc.y - 2.0*blur*dir.y)) * 0.121;
    sum += texture2D(u_texture, vec2(tc.x - 1.0*blur*dir.x, tc.y - 1.0*blur*dir.y)) * 0.194;

    sum += texture2D(u_texture, vec2(tc.x, tc.y)) * 0.227;

    sum += texture2D(u_texture, vec2(tc.x + 1.0*blur*dir.x, tc.y + 1.0*blur*dir.y)) * 0.194;
    sum += texture2D(u_texture, vec2(tc.x + 2.0*blur*dir.x, tc.y + 2.0*blur*dir.y)) * 0.121;
    sum += texture2D(u_texture, vec2(tc.x + 3.0*blur*dir.x, tc.y + 3.0*blur*dir.y)) * 0.054;
    sum += texture2D(u_texture, vec2(tc.x + 4.0*blur*dir.x, tc.y + 4.0*blur*dir.y)) * 0.016;

    return sum;
}

vec3 blurRGB(){
    return blur().rgb;
}

void main() {

    if(u_grayscale){
        vec4 tc = texture2D(u_texture,v_texCoords);
        vec3 color = (u_blur ? blurRGB() : tc.rgb) * fade;
        float grayscale = dot(color,vec3(0.2f,0.5f,0.1f));
        gl_FragColor = v_color * vec4(grayscale,grayscale,grayscale,tc.a);
    }else{
        vec4 tc = texture2D(u_texture,v_texCoords);
        vec3 color = (u_blur ? blurRGB() : tc.rgb) * fade;
        gl_FragColor = v_color * vec4(color,tc.a);
    }
}

