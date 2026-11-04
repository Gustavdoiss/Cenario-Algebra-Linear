// =========================================================
// Comunicação com a API REST (Spring Boot)
// =========================================================

// Se a página foi aberta pelo próprio Spring (http://localhost:8080), a API está na mesma origem.
// Se foi aberta por outro servidor (ex.: Live Server na porta 5500), aponta para localhost:8080.
const BASE = location.port === "8080" ? "" : "http://localhost:8080";

async function pedir(caminho, opcoes) {
  const resp = await fetch(BASE + caminho, opcoes);
  const dados = await resp.json().catch(() => ({}));
  if (!resp.ok) {
    // O back-end responde { "erro": "mensagem" } quando algo dá errado (veja TratadorDeErros.java)
    throw new Error(dados.erro || "Erro " + resp.status + " na API");
  }
  return dados;
}

const postJson = (caminho, corpo) =>
  pedir(caminho, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(corpo)
  });

// GET /api/transformacoes
export const listarTransformacoes = () => pedir("/api/transformacoes");

// POST /api/transformacoes/{id}   { vetor: [...], parametros: {...} }
export const aplicarTransformacao = (id, vetor, parametros) =>
  postJson("/api/transformacoes/" + id, { vetor, parametros });

// POST /api/homogeneas/para-homogenea   { vetor: [x, y] }
export const paraHomogenea = vetor => postJson("/api/homogeneas/para-homogenea", { vetor });

// POST /api/homogeneas/para-cartesiana  { vetor: [x, y, w] }
export const paraCartesiana = vetor => postJson("/api/homogeneas/para-cartesiana", { vetor });

// GET /api/codigo?metodos=a,b,c   (o código Java vem do próprio back-end)
const cacheCodigo = new Map();
export async function buscarCodigo(metodos) {
  const faltando = metodos.filter(m => !cacheCodigo.has(m));
  if (faltando.length) {
    const trechos = await pedir("/api/codigo?metodos=" + encodeURIComponent(faltando.join(",")));
    trechos.forEach(t => cacheCodigo.set(t.metodo, t));
  }
  return metodos.map(m => cacheCodigo.get(m));
}
