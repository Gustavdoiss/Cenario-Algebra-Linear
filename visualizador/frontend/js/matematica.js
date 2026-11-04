// =========================================================
// Contas usadas SÓ para desenhar a animação.
// (Os cálculos "de verdade" — matriz e resultado — vêm da API Java.)
// =========================================================

// Matriz identidade n x n
export const identidade = n =>
  Array.from({ length: n }, (_, i) => Array.from({ length: n }, (_, j) => (i === j ? 1 : 0)));

// Mistura a identidade com H: t = 0 -> nada mudou, t = 1 -> H completa.
// É isso que faz a grade "se mexer aos poucos" na animação.
export function interpolar(H, t) {
  const I = identidade(H.length);
  return H.map((linha, i) => linha.map((h, j) => (1 - t) * I[i][j] + t * h));
}

// Aplica uma matriz homogênea a um ponto cartesiano:
// (x, y) -> [x, y, 1] -> H · [x, y, 1] -> divide por w
export function transformarPonto(H, ponto) {
  const n = H.length;                   // 3 no 2D, 4 no 3D
  const ph = [...ponto, 1];
  const r = H.map(linha => linha.reduce((soma, h, j) => soma + h * ph[j], 0));
  const w = r[n - 1] || 1;
  return r.slice(0, n - 1).map(x => x / w);
}

// Garante um ponto 3D [x, y, z] (o manim-web sempre trabalha com 3 coordenadas)
export const p3 = q => [q[0], q[1], q[2] || 0];

export const distancia = (a, b) => Math.hypot(a[0] - b[0], a[1] - b[1], (a[2] || 0) - (b[2] || 0));
