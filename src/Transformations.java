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
    private static Vector dotHomogeneous(double[][] mData, Vector vector) {
        int dim = vector.getDim();
        if (mData.length != dim + 1) {
            throw new IllegalArgumentException("A matriz de transformação deve ter dimensão " + (dim + 1) + "x" + (dim + 1) + ".");
        }

        // 1. Cartesiano -> Coordenada Homogênea ((dim + 1) x 1)
        double[][] vData = new double[dim + 1][1];
        for (int i = 1; i <= dim; i++) {
            vData[i - 1][0] = vector.get(i);
        }
        vData[dim][0] = 1.0;

        // 2. Multiplicação da Matriz de Transformação pelo Vetor Homogêneo
        Matrix m = new Matrix(dim + 1, dim + 1, mData);
        Matrix resHomogeneous = LinearAlgebra.dot(m, new Matrix(dim + 1, 1, vData));

        // 3. Coordenada Homogênea -> Cartesiano
        double w = resHomogeneous.get(dim + 1, 1);
        double[] resCartesian = new double[dim];
        for (int i = 1; i <= dim; i++) {
            resCartesian[i - 1] = resHomogeneous.get(i, 1) / w;
        }

        return new Vector(resCartesian, dim);
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

    public static Vector translate2D(Vector vector, double dx, double dy) {
        checkDim(vector, 2, "translate2D");

        // Matriz de Translação 2D (3x3)
        double[][] mData = {
            {1.0, 0.0, dx},
            {0.0, 1.0, dy},
            {0.0, 0.0, 1.0}
        };

        return dotHomogeneous(mData, vector);
    }

    public static Vector translate3D(Vector vector, double dx, double dy, double dz) {
        checkDim(vector, 3, "translate3D");

        // Matriz de Translação 3D (4x4)
        double[][] mData = {
            {1.0, 0.0, 0.0, dx},
            {0.0, 1.0, 0.0, dy},
            {0.0, 0.0, 1.0, dz},
            {0.0, 0.0, 0.0, 1.0}
        };

        return dotHomogeneous(mData, vector);
    }

    // ==========================================
    // CISALHAMENTO X ROTAÇÃO
    // Gustavo
    // ==========================================

    public static Vector shearing(Vector vector, double kx, double ky) {
        checkDim(vector, 2, "shearing");

        // Matriz de Cisalhamento 2D (3x3)
        double[][] mData = {
                {1.0, ky, 0.0},
                {kx, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        };

        return dotHomogeneous(mData, vector);
    }

    public static Vector rotation2D(Vector vector, double angle) {
        checkDim(vector, 2, "rotation2D");

        double rad = Math.toRadians(angle);
        double c = Math.cos(rad), s = Math.sin(rad);

        //Matrix de Rotação 2D (3x3)
        double[][] mData = {
                {c, -s, 0.0},
                {s, c, 0.0},
                {0.0, 0.0, 1.0}
        };

        return dotHomogeneous(mData, vector);
    }

    public static Vector rotation3DX(Vector vector, double angle) {
        checkDim(vector, 3, "rotation3DX");

        double rad = Math.toRadians(angle);
        double c = Math.cos(rad), s = Math.sin(rad);

        //Matrix de Rotação 3DX (4x4)
        double[][] mData = {
                {1.0, 0.0, 0.0, 0.0},
                {0.0, c, -s, 0.0},
                {0.0, s, c, 0.0},
                {0.0, 0.0, 0.0, 1.0}
        };

        return dotHomogeneous(mData, vector);
    }

    public static Vector rotation3DY(Vector vector, double angle) {
        checkDim(vector, 3, "rotation3DY");

        double rad = Math.toRadians(angle);
        double c = Math.cos(rad), s = Math.sin(rad);

        //Matrix de Rotação 3DY (4x4)
        double[][] mData = {
                {c, 0.0, s, 0.0},
                {0.0, 1.0, 0.0, 0.0},
                {-s, 0.0, c, 0.0},
                {0.0, 0.0, 0.0, 1.0}
        };

        return dotHomogeneous(mData, vector);
    }

    public static Vector rotation3DZ(Vector vector, double angle) {
        checkDim(vector, 3, "rotation3DZ");

        double rad = Math.toRadians(angle);
        double c = Math.cos(rad), s = Math.sin(rad);

        //Matrix de Rotação 3DZ (4x4)
        double[][] mData = {
                {c, -s, 0.0, 0.0},
                {s, c, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 1.0}
        };

        return dotHomogeneous(mData, vector);
    }

    // ==========================================
    // PROJEÇÃO
    // Enzo
    // ==========================================

    public static Vector projection2DX(Vector vector) {
        checkDim(vector, 2, "projection2DX");

        // Matriz de Projeção 2DX (3x3)
        double[][] mData = {
            {1.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 1.0}
        };

        return dotHomogeneous(mData, vector);
    }

    public static Vector projection2DY(Vector vector) {
        checkDim(vector, 2, "projection2DY");

        // Matriz de Projeção 2DY (3x3)
        double[][] mData = {
            {0.0, 0.0, 0.0},
            {0.0, 1.0, 0.0},
            {0.0, 0.0, 1.0}
        };

        return dotHomogeneous(mData, vector);
    }

    public static Vector projection3DX(Vector vector) {
        checkDim(vector, 3, "projection3DX");

        // Matriz de Projeção 3DX (4x4)
        double[][] mData = {
            {1.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 1.0}
        };

        return dotHomogeneous(mData, vector);
    }

    public static Vector projection3DY(Vector vector) {
        checkDim(vector, 3, "projection3DY");

        // Matriz de Projeção 3DY (4x4)
        double[][] mData = {
            {0.0, 0.0, 0.0, 0.0},
            {0.0, 1.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 1.0}
        };

        return dotHomogeneous(mData, vector);
    }

    public static Vector projection3DZ(Vector vector) {
        checkDim(vector, 3, "projection3DZ");

        // Matriz de Projeção 3DZ (4x4)
        double[][] mData = {
            {0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 1.0, 0.0},
            {0.0, 0.0, 0.0, 1.0}
        };

        return dotHomogeneous(mData, vector);
    }
}
