// =========================================================
// Funções pequenas usadas pelas duas abas
// =========================================================

export const $ = id => document.getElementById(id);

// Formata número no padrão brasileiro: 2.5 -> "2,5"; -0 -> "0"
export function fmt(n) {
  if (!isFinite(n)) return "∞";
  let r = Math.round(n * 1000) / 1000;
  if (Object.is(r, -0)) r = 0;
  return String(r).replace(".", ",");
}
// Coloca parênteses em negativos: (-2)
export const p = n => (n < 0 ? "(" + fmt(n) + ")" : fmt(n));
// (x ; y ; z)
export const vec = v => "(" + v.map(fmt).join(" ; ") + ")";

// Matriz em HTML. "destacarUltima" deixa a última linha/coluna (parte homogênea) em cinza.
export function matHtml(M, destacarUltima = false) {
  const n = M.length, m = M[0].length;
  let s = '<span class="mx" style="grid-template-columns:repeat(' + m + ',auto)">';
  M.forEach((linha, i) => linha.forEach((x, j) => {
    const h = destacarUltima && (i === n - 1 || j === m - 1);
    s += "<i" + (h ? ' class="h"' : "") + ">" + fmt(x) + "</i>";
  }));
  return s + "</span>";
}
// Vetor coluna em HTML
export const colHtml = (v, destacarUltima = false) => matHtml(v.map(x => [x]), destacarUltima);

// Escapa < > & para inserir texto como HTML com segurança
export const esc = s => s.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");

// ---------- Destaque de sintaxe Java (bem simples) ----------
const PALAVRAS = new Set(("public private protected static final class return new if else for while " +
  "throw throws try catch void double int boolean char long float true false null this").split(" "));

export function javaHtml(codigo) {
  // Cada pedaço é: comentário | string | número | palavra
  const re = /(\/\/[^\n]*)|("(?:\\.|[^"\\])*")|(\b\d+(?:\.\d+)?\b)|([A-Za-z_]\w*)/g;
  let out = "", ultimo = 0, m;
  while ((m = re.exec(codigo))) {
    out += esc(codigo.slice(ultimo, m.index));
    const t = esc(m[0]);
    if (m[1]) out += '<span class="co">' + t + "</span>";
    else if (m[2]) out += '<span class="st">' + t + "</span>";
    else if (m[3]) out += '<span class="nu">' + t + "</span>";
    else if (PALAVRAS.has(m[0])) out += '<span class="kw">' + t + "</span>";
    else if (/^[A-Z]/.test(m[0])) out += '<span class="tp">' + t + "</span>";
    else out += t;
    ultimo = re.lastIndex;
  }
  return out + esc(codigo.slice(ultimo));
}

// Bloco de código com o nome do arquivo e a linha onde o método começa
export const blocoJava = trecho =>
  '<span class="arquivo">' + esc(trecho.arquivo) + " · linha " + trecho.linhaInicial + "</span>" +
  '<pre class="java"><code>' + javaHtml(trecho.codigo) + "</code></pre>";

// Espera o usuário parar de digitar antes de chamar a API
export function debounce(fn, ms = 150) {
  let id = 0;
  return (...args) => { clearTimeout(id); id = setTimeout(() => fn(...args), ms); };
}

// Tamanho (em px) da área quadrada onde o canvas do manim-web fica
export const tamanhoCena = el => Math.max(200, Math.round(el.clientWidth));
