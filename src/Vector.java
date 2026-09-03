public class Vector {
    private double[] elements;
    private final int dim;

    public Vector(double[] elements, int dim) {
        if (dim <= 0) throw new ExceptionInInitializerError("Dimensão de Vetor inválida!");
        this.dim = dim;
        this.elements = new double[dim];

        for (int i = 0; i < dim; i++) {
            this.elements[i] = elements[i];
        }
    }

    public Vector (int dim) {
        if (dim <= 0) throw new ExceptionInInitializerError("Dimensão de Vetor inválida!");
        this.dim = dim;
        this.elements = new double[dim];

        for (int i = 0; i < dim; i++) {
            this.elements[i] = Math.round(Math.random() * 11);
        }
    }

    public int getDim() {
        return this.dim;
    }

    public double get(int i) {
        if (i > this.dim) {
            throw new Error("Índice não existente no Vetor!");
        }else {
            return this.elements[i-1];
        }
    }

    public void set(int i, double value) {
        if (i > this.dim) {
            throw new Error("Índice não existente no Vetor!");
        }else {
            this.elements[i-1] = value;
        }
    }
}