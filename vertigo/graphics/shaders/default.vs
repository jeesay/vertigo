    attribute vec3 aVertexPosition;

    uniform mat4 M__Matrix;
    uniform mat4 V_Matrix;
    uniform mat4 P_Matrix;

    void main(void) {
        mat4 mvp = P_Matrix * V_Matrix * M_Matrix;
        gl_Position = mvp * vec4(aVertexPosition, 1.0);
    }
