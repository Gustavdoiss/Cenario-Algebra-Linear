package br.unifor.algebralinear;

import br.unifor.algebralinear.algebra.Transformations;
import br.unifor.algebralinear.algebra.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransformationsTest {

    private static final double EPS = 1e-9;

    private static Vector v(double... xs) {
        return new Vector(xs, xs.length);
    }

    @Test
    void translacao2D() {
        Vector r = Transformations.translate2D(v(2, 1), 3, -4);
        assertEquals(5, r.get(1), EPS);
        assertEquals(-3, r.get(2), EPS);
    }

    @Test
    void rotacao2DDe90Graus() {
        Vector r = Transformations.rotation2D(v(1, 0), 90);
        assertEquals(0, r.get(1), EPS);
        assertEquals(1, r.get(2), EPS);
    }

    @Test
    void rotacao3DEmZ() {
        Vector r = Transformations.rotation3DZ(v(1, 0, 5), 90);
        assertArrayEquals(new double[]{0, 1, 5}, new double[]{r.get(1), r.get(2), r.get(3)}, EPS);
    }

    @Test
    void cisalhamentoSegueAMatrizDoRepositorio() {
        // x' = x + ky*y ; y' = kx*x + y
        Vector r = Transformations.shearing(v(2, 3), 0.5, 1);
        assertEquals(5, r.get(1), EPS);
        assertEquals(4, r.get(2), EPS);
    }

    @Test
    void reflexaoEProjecao() {
        Vector r = Transformations.reflection3DX(v(1, 2, 3));
        assertArrayEquals(new double[]{1, -2, -3}, new double[]{r.get(1), r.get(2), r.get(3)}, EPS);
        Vector p = Transformations.projection2DY(v(4, 7));
        assertEquals(0, p.get(1), EPS);
        assertEquals(7, p.get(2), EPS);
    }

    @Test
    void dimensaoErradaLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> Transformations.translate2D(v(1, 2, 3), 1, 1));
    }

    @Test
    void homogeneasIdaEVolta() {
        var h = Transformations.toHomogeneous(v(3, 4));
        assertEquals(3, h.getRows());
        assertEquals(1, h.get(3, 1), EPS);
        Vector c = Transformations.toCartesian(h);
        assertEquals(3, c.get(1), EPS);
        assertEquals(4, c.get(2), EPS);
    }
}
