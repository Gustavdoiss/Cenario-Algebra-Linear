// =========================================================
// ABA "Transformações"
// 1. Busca o catálogo na API (GET /api/transformacoes)
// 2. Monta os botões e controles
// 3. A cada mudança chama POST /api/transformacoes/{id} e atualiza cena, resultado e explicação
// =========================================================
import { listarTransformacoes, aplicarTransformacao, buscarCodigo } from "./api.js";
import { $, fmt, p, vec, matHtml, colHtml, blocoJava, debounce, esc } from "./util.js";
import { Cena2D } from "./cena2d.js";
import { Cena3D } from "./cena3d.js";
import { COR } from "./cores.js";

const NOMES_EIXOS = ["x", "y", "z"];
const VETOR_PADRAO = { 2: [2, 1], 3: [1, 2, 1] };

const estado = {
  catalogo: [],        // vem da API
  dim: 2,
  categoria: "Rotação",
  id: "rotation2D",
  parametros: {},      // { angle: 45 }
  vetor: [2, 1],
  cena: null,          // Cena2D ou Cena3D
  cenaDim: 0,
  pedido: 0            // para ignorar respostas antigas da API
};

export async function iniciar() {
  estado.catalogo = await listarTransformacoes();
  ligarEventos();
  escolherDimensao(2);
}

// Chamado quando a aba fica visível de novo
export function mostrar() {
  if (estado.cena) estado.cena.redimensionar();
}

export function redimensionar() {
  if (estado.cena && !$("pg-transformacoes").hidden) estado.cena.redimensionar();
}

// ---------------------------------------------------------
// Seleção (dimensão → categoria → variante)
// ---------------------------------------------------------
const doCatalogo = () => estado.catalogo.filter(t => t.dimensao === estado.dim);
const definicao = () => estado.catalogo.find(t => t.id === estado.id);

function escolherDimensao(dim) {
  estado.dim = dim;
  estado.vetor = [...VETOR_PADRAO[dim]];
  document.querySelectorAll("#tf-dimensao button").forEach(b => b.classList.toggle("on", +b.dataset.dim === dim));

  // Categorias que existem nessa dimensão (ex.: cisalhamento só tem 2D)
  const categorias = [...new Set(doCatalogo().map(t => t.categoria))];
  $("tf-categorias").innerHTML = categorias
    .map(c => '<button type="button" data-cat="' + esc(c) + '">' + esc(c) + "</button>").join("");
  if (!categorias.includes(estado.categoria)) estado.categoria = categorias[0];

  montarVetor();
  criarCena();
  escolherCategoria(estado.categoria);
}

function escolherCategoria(cat) {
  estado.categoria = cat;
  document.querySelectorAll("#tf-categorias button").forEach(b => b.classList.toggle("on", b.dataset.cat === cat));
  const variantes = doCatalogo().filter(t => t.categoria === cat);
  // Só mostra os botões de variante quando há mais de uma (ex.: rotação em X, Y ou Z)
  $("tf-variantes").innerHTML = variantes.length > 1
    ? variantes.map(t => '<button type="button" data-id="' + t.id + '">' + esc(t.nome) + "</button>").join("")
    : "";
  escolherTransformacao(variantes[0].id);
}

function escolherTransformacao(id) {
  estado.id = id;
  const def = definicao();
  document.querySelectorAll("#tf-variantes button").forEach(b => b.classList.toggle("on", b.dataset.id === id));
  $("tf-descricao").textContent = def.descricao;

  // Parâmetros: um controle deslizante + caixa numérica para cada um
  estado.parametros = {};
  $("tf-parametros").innerHTML = def.parametros.map(par => {
    estado.parametros[par.nome] = par.padrao;
    return '<div class="param"><label class="f"><span>' + esc(par.rotulo) + "</span>" +
      '<span class="linha">' +
      '<input type="range" data-par="' + par.nome + '" min="' + par.min + '" max="' + par.max + '" step="' + par.passo + '" value="' + par.padrao + '">' +
      '<input type="number" data-par="' + par.nome + '" step="' + par.passo + '" value="' + par.padrao + '" aria-label="' + esc(par.rotulo) + '">' +
      "</span></label></div>";
  }).join("") || '<p class="mini">Esta transformação não tem parâmetros: a matriz é sempre a mesma.</p>';

  calcular({ animar: true });
}

function montarVetor() {
  $("tf-vetor").innerHTML = estado.vetor.map((x, i) =>
    '<label class="f"><span>' + NOMES_EIXOS[i] + '</span><input type="number" step="any" data-vi="' + i + '" value="' + x + '"></label>'
  ).join("");
}

function criarCena() {
  if (estado.cena && estado.cenaDim === estado.dim) return;
  if (estado.cena) estado.cena.destruir();
  const el = $("tf-cena");
  try {
    estado.cena = estado.dim === 2 ? new Cena2D(el) : new Cena3D(el);
    estado.cenaDim = estado.dim;
    estado.cena.aoMudarT = t => {
      $("tf-t").value = t;
      $("tf-t-label").textContent = "t = " + fmt(t) + (t >= 1 ? " (transformação completa)" : "");
    };
  } catch (e) {
    console.error(e);
    estado.cena = null;
    el.innerHTML = '<div class="msg">Não foi possível iniciar o manim-web (WebGL): ' + esc(e.message) + "</div>";
  }
  const cor = (c, txt) => '<span class="cor" style="background:' + c + '"></span>' + txt;
  $("tf-legenda").innerHTML = [
    cor(COR.i, "î = e1"), cor(COR.j, "ĵ = e2"), estado.dim === 3 ? cor(COR.k, "k̂ = e3") : null,
    cor(COR.v, "v → H·v"), cor(COR.antes, estado.dim === 2 ? "letra F antes" : "cubo antes"),
    estado.dim === 2 ? cor(COR.grade, "grade transformada") : "arraste para girar a câmera"
  ].filter(Boolean).join(" · ");
}

// ---------------------------------------------------------
// Chamada à API e atualização da tela
// ---------------------------------------------------------
async function calcular({ animar = false } = {}) {
  const meuPedido = ++estado.pedido;
  const def = definicao();
  let r;
  try {
    r = await aplicarTransformacao(def.id, estado.vetor, estado.parametros);
  } catch (e) {
    $("tf-resultado").innerHTML = '<p class="mini erro">' + esc(e.message) + "</p>";
    return;
  }
  if (meuPedido !== estado.pedido) return;   // chegou uma resposta mais nova

  if (estado.cena) {
    estado.cena.definirMatriz(r.matriz);
    estado.cena.definirVetor(r.entrada);
    if (animar) estado.cena.animar(); else if (!estado.cena.animando) estado.cena.desenhar(1);
  }
  mostrarResultado(r);
  await mostrarExplicacao(r, def);
}

function mostrarResultado(r) {
  $("tf-resultado").innerHTML =
    "<h3>Resultado</h3>" +
    '<p class="big">' + r.nome + "(v) = " + vec(r.resultado) + "</p>" +
    '<span class="rot">Matriz homogênea H (' + r.matriz.length + "×" + r.matriz.length + ")</span>" +
    '<div class="conta">' + matHtml(r.matriz, true) + "</div>" +
    '<span class="rot">H · v<sub>h</sub> = resultado homogêneo</span>' +
    '<div class="conta">' + matHtml(r.matriz, true) + "<span>·</span>" + colHtml(r.entradaHomogenea, true) +
    "<span>=</span>" + colHtml(r.saidaHomogenea, true) + "</div>" +
    '<p class="mini">A parte cinza é a "parte homogênea": a última linha [0 … 0 1] mantém w = 1.</p>';
}

