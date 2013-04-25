    attribute vec3 aVertexPosition;

    uniform mat4 M_Matrix;
    uniform mat4 V_Matrix;
    uniform mat4 P_Matrix;
    uniform vec4 aColor;

    varying color;

    void main(void) {
        mat4 mvp = P_Matrix * V_Matrix * M_Matrix;
        gl_Position = mvp * vec4(aVertexPosition, 1.0);
        color = aColor;
    }
