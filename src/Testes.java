public class Testes {

    public static void main(String[] args) {
        alissonTeste();
        gustavoTeste();
        arthurTeste();
        enzoTeste();
    }

    //Alisson
    public static void alissonTeste() {
        System.out.println("============== TESTES DE TRANSLAÇÃO ==============");

        Vector v2 = new Vector(new double[]{2.0, 3.0}, 2);
        System.out.println("\n--- Vetor V2 (2D) ---");
        v2.printVector();

        System.out.println("\n1. translate2D(V2, dx = 4, dy = -1) -> esperado (6, 2):");
        Transformations.translate2D(v2, 4.0, -1.0).printVector();

        Vector v3 = new Vector(new double[]{1.0, 2.0, 3.0}, 3);
        System.out.println("\n--- Vetor V3 (3D) ---");
        v3.printVector();

        System.out.println("\n2. translate3D(V3, dx = 1, dy = 1, dz = -5) -> esperado (2, 3, -2):");
        Transformations.translate3D(v3, 1.0, 1.0, -5.0).printVector();

        System.out.println("\n3. Erro esperado: translate2D com vetor 3D -> translate2D(V3, 1, 1)");
        try {
            Transformations.translate2D(v3, 1.0, 1.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("\n4. Erro esperado: translate3D com vetor 2D -> translate3D(V2, 1, 1, 1)");
        try {
            Transformations.translate3D(v2, 1.0, 1.0, 1.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    //Gustavo
    public static void gustavoTeste() {
        System.out.println("\n============== TESTES DE CISALHAMENTO E ROTAÇÃO ==============");

        Vector v2 = new Vector(new double[]{2.0, 3.0}, 2);
        System.out.println("\n--- Vetor V2 (2D) ---");
        v2.printVector();

        System.out.println("\n1. shearing(V2, kx = 2, ky = 0) -> esperado (8, 3):");
        Transformations.shearing(v2, 2.0, 0.0).printVector();

        System.out.println("\n2. shearing(V2, kx = 0, ky = 1) -> esperado (2, 5):");
        Transformations.shearing(v2, 0.0, 1.0).printVector();

        System.out.println("\n3. rotation2D(V2, 90°) -> esperado (-3, 2):");
        Transformations.rotation2D(v2, 90.0).printVector();

        Vector v3 = new Vector(new double[]{1.0, 2.0, 3.0}, 3);
        System.out.println("\n--- Vetor V3 (3D) ---");
        v3.printVector();

        System.out.println("\n4. rotation3DX(V3, 90°) -> esperado (1, -3, 2):");
        Transformations.rotation3DX(v3, 90.0).printVector();

        System.out.println("\n5. rotation3DY(V3, 90°) -> esperado (3, 2, -1):");
        Transformations.rotation3DY(v3, 90.0).printVector();

        System.out.println("\n6. rotation3DZ(V3, 90°) -> esperado (-2, 1, 3):");
        Transformations.rotation3DZ(v3, 90.0).printVector();

        System.out.println("\n7. Erro esperado: rotation3DX com vetor 2D -> rotation3DX(V2, 90°)");
        try {
            Transformations.rotation3DX(v2, 90.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("\n8. Erro esperado: shearing com vetor 3D -> shearing(V3, 1, 1)");
        try {
            Transformations.shearing(v3, 1.0, 1.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    //Arthur
    public static void arthurTeste() {
        System.out.println("\n============== TESTES DE REFLEXÃO ==============");

        Vector v2 = new Vector(new double[]{2.0, 3.0}, 2);
        System.out.println("\n--- Vetor V2 (2D) ---");
        v2.printVector();

        System.out.println("\n1. reflection2DX(V2) -> esperado (2, -3):");
        Transformations.reflection2DX(v2).printVector();

        System.out.println("\n2. reflection2DY(V2) -> esperado (-2, 3):");
        Transformations.reflection2DY(v2).printVector();

        Vector v3 = new Vector(new double[]{1.0, 2.0, 3.0}, 3);
        System.out.println("\n--- Vetor V3 (3D) ---");
        v3.printVector();

        System.out.println("\n3. reflection3DX(V3) [plano YZ] -> esperado (-1, 2, 3):");
        Transformations.reflection3DX(v3).printVector();

        System.out.println("\n4. reflection3DY(V3) [plano XZ] -> esperado (1, -2, 3):");
        Transformations.reflection3DY(v3).printVector();

        System.out.println("\n5. reflection3DZ(V3) [plano XY] -> esperado (1, 2, -3):");
        Transformations.reflection3DZ(v3).printVector();

        System.out.println("\n6. reflection3DXAxis(V3) [eixo X] -> esperado (1, -2, -3):");
        Transformations.reflection3DXAxis(v3).printVector();

        System.out.println("\n7. reflection3DYAxis(V3) [eixo Y] -> esperado (-1, 2, -3):");
        Transformations.reflection3DYAxis(v3).printVector();

        System.out.println("\n8. reflection3DZAxis(V3) [eixo Z] -> esperado (-1, -2, 3):");
        Transformations.reflection3DZAxis(v3).printVector();

        System.out.println("\n9. Erro esperado: reflection2DX com vetor 3D -> reflection2DX(V3)");
        try {
            Transformations.reflection2DX(v3);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    //Enzo
    public static void enzoTeste() {
        System.out.println("\n============== TESTES DE PROJEÇÃO ==============");

        Vector v2 = new Vector(new double[]{2.0, 3.0}, 2);
        System.out.println("\n--- Vetor V2 (2D) ---");
        v2.printVector();

        System.out.println("\n1. projection2DX(V2) -> esperado (2, 0):");
        Transformations.projection2DX(v2).printVector();

        System.out.println("\n2. projection2DY(V2) -> esperado (0, 3):");
        Transformations.projection2DY(v2).printVector();

        Vector v3 = new Vector(new double[]{1.0, 2.0, 3.0}, 3);
        System.out.println("\n--- Vetor V3 (3D) ---");
        v3.printVector();

        System.out.println("\n3. projection3DX(V3) [plano YZ] -> esperado (0, 2, 3):");
        Transformations.projection3DX(v3).printVector();

        System.out.println("\n4. projection3DY(V3) [plano XZ] -> esperado (1, 0, 3):");
        Transformations.projection3DY(v3).printVector();

        System.out.println("\n5. projection3DZ(V3) [plano XY] -> esperado (1, 2, 0):");
        Transformations.projection3DZ(v3).printVector();

        System.out.println("\n6. projection3DXAxis(V3) [eixo X] -> esperado (1, 0, 0):");
        Transformations.projection3DXAxis(v3).printVector();

        System.out.println("\n7. projection3DYAxis(V3) [eixo Y] -> esperado (0, 2, 0):");
        Transformations.projection3DYAxis(v3).printVector();

        System.out.println("\n8. projection3DZAxis(V3) [eixo Z] -> esperado (0, 0, 3):");
        Transformations.projection3DZAxis(v3).printVector();

        System.out.println("\n9. Erro esperado: projection3DX com vetor 2D -> projection3DX(V2)");
        try {
            Transformations.projection3DX(v2);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
