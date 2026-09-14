public class LinearAlgebra {
    //Alisson
    public static Matrix transpose(Matrix a) {
        int rows = a.getRows();
        int cols = a.getCols();
        double[][] res = new double[cols][rows];

        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                res[j - 1][i - 1] = a.get(i, j);
            }
        }
        return new Matrix(cols, rows, res);
    }


    public static Matrix transpose(Vector a) {
        int dim = a.getDim();
        double[][] res = new double[1][dim];

        for (int i = 1; i <= dim; i++) {
            res[0][i - 1] = a.get(i);
        }
        return new Matrix(1, dim, res);
    }


    public static Matrix sum(Matrix a, Matrix b) {
        if (a.getRows() != b.getRows() || a.getCols() != b.getCols()) {
            throw new IllegalArgumentException("As matrizes devem ter as mesmas dimensões.");
        }

        int rows = a.getRows();
        int cols = a.getCols();
        double[][] res = new double[rows][cols];

        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                res[i - 1][j - 1] = a.get(i, j) + b.get(i, j);
            }
        }
        return new Matrix(rows, cols, res);
    }

    public static Vector sum(Vector a, Vector b) {
        if (a.getDim() != b.getDim()) {
            throw new IllegalArgumentException("Os vetores devem ter a mesma dimensão.");
        }

        int dim = a.getDim();
        double[] res = new double[dim];

        for (int i = 1; i <= dim; i++) {
            res[i - 1] = a.get(i) + b.get(i);
        }
        return new Vector(res, dim);
    }



    public static Matrix times(Matrix a, Matrix b) {
        if (a.getRows() != b.getRows() || a.getCols() != b.getCols()) {
            throw new IllegalArgumentException("As matrizes devem ter as mesmas dimensões.");
        }

        int rows = a.getRows();
        int cols = a.getCols();
        double[][] res = new double[rows][cols];

        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                res[i - 1][j - 1] = a.get(i, j) * b.get(i, j);
            }
        }
        return new Matrix(rows, cols, res);
    }



    public static Vector times(Vector a, Vector b) {
        if (a.getDim() != b.getDim()) {
            throw new IllegalArgumentException("Os vetores devem ter a mesma dimensão.");
        }

        int dim = a.getDim();
        double[] res = new double[dim];

        for (int i = 1; i <= dim; i++) {
            res[i - 1] = a.get(i) * b.get(i);
        }
        return new Vector(res, dim);
    }



    public static Matrix times(double a, Matrix b) {
        int rows = b.getRows();
        int cols = b.getCols();
        double[][] res = new double[rows][cols];

        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                res[i - 1][j - 1] = a * b.get(i, j);
            }
        }
        return new Matrix(rows, cols, res);
    }



    public static Matrix times(Matrix a, double b) {
        return times(b, a);
    }



    public static Vector times(double a, Vector b) {
        int dim = b.getDim();
        double[] res = new double[dim];

        for (int i = 1; i <= dim; i++) {
            res[i - 1] = a * b.get(i);
        }
        return new Vector(res, dim);
    }



    public static Vector times(Vector a, double b) {
        return times(b, a);
    }

    public static void alissonTeste() {
        double[][] m1Data = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] m2Data = {{5.0, 6.0}, {7.0, 8.0}};
        Matrix m1 = new Matrix(2, 2, m1Data);
        Matrix m2 = new Matrix(2, 2, m2Data);

        System.out.println("============== TESTES COM MATRIZES ==============");

        System.out.println("\n--- Matriz M1 ---");
        m1.printMatrix();

        System.out.println("\n1. Transposta de M1:");
        LinearAlgebra.transpose(m1).printMatrix();

        System.out.println("\n2. Soma (M1 + M2):");
        LinearAlgebra.sum(m1, m2).printMatrix();

        System.out.println("\n3. Multiplicação Elemento a Elemento (M1 * M2):");
        LinearAlgebra.times(m1, m2).printMatrix();

        System.out.println("\n4. Multiplicação por Escalar (2.5 * M1):");
        LinearAlgebra.times(2.5, m1).printMatrix();

        System.out.println("\n============== TESTES COM VETORES ==============");

        Vector v1 = new Vector(new double[]{1.0, 2.0, 3.0}, 3);
        Vector v2 = new Vector(new double[]{4.0, 5.0, 6.0}, 3);

        System.out.println("\n--- Vetor V1 ---");
        v1.printVector();
        System.out.println("--- Vetor V2 ---");
        v2.printVector();

        System.out.println("\n5. Transposta de V1 (vira Matrix 1x3):");
        LinearAlgebra.transpose(v1).printMatrix();

        System.out.println("\n6. Soma (V1 + V2):");
        LinearAlgebra.sum(v1, v2).printVector();

        System.out.println("\n7. Multiplicação Elemento a Elemento (V1 * V2):");
        LinearAlgebra.times(v1, v2).printVector();

        System.out.println("\n8. Multiplicação por Escalar (2.5 * V1):");
        LinearAlgebra.times(2.5, v1).printVector();

        System.out.println("\n9. Erro esperado: somar vetores de dimensões diferentes -> V1 + V3");
        Vector v3 = new Vector(new double[]{1.0, 2.0}, 2);
        System.out.println("--- Vetor V3 ---");
        v3.printVector();

        try {
            LinearAlgebra.sum(v1, v3);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    //parte Enzo

    public static Matrix dot(Matrix a, Matrix b) {
        
        if (a.getCols() != b.getRows()) {
            throw new IllegalArgumentException("A quantidade de colunas da primeira matriz deve ser igual à quantidade de linhas da segunda matriz.");
        }

        int rows = a.getRows();
        int cols = b.getCols();        
     
        double[][] resultElements = new double[rows][cols];
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                double sum = 0;
                for (int k = 1; k <= a.getCols(); k++) {

                    sum += a.get(i, k) * b.get(k, j);
                }
               
                resultElements[i-1][j-1] = sum; 
            }
        }

        return new Matrix(rows, cols, resultElements);
    }

    public static Matrix gauss(Matrix a) {
        int rows = a.getRows();
        int cols = a.getCols();
        
        double[][] copyElements = new double[rows][cols];
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                copyElements[i-1][j-1] = a.get(i, j);
            }
        }
        Matrix res = new Matrix(rows, cols, copyElements);

        for (int k = 1; k <= Math.min(rows, cols); k++) {
            
            int maxRow = k;
            double maxVal = Math.abs(res.get(k, k));
            for (int i = k + 1; i <= rows; i++) {
                if (Math.abs(res.get(i, k)) > maxVal) {
                    maxVal = Math.abs(res.get(i, k));
                    maxRow = i;
                }
            }
            
            if (maxRow != k) {
                for (int j = 1; j <= cols; j++) {
                    double temp = res.get(k, j);
                    res.set(k, j, res.get(maxRow, j));
                    res.set(maxRow, j, temp);
                }
            }

            if (Math.abs(res.get(k, k)) < 1e-10) {
                continue;
            }

            for (int i = k + 1; i <= rows; i++) {
                double factor = res.get(i, k) / res.get(k, k);
                res.set(i, k, 0.0);
                for (int j = k + 1; j <= cols; j++) {
                    double newValue = res.get(i, j) - factor * res.get(k, j);
                    res.set(i, j, newValue);
                }
            }
        }

        return res;
    }

    public static Matrix solve(Matrix a) {

        Matrix escalonada = gauss(a);
        int rows = escalonada.getRows();
        int cols = escalonada.getCols();

        if (cols <= rows) {
            throw new IllegalArgumentException("Erro: A matriz não representa um sistema linear resolvível (precisa ser aumentada).");
        }

        int n = cols - 1;

        for (int i = 1; i <= rows; i++) {
            boolean coeficientesZerados = true;
            for (int j = 1; j <= n; j++) {
                if (Math.abs(escalonada.get(i, j)) > 1e-10) {
                    coeficientesZerados = false;
                    break;
                }
            }
            if (coeficientesZerados && Math.abs(escalonada.get(i, cols)) > 1e-10) {
                double[][] semSolucao = {{0.0}};
                return new Matrix(1, 1, semSolucao);
            }
        }

        boolean indeterminado = false;
        for (int i = 1; i <= Math.min(rows, n); i++) {
            if (Math.abs(escalonada.get(i, i)) < 1e-10) {
                indeterminado = true;
                break;
            }
        }

        if (indeterminado) {
            double[][] simbolico = new double[n][1];
            for (int i = 0; i < n; i++) simbolico[i][0] = Double.NaN;
            return new Matrix(n, 1, simbolico);
        }

        double[][] solElements = new double[rows][1];
        for (int i = rows; i >= 1; i--) {
            double sum = escalonada.get(i, cols);
            for (int j = i + 1; j <= rows; j++) {
                sum -= escalonada.get(i, j) * solElements[j - 1][0];
            }
            double pivot = escalonada.get(i, i);
            solElements[i - 1][0] = sum / pivot;
        }
        return new Matrix(rows, 1, solElements);
    }

    public static void enzoTeste() {
        System.out.println("\n--- Teste do Método: dot (Multiplicação) ---");
        double[][] valA = {{1, 2}, {3, 4}};
        double[][] valB = {{2, 0}, {1, 2}};
        Matrix mA = new Matrix(2, 2, valA);
        System.out.println("\n--- Matriz A ---");
        mA.printMatrix();
        Matrix mB = new Matrix(2, 2, valB);
        System.out.println("\n--- Matriz B ---");
        mB.printMatrix();
        System.out.println("\n--- Multiplicação ---");
        dot(mA, mB).printMatrix();

        System.out.println("\n9b. Erro esperado: multiplicação com dimensões incompatíveis -> A x C");
        Matrix mC = new Matrix(3, 1, new double[][]{{1}, {2}, {3}});
        System.out.println("\n--- Matriz C ---");
        mC.printMatrix();

        try {
            dot(mA, mC);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("\n--- Teste do Método: gauss (Eliminação) ---");
        System.out.println("\n--- Matriz G ---");
        double[][] valG = {
                {2, 1, -1, 8},
                {-3, -1, 2, -11},
                {-2, 1, 2, -3}
        };
        Matrix mG = new Matrix(3, 4, valG);
        mG.printMatrix();
        Matrix mGauss = gauss(mG);
        System.out.println("\n--- Escalonada ---");
        mGauss.printMatrix();

        System.out.println("\n--- Teste do Método: solve — Sistema Possível e Determinado (SPD) ---");
        System.out.println("\n--- Resolução de G ---");
        solve(mG).printMatrix();

        System.out.println("\n--- Teste do Método: solve — Sistema Possível e Indeterminado (SPI) ---");
        double[][] valSPI = {
                {1, 2, 3, 6},
                {2, 4, 6, 12},
                {1, 1, 1, 3}
        };
        System.out.println("\n--- Sistema SPI ---");
        new Matrix(3, 4, valSPI).printMatrix();
        System.out.println();
        solve(new Matrix(3, 4, valSPI)).printMatrix();

        System.out.println("\n--- Teste do Método: solve — Sistema Impossível (SI) ---");
        double[][] valSI = {
                {1, 1, 1, 3},
                {1, 1, 1, 5},
                {2, 2, 2, 8}
        };
        System.out.println("\n--- Sistema SI ---");
        new Matrix(3, 4, valSI).printMatrix();
        System.out.println();
        solve(new Matrix(3, 4, valSI)).printMatrix();

        System.out.println("\n--- Teste do Método: solve — Erro esperado (matriz não aumentada) ---");
        double[][] valNaoAumentada = {{1, 2}, {3, 4}};
        System.out.println("\n--- Sistema SI ---");
        new Matrix(2, 2, valNaoAumentada).printMatrix();
        try {
            solve(new Matrix(2, 2, valNaoAumentada));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
