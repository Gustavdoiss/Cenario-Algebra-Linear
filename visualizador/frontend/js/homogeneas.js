// =========================================================
// ABA "Coordenadas Homogêneas"
// Mostra o código Java da conversão + dois conversores que chamam a API
// =========================================================
import { paraHomogenea, paraCartesiana, buscarCodigo } from "./api.js";
import { $, fmt, vec, blocoJava, debounce, esc } from "./util.js";
import { CenaHomogenea } from "./cenaHomogenea.js";

const EIXOS = ["x", "y", "z"];
const estado = { dim: 2, cart: [3, 2], hom: [4, 2, 2], cena: null };

// Cartões de código: título, explicação e os métodos Java (buscados na API)
const CARTOES = [
  {
    titulo: "Visão geral: dotHomogeneous",
    texto: "Todas as transformações de <code>Transformations.java</code> terminam chamando este método. " +
      "Ele recebe a matriz homogênea (3×3 no 2D, 4×4 no 3D) e o vetor cartesiano, e faz 3 passos:",
    passos: [
      "Confere se a matriz tem tamanho (dim + 1) × (dim + 1). Um vetor 2D precisa de matriz 3×3.",
      "Passo 1 — <code>toHomogeneous</code>: (x ; y) vira a coluna [x, y, 1].",
      "Passo 2 — <code>LinearAlgebra.dot</code>: multiplica a matriz pela coluna.",
      "Passo 3 — <code>toCartesian</code>: divide tudo por w e joga o w fora."
    ],
    metodos: ["dotHomogeneous"]
  },
  {
    titulo: "Passo 1 — cartesiano → homogêneo",
    texto: "Cria uma matriz-coluna com uma linha a mais que o vetor. As primeiras linhas recebem as coordenadas " +
      "e a última recebe <b>1</b>. Esse 1 é o que permite a última coluna da matriz (dx, dy) ser somada no resultado.",
    passos: [
      "<code>vData</code> tem <code>dim + 1</code> linhas e 1 coluna.",
      "O <code>for</code> começa em 1 porque <code>Vector.get</code> usa índices a partir de 1 (como na matemática).",
      "<code>vData[dim][0] = 1.0</code> é a coordenada w."
    ],
    metodos: ["toHomogeneous"]
  },
  {
    titulo: "Passo 2 — multiplicação de matrizes",
    texto: "A multiplicação usada é a mesma da classe <code>LinearAlgebra</code> do trabalho: " +
      "cada elemento do resultado é a linha da matriz vezes a coluna do vetor (multiplica e soma).",
    passos: [
      "Só funciona se colunas de A = linhas de B (por isso a matriz é (dim + 1) × (dim + 1)).",
      "Com a translação 2D: x′ = 1·x + 0·y + dx·1 → o 1 do passo anterior \"puxa\" o dx para dentro da conta."
    ],
    metodos: ["dot"]
  },
  {
    titulo: "Passo 3 — homogêneo → cartesiano",
    texto: "Lê o w (última linha) e divide as outras coordenadas por ele. Nas transformações deste trabalho a última " +
      "linha da matriz é sempre [0 … 0 1], então w continua 1. Mas [4, 2, 2] e [2, 1, 1] são o <b>mesmo ponto</b> (2 ; 1)!",
    passos: [
      "Se w = 0 não dá para dividir: o ponto está \"no infinito\" (é uma direção). A API responde erro 400.",
      "O resultado tem uma coordenada a menos que a entrada."
    ],
    metodos: ["toCartesian"]
  },
  {
    titulo: "Auxiliares: vetor ↔ matriz-coluna",
    texto: "Métodos originais do repositório que convertem um <code>Vector</code> em uma <code>Matrix</code> de uma coluna " +
      "e vice-versa. É a mesma ideia do passo 1, mas sem acrescentar o w.",
    passos: [],
    metodos: ["toMatrix", "toVector"]
  }
];

export async function iniciar() {
  ligarEventos();
  montarEntradas();
  await mostrarCodigos();
}

export function mostrar() {
  // A cena só é criada quando a aba aparece (o canvas precisa ter tamanho)
  if (!estado.cena) {
    try {
      estado.cena = new CenaHomogenea($("hg-cena"));
    } catch (e) {
      console.error(e);
      $("hg-cena").innerHTML = '<div class="msg">Não foi possível iniciar o manim-web (WebGL): ' + esc(e.message) + "</div>";
    }
    converter();
  } else {
    estado.cena.redimensionar();
  }
}

export function redimensionar() {
  if (estado.cena && !$("pg-homogeneas").hidden) estado.cena.redimensionar();
}

function montarEntradas() {
  const n = estado.dim;
  const campo = (grupo, i, rotulo, valor) =>
    '<label class="f"><span>' + rotulo + '</span><input type="number" step="any" data-g="' + grupo +
    '" data-i="' + i + '" value="' + valor + '"></label>';
  $("hg-cart").innerHTML = estado.cart.map((x, i) => campo("cart", i, EIXOS[i], x)).join("");
  $("hg-hom").innerHTML = estado.hom.map((x, i) => campo("hom", i, i === n ? "w" : EIXOS[i], x)).join("");
  document.querySelectorAll("#hg-dimensao button").forEach(b => b.classList.toggle("on", +b.dataset.dim === n));

  const cena = $("hg-cena");
  let aviso = cena.querySelector(".msg.dim3");
  if (n === 3 && !aviso) {
    aviso = document.createElement("div");
    aviso.className = "msg dim3";
    aviso.style.background = "rgba(28,28,26,.92)";
    aviso.textContent = "No 3D o ponto homogêneo tem 4 coordenadas [x, y, z, w]: não dá para desenhar 4D 🙂. " +
      "A conta é exatamente a mesma: divide x, y e z por w.";
    cena.appendChild(aviso);
  }
  if (n === 2 && aviso) aviso.remove();
  $("hg-animar").disabled = n === 3;
}

async function converter() {
  // Cartesiano -> homogêneo (POST /api/homogeneas/para-homogenea)
  try {
    const r = await paraHomogenea(estado.cart);
    $("hg-cart-res").className = "mini";
    $("hg-cart-res").innerHTML = vec(r.cartesiano) + " → [" + r.homogeneo.map(fmt).join(", ") + "]";
  } catch (e) {
    $("hg-cart-res").className = "mini erro";
    $("hg-cart-res").textContent = e.message;
  }
  // Homogêneo -> cartesiano (POST /api/homogeneas/para-cartesiana)
  const w = estado.hom[estado.dim];
  try {
    const r = await paraCartesiana(estado.hom);
    $("hg-hom-res").className = "mini";
    $("hg-hom-res").innerHTML = "[" + r.homogeneo.map(fmt).join(", ") + "] → " +
      r.cartesiano.map((c, i) => fmt(r.homogeneo[i]) + "/" + fmt(w)).join(" ; ") + " = " + vec(r.cartesiano);
  } catch (e) {
    $("hg-hom-res").className = "mini erro";
    $("hg-hom-res").textContent = "API: " + e.message;
  }
  if (estado.cena && estado.dim === 2 && !estado.cena.animando) {
    estado.cena.definir(estado.hom);
    estado.cena.desenhar(0);
  }
}

async function mostrarCodigos() {
  const alvo = $("hg-codigos");
  try {
    const todos = await buscarCodigo(CARTOES.flatMap(c => c.metodos));
    const porNome = Object.fromEntries(todos.map(t => [t.metodo, t]));
    alvo.innerHTML = CARTOES.map(c =>
      '<article class="codigo-card"><h3>' + c.titulo + "</h3><p>" + c.texto + "</p>" +
      (c.passos.length ? "<ol>" + c.passos.map(s => "<li>" + s + "</li>").join("") + "</ol>" : "") +
      c.metodos.map(m => blocoJava(porNome[m])).join("") + "</article>"
    ).join("");
  } catch (e) {
    alvo.innerHTML = '<p class="mini erro">' + esc(e.message) + "</p>";
  }
}

const converterDepois = debounce(converter, 150);

function ligarEventos() {
  $("hg-dimensao").addEventListener("click", e => {
    const b = e.target.closest("button[data-dim]");
    if (!b || +b.dataset.dim === estado.dim) return;
    estado.dim = +b.dataset.dim;
    estado.cart = estado.dim === 2 ? [3, 2] : [3, 2, 1];
    estado.hom = estado.dim === 2 ? [4, 2, 2] : [4, 2, 6, 2];
    montarEntradas();
    converter();
  });
  document.querySelector("#pg-homogeneas .manipulacao").addEventListener("input", e => {
    const { g, i } = e.target.dataset;
    if (!g) return;
    const valor = parseFloat(e.target.value);
    estado[g][+i] = isNaN(valor) ? 0 : valor;
    converterDepois();
  });
  $("hg-animar").addEventListener("click", () => estado.cena && estado.cena.animar());
}
