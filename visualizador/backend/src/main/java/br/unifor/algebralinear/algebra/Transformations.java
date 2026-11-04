package br.unifor.algebralinear.algebra;

public class Transformations {

    // ==========================================
    // VECTOR -> MATRIX  |  MATRIX -> VECTOR
    // ==========================================

    private static Matrix toMatrix(Vector vector) {
        double[][] newMatrix = new double[vector.getDim()][1];
        for (int i = 0; i < vector.getDim(); i++) {
            newMatrix[i][0] = vector.get(i+1);
        }

        return new Matrix(vector.getDim(), 1 ,newMatrix);
    }

    private static Vector toVector(Matrix matrix) {
        double[] newVector = new double[matrix.getRows()];
        for (int i = 0; i < matrix.getRows(); i++) {
            newVector[i] = matrix.get(i+1,1);
        }

        return new Vector(newVector, matrix.getRows());
    }

    // ==========================================
    // COORDENADAS HOMOGÊNEAS
    // Alisson
    // ==========================================
    // Passo 1: Cartesiano -> Coordenada Homogênea ((dim + 1) x 1)
    // Ex.: (x, y) vira a matriz-coluna [x, y, 1]
    public static Matrix toHomogeneous(Vector vector) {
        int dim = vector.getDim();
        double[][] vData = new double[dim + 1][1];
        for (int i = 1; i <= dim; i++) {
            vData[i - 1][0] = vector.get(i);
        }
        vData[dim][0] = 1.0;

        return new Matrix(dim + 1, 1, vData);
    }

    // Passo 3: Coordenada Homogênea -> Cartesiano
    // Ex.: [x, y, w] vira (x / w, y / w)
    public static Vector toCartesian(Matrix homogeneous) {
        int dim = homogeneous.getRows() - 1;
        double w = homogeneous.get(dim + 1, 1);
        if (w == 0) {
            throw new IllegalArgumentException("w = 0: o ponto está no infinito e não tem coordenada cartesiana.");
        }

        double[] resCartesian = new double[dim];
        for (int i = 1; i <= dim; i++) {
            resCartesian[i - 1] = homogeneous.get(i, 1) / w;
        }

        return new Vector(resCartesian, dim);
    }

    public static Vector dotHomogeneous(double[][] mData, Vector vector) {
        int dim = vector.getDim();
        if (mData.length != dim + 1) {
            throw new IllegalArgumentException("A matriz de transformação deve ter dimensão " + (dim + 1) + "x" + (dim + 1) + ".");
        }

        // 1. Cartesiano -> Coordenada Homogênea ((dim + 1) x 1)
        Matrix vHomogeneous = toHomogeneous(vector);

        // 2. Multiplicação da Matriz de Transformação pelo Vetor Homogêneo
        Matrix m = new Matrix(dim + 1, dim + 1, mData);
        Matrix resHomogeneous = LinearAlgebra.dot(m, vHomogeneous);

        // 3. Coordenada Homogênea -> Cartesiano
        return toCartesian(resHomogeneous);
    }

    private static void checkDim(Vector vector, int dim, String method) {
        if (vector.getDim() != dim) {
            throw new IllegalArgumentException("O vetor de entrada precisa ter dimensão " + dim + " para " + method + ".");
        }
    }

    // ==========================================
    // TRANSLAÇÃO
    // Alisson
    // ==========================================

    public static double[][] translate2DMatrix(double dx, double dy) {
        // Matriz de Translação 2D (3x3)
        return new double[][] {
            {1.0, 0.0, dx},
            {0.0, 1.0, dy},
            {0.0, 0.0, 1.0}
        };
    }

    public static Vector translate2D(Vector vector, double dx, double dy) {
        checkDim(vector, 2, "translate2D");

        return dotHomogeneous(translate2DMatrix(dx, dy), vector);
    }

