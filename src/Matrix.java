public class Matrix {
    private final int rows, cols;
    private final double[][] elements;

    public Matrix(int rows, int cols, double[][] elements) {
        this.rows = rows;
        this.cols = cols;
        this.elements = new double[rows][cols];

        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[0].length; j++) set(i+1, j+1, elements[i][j]);
        }
    }

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.elements = new double[rows][cols];

        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[0].length; j++) {
                set(i + 1, j + 1, Math.random() * 10);
            }
        }
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public void set(int i, int j, double value) {
        if (i > rows || i <= 0 || j > cols || j <= 0) {
            System.out.println("Não foi possível definir um valor!");
        }else {
            this.elements[i-1][j-1] = value;
        }
    }

    public double get(int i, int j) {
        if (i > rows || i <= 0 || j > cols || j <= 0) {
            System.out.println("Não foi possível encontrar um valor!");
            return 0;
        }else {
            return this.elements[i-1][j-1];
        }
    }

    public void printMatrix() {
        System.out.print("| . ");
        for (int i = 0; i < this.cols; i++) {
            System.out.printf("|   %d  ", i+1);
        }
        System.out.println("|");

        for (int i = 0; i < this.rows; i++) {
            System.out.printf("| %d ", i+1);
            for (int j = 0; j < this.cols; j++) {
                System.out.printf("| %.2f ", this.get(i+1, j+1));
            }
            System.out.println("|");
        }
    }
}
