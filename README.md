# Implementação Computacional de Álgebra Matricial e Sistemas Lineares

Trabalho da disciplina **T168: Álgebra e Geometria Computacional** (UNIFOR).

O objetivo é implementar, em **Java**, a representação de matrizes e vetores, as operações algébricas elementares, a eliminação gaussiana e o algoritmo de Gauss-Jordan para resolução de sistemas lineares.

## Checklist

### Classe `Matrix`
- [ ] Construtor: `Matrix(int rows, int cols, double[] elements)`
- [ ] Método `get(int i, int j)`
- [ ] Método `set(int i, int j, double value)`

### Classe `Vector`
- [ ] Construtor: `Vector(int dim, double[] elements)`
- [ ] Método `get(int i)`
- [ ] Método `set(int i, double value)`

### Classe `LinearAlgebra`
- [ ] Método `transpose(a)` — transpõe uma `Matrix` ou `Vector`
- [ ] Método `sum(a, b)` — soma duas matrizes ou dois vetores
- [ ] Método `times(a, b)` — multiplicação elemento a elemento (ou escalar × matriz)
- [ ] Método `dot(a, b)` — multiplicação de matrizes
- [ ] Método `gauss(a)` — eliminação gaussiana
- [ ] Método `solve(a)` — resolve o sistema linear (matriz aumentada)

### Extras
- [ ] Tratamento de erros nos métodos (dimensões incompatíveis, índices inválidos etc.)
- [ ] Testes das operações implementadas
