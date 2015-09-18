varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_fade;
uniform bool u_grayscale;

void main()
{
    if(u_grayscale){
        vec4 tc = texture2D(u_texture,v_texCoords);
        vec3 color = tc.rgb * u_fade;
        float grayscale = dot(color.rgb,vec3(0.2f,0.5f,0.1f));
        gl_FragColor = v_color * vec4(grayscale,grayscale,grayscale,tc.a);
    }else{
        vec4 tc = texture2D(u_texture,v_texCoords);
        vec3 color = tc.rgb * u_fade;
        gl_FragColor = v_color * vec4(color,tc.a);
    }
}