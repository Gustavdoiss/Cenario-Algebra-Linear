// =========================================================
// Ponto de entrada do front-end: verifica a API e troca de aba pelo #hash da URL
// =========================================================
import * as Transformacoes from "./transformacoes.js";
import * as Homogeneas from "./homogeneas.js";
import { $, debounce } from "./util.js";

const PAGINAS = { transformacoes: Transformacoes, homogeneas: Homogeneas };
const TITULOS = { transformacoes: "Transformações Lineares", homogeneas: "Coordenadas Homogêneas" };

function irPara(chamarMostrar = true) {
  const nome = PAGINAS[location.hash.slice(1)] ? location.hash.slice(1) : "transformacoes";
  Object.keys(PAGINAS).forEach(n => { $("pg-" + n).hidden = n !== nome; });
  document.querySelectorAll("nav a").forEach(a => {
    if (a.dataset.p === nome) a.setAttribute("aria-current", "page"); else a.removeAttribute("aria-current");
  });
  document.title = TITULOS[nome] + " | Visualizador de Álgebra Linear";
  if (chamarMostrar) PAGINAS[nome].mostrar();
}

async function iniciar() {
  irPara(false);   // deixa a aba visível antes de criar o canvas (ele precisa ter largura)
  try {
    // As duas abas buscam dados na API; se ela estiver desligada, avisa o usuário
    await Promise.all([Transformacoes.iniciar(), Homogeneas.iniciar()]);
    $("api-status").textContent = "API: conectada";
    $("api-status").className = "api ok";
  } catch (e) {
    console.error(e);
    $("api-status").textContent = "API: desligada";
    $("api-status").className = "api erro";
    $("aviso").hidden = false;
    $("aviso").innerHTML = "Não consegui falar com a API Spring Boot (" + e.message + "). " +
      "Abra um terminal em <code>visualizador/backend</code> e rode <code>mvn spring-boot:run</code>, depois recarregue a página.";
  }
  window.addEventListener("hashchange", () => irPara());
  window.addEventListener("resize", debounce(() => {
    Transformacoes.redimensionar();
    Homogeneas.redimensionar();
  }, 200));
  irPara();
}

iniciar();
