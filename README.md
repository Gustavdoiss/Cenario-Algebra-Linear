# Implementação Computacional de Álgebra Matricial e Sistemas Lineares

Trabalho da disciplina **T168: Álgebra e Geometria Computacional** (UNIFOR).

O objetivo é implementar, em **Java**, a representação de matrizes e vetores, as operações algébricas elementares, a eliminação gaussiana e o algoritmo de Gauss-Jordan para resolução de sistemas lineares.

## Checklist

### Classe `Matrix`
- [x] Construtor: `Matrix(int rows, int cols, double[] elements)`
- [x] Método `get(int i, int j)`
- [x] Método `set(int i, int j, double value)`

### Classe `Vector`
- [x] Construtor: `Vector(int dim, double[] elements)`
- [x] Método `get(int i)`
- [x] Método `set(int i, double value)`

### Classe `LinearAlgebra`
- [x] Método `transpose(a)` — transpõe uma `Matrix` ou `Vector`
- [x] Método `sum(a, b)` — soma duas matrizes ou dois vetores
- [x] Método `times(a, b)` — multiplicação elemento a elemento (ou escalar × matriz)
- [x] Método `dot(a, b)` — multiplicação de matrizes
- [x] Método `gauss(a)` — eliminação gaussiana
- [x] Método `solve(a)` — resolve o sistema linear (matriz aumentada)

### Extras
- [x] Tratamento de erros nos métodos (dimensões incompatíveis, índices inválidos etc.)
- [x] Testes das operações implementadas