    public static double[][] translate3DMatrix(double dx, double dy, double dz) {
        // Matriz de Translação 3D (4x4)
        return new double[][] {
            {1.0, 0.0, 0.0, dx},
            {0.0, 1.0, 0.0, dy},
            {0.0, 0.0, 1.0, dz},
            {0.0, 0.0, 0.0, 1.0}
        };
    }

    public static Vector translate3D(Vector vector, double dx, double dy, double dz) {
        checkDim(vector, 3, "translate3D");

        return dotHomogeneous(translate3DMatrix(dx, dy, dz), vector);
    }

    // ==========================================
    // CISALHAMENTO X ROTAÇÃO
    // Gustavo
    // ==========================================

    public static double[][] shearingMatrix(double kx, double ky) {
        // Matriz de Cisalhamento 2D (3x3)
        return new double[][] {
                {1.0, ky, 0.0},
                {kx, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        };
    }

    public static Vector shearing(Vector vector, double kx, double ky) {
        checkDim(vector, 2, "shearing");

        return dotHomogeneous(shearingMatrix(kx, ky), vector);
    }

    public static double[][] rotation2DMatrix(double angle) {
        double rad = Math.toRadians(angle);
        double c = Math.cos(rad), s = Math.sin(rad);

        //Matrix de Rotação 2D (3x3)
        return new double[][] {
                {c, -s, 0.0},
                {s, c, 0.0},
                {0.0, 0.0, 1.0}
        };
    }

    public static Vector rotation2D(Vector vector, double angle) {
        checkDim(vector, 2, "rotation2D");

        return dotHomogeneous(rotation2DMatrix(angle), vector);
    }

    public static double[][] rotation3DXMatrix(double angle) {
        double rad = Math.toRadians(angle);
        double c = Math.cos(rad), s = Math.sin(rad);

        //Matrix de Rotação 3DX (4x4)
        return new double[][] {
                {1.0, 0.0, 0.0, 0.0},
                {0.0, c, -s, 0.0},
                {0.0, s, c, 0.0},
                {0.0, 0.0, 0.0, 1.0}
        };
    }

    public static Vector rotation3DX(Vector vector, double angle) {
        checkDim(vector, 3, "rotation3DX");

        return dotHomogeneous(rotation3DXMatrix(angle), vector);
    }

    public static double[][] rotation3DYMatrix(double angle) {
        double rad = Math.toRadians(angle);
        double c = Math.cos(rad), s = Math.sin(rad);

        //Matrix de Rotação 3DY (4x4)
        return new double[][] {
                {c, 0.0, s, 0.0},
                {0.0, 1.0, 0.0, 0.0},
                {-s, 0.0, c, 0.0},
                {0.0, 0.0, 0.0, 1.0}
        };
    }

    public static Vector rotation3DY(Vector vector, double angle) {
        checkDim(vector, 3, "rotation3DY");

        return dotHomogeneous(rotation3DYMatrix(angle), vector);
    }

    public static double[][] rotation3DZMatrix(double angle) {
        double rad = Math.toRadians(angle);
        double c = Math.cos(rad), s = Math.sin(rad);

        //Matrix de Rotação 3DZ (4x4)
        return new double[][] {
                {c, -s, 0.0, 0.0},
                {s, c, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 1.0}
        };
    }

    public static Vector rotation3DZ(Vector vector, double angle) {
        checkDim(vector, 3, "rotation3DZ");

        return dotHomogeneous(rotation3DZMatrix(angle), vector);
    }

    // ==========================================
    // PROJEÇÃO
    // Enzo
    // ==========================================

    public static double[][] projection2DXMatrix() {
        // Matriz de Projeção 2DX (3x3)
        return new double[][] {
            {1.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 1.0}
        };
    }

    public static Vector projection2DX(Vector vector) {
        checkDim(vector, 2, "projection2DX");

        return dotHomogeneous(projection2DXMatrix(), vector);
    }

    public static double[][] projection2DYMatrix() {
        // Matriz de Projeção 2DY (3x3)
        return new double[][] {
            {0.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        };
    }

    public static Vector projection2DY(Vector vector) {
        checkDim(vector, 2, "projection2DY");

        return dotHomogeneous(projection2DYMatrix(), vector);
    }

    public static double[][] projection3DXMatrix() {
        // Matriz de Projeção 3DX (4x4)
        return new double[][] {
            {1.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 1.0}
        };
    }

    public static Vector projection3DX(Vector vector) {
        checkDim(vector, 3, "projection3DX");

        return dotHomogeneous(projection3DXMatrix(), vector);
    }

    public static double[][] projection3DYMatrix() {
        // Matriz de Projeção 3DY (4x4)
        return new double[][] {
            {0.0, 0.0, 0.0, 0.0},
            {0.0, 1.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 1.0}
        };
    }

    public static Vector projection3DY(Vector vector) {
        checkDim(vector, 3, "projection3DY");

        return dotHomogeneous(projection3DYMatrix(), vector);
    }

    public static double[][] projection3DZMatrix() {
        // Matriz de Projeção 3DZ (4x4)
        return new double[][] {
            {0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 1.0, 0.0},
            {0.0, 0.0, 0.0, 1.0}
        };
    }

    public static Vector projection3DZ(Vector vector) {
        checkDim(vector, 3, "projection3DZ");

        return dotHomogeneous(projection3DZMatrix(), vector);
    }
    // ==========================================
    // REFLEXÃO
    // Arthur
    // ==========================================

    public static double[][] reflection2DXMatrix() {
        // Matriz de Reflexão 2DX (3x3)
        return new double[][] {
            {1.0, 0.0, 0.0},
            {0.0, -1.0, 0.0},
            {0.0, 0.0, 1.0}
        };
    }

    public static Vector reflection2DX(Vector vector) {
        checkDim(vector, 2, "reflection2DX");

        return dotHomogeneous(reflection2DXMatrix(), vector);
    }

    public static double[][] reflection2DYMatrix() {
        // Matriz de Reflexão 2DY (3x3)
        return new double[][] {
            {-1.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        };
    }

    public static Vector reflection2DY(Vector vector) {
        checkDim(vector, 2, "reflection2DY");

        return dotHomogeneous(reflection2DYMatrix(), vector);
    }

    public static double[][] reflection3DXMatrix() {
        // Matriz de Reflexão 3DX (4x4)
        return new double[][] {
            {1.0, 0.0, 0.0, 0.0},
            {0.0, -1.0, 0.0, 0.0},
            {0.0, 0.0, -1.0, 0.0},
            {0.0, 0.0, 0.0, 1.0}
        };
    }

    public static Vector reflection3DX(Vector vector) {
        checkDim(vector, 3, "reflection3DX");

        return dotHomogeneous(reflection3DXMatrix(), vector);
    }

    public static double[][] reflection3DYMatrix() {
        // Matriz de Reflexão 3DY (4x4)
        return new double[][] {
            {-1.0, 0.0, 0.0, 0.0},
            {0.0, 1.0, 0.0, 0.0},
            {0.0, 0.0, -1.0, 0.0},
            {0.0, 0.0, 0.0, 1.0}
        };
    }

    public static Vector reflection3DY(Vector vector) {
        checkDim(vector, 3, "reflection3DY");

        return dotHomogeneous(reflection3DYMatrix(), vector);
    }

    public static double[][] reflection3DZMatrix() {
        // Matriz de Reflexão 3DZ (4x4)
        return new double[][] {
            {-1.0, 0.0, 0.0, 0.0},
            {0.0, -1.0, 0.0, 0.0},
            {0.0, 0.0, 1.0, 0.0},
            {0.0, 0.0, 0.0, 1.0}
        };
    }

    public static Vector reflection3DZ(Vector vector) {
        checkDim(vector, 3, "reflection3DZ");

        return dotHomogeneous(reflection3DZMatrix(), vector);
    }

}