async function mostrarExplicacao(r, def) {
  const n = r.entrada.length;
  const nomesP = Object.entries(r.parametros).map(([k, v]) => k + " = " + fmt(v)).join(", ");
  const w = r.saidaHomogenea[n];

  // Uma linha da multiplicação para cada coordenada: x′ = a·x + b·y + c·1
  const linhas = r.matriz.slice(0, n).map((linha, i) =>
    "<li><code>" + NOMES_EIXOS[i] + "′ = " +
    linha.map((h, j) => p(h) + "·" + p(r.entradaHomogenea[j])).join(" + ") +
    " = " + fmt(r.saidaHomogenea[i]) + "</code></li>").join("");

  let html = "<h3>Explicação</h3><ol>" +
    "<li>Montar a matriz homogênea com <code>" + def.id + "Matrix(" + esc(nomesP) + ")</code>. " +
    "Ela é " + (n + 1) + "×" + (n + 1) + ": a matriz " + n + "×" + n + " da transformação + uma coluna extra para translação.</li>" +
    "<li>Converter v para coordenada homogênea (<code>toHomogeneous</code>): " + vec(r.entrada) + " → [" +
    r.entradaHomogenea.map(fmt).join(", ") + "].</li>" +
    "<li>Multiplicar H · v<sub>h</sub> (<code>LinearAlgebra.dot</code>), linha por coluna:<ol>" + linhas + "</ol></li>" +
    "<li>Voltar para cartesiano dividindo por w = " + fmt(w) + " (<code>toCartesian</code>): resultado " + vec(r.resultado) + ".</li>" +
    "</ol><p>" + esc(def.descricao) + "</p>";

  html += '<h3 style="margin-top:16px">Código Java (Spring Boot)</h3>' +
    '<p>Este é o código que a API executou. O método <code>' + def.id + "</code> chama <code>dotHomogeneous</code>, " +
    'explicado na aba <a href="#homogeneas">Coordenadas Homogêneas</a>.</p><div id="tf-codigo">carregando…</div>';
  $("tf-explicacao").innerHTML = html;

  try {
    const trechos = await buscarCodigo(def.metodosJava);
    const alvo = $("tf-codigo");
    if (alvo && estado.id === def.id) alvo.innerHTML = trechos.map(blocoJava).join("");
  } catch (e) {
    const alvo = $("tf-codigo");
    if (alvo) alvo.innerHTML = '<p class="mini erro">' + esc(e.message) + "</p>";
  }
}

// ---------------------------------------------------------
// Eventos
// ---------------------------------------------------------
const recalcular = debounce(() => calcular(), 120);

function ligarEventos() {
  $("tf-dimensao").addEventListener("click", e => {
    const b = e.target.closest("button[data-dim]");
    if (b && +b.dataset.dim !== estado.dim) escolherDimensao(+b.dataset.dim);
  });
  $("tf-categorias").addEventListener("click", e => {
    const b = e.target.closest("button[data-cat]");
    if (b) escolherCategoria(b.dataset.cat);
  });
  $("tf-variantes").addEventListener("click", e => {
    const b = e.target.closest("button[data-id]");
    if (b) escolherTransformacao(b.dataset.id);
  });

  // Slider e caixa numérica do mesmo parâmetro andam juntos
  $("tf-parametros").addEventListener("input", e => {
    const nome = e.target.dataset.par;
    if (!nome) return;
    const valor = parseFloat(e.target.value);
    if (isNaN(valor)) return;
    estado.parametros[nome] = valor;
    document.querySelectorAll('#tf-parametros [data-par="' + nome + '"]').forEach(el => {
      if (el !== e.target) el.value = valor;
    });
    recalcular();
  });

  $("tf-vetor").addEventListener("input", e => {
    const i = e.target.dataset.vi;
    if (i === undefined) return;
    const valor = parseFloat(e.target.value);
    estado.vetor[+i] = isNaN(valor) ? 0 : valor;
    recalcular();
  });

  // Slider t: mostra a transformação "pela metade"
  $("tf-t").addEventListener("input", e => {
    if (estado.cena && !estado.cena.animando) estado.cena.desenhar(parseFloat(e.target.value));
  });
  $("tf-animar").addEventListener("click", () => estado.cena && estado.cena.animar());
  $("tf-resetar").addEventListener("click", () => estado.cena && !estado.cena.animando && estado.cena.desenhar(0));
}
