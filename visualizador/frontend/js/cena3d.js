// =========================================================
// Cena 3D com manim-web: eixos, cubo unitário, î, ĵ, k̂ e vetor v
// (dá para girar a câmera arrastando com o mouse)
// =========================================================
import { ThreeDScene, ThreeDAxes, Line3D, Arrow3D, Dot3D, smooth } from "./manim.js";
import { interpolar, transformarPonto, identidade, distancia } from "./matematica.js";
import { AnimacaoMatriz } from "./cena2d.js";
import { COR } from "./cores.js";
import { tamanhoCena } from "./util.js";

// 8 vértices e 12 arestas do cubo [0,1]³
const VERTICES = [[0, 0, 0], [1, 0, 0], [1, 1, 0], [0, 1, 0], [0, 0, 1], [1, 0, 1], [1, 1, 1], [0, 1, 1]];
const ARESTAS = [[0, 1], [1, 2], [2, 3], [3, 0], [4, 5], [5, 6], [6, 7], [7, 4], [0, 4], [1, 5], [2, 6], [3, 7]];

export class Cena3D {
  constructor(el) {
    this.el = el;
    const lado = tamanhoCena(el);
    this.scene = new ThreeDScene(el, {
      width: lado, height: lado,
      backgroundColor: COR.fundo,
      phi: 1.15, theta: -0.75, distance: 9,
      enableOrbitControls: true
    });
    this.H = identidade(4);
    this.v = [1, 2, 1];

    this.eixos = new ThreeDAxes({
      xRange: [-4, 4, 1], yRange: [-4, 4, 1], zRange: [-3, 3, 1],
      xColor: COR.i, yColor: COR.j, zColor: COR.k, axisColor: "#9a9a92",
      showLabels: true
    });
    this.scene.add(this.eixos);

    // Cubo "antes" (cinza) e cubo que se transforma (amarelo)
    ARESTAS.forEach(([a, b]) =>
      this.scene.add(new Line3D({ start: VERTICES[a], end: VERTICES[b], color: COR.antes, lineWidth: 2 })));
    this.arestas = ARESTAS.map(([a, b]) => {
      const l = new Line3D({ start: VERTICES[a], end: VERTICES[b], color: COR.forma, lineWidth: 3 });
      this.scene.add(l);
      return { l, a: VERTICES[a], b: VERTICES[b] };
    });

    this.setas = {
      i: this.novaSeta(COR.i), j: this.novaSeta(COR.j), k: this.novaSeta(COR.k), v: this.novaSeta(COR.v)
    };
    this.ponta = new Dot3D({ point: [1, 2, 1], color: COR.v, radius: 0.1 });
    this.scene.add(this.ponta);

    this.pecaPrincipal = this.ponta;
    this.desenhar(1);
  }

  novaSeta(cor) {
    const s = new Arrow3D({ start: [0, 0, 0], end: [1, 0, 0], color: cor, tipLength: 0.25, tipRadius: 0.09, shaftRadius: 0.025 });
    s.visivel = false;
    return s;
  }

  moverSeta(seta, inicio, fim) {
    const aparece = distancia(inicio, fim) > 0.05;
    if (aparece) { seta.setStart(inicio); seta.setEnd(fim); }
    if (aparece && !seta.visivel) this.scene.add(seta);
    if (!aparece && seta.visivel) this.scene.remove(seta);
    seta.visivel = aparece;
  }

  definirMatriz(H) { this.H = H; }
  definirVetor(v) { this.v = v; }

  desenhar(t) {
    const M = interpolar(this.H, t);
    const f = q => transformarPonto(M, q);

    this.arestas.forEach(({ l, a, b }) => { l.setStart(f(a)); l.setEnd(f(b)); });
    const o = f([0, 0, 0]);
    this.moverSeta(this.setas.i, o, f([1, 0, 0]));
    this.moverSeta(this.setas.j, o, f([0, 1, 0]));
    this.moverSeta(this.setas.k, o, f([0, 0, 1]));
    const fv = f(this.v);
    this.moverSeta(this.setas.v, [0, 0, 0], fv);
    this.ponta.moveToPoint(fv);

    if (this.aoMudarT) this.aoMudarT(t);
    this.scene.render();
  }

  async animar(duracao = 1.6) {
    if (this.animando) return;
    this.animando = true;
    this.desenhar(0);
    try {
      await this.scene.play(new AnimacaoMatriz(this, { duration: duracao, rateFunc: smooth }));
    } finally {
      this.animando = false;
      this.desenhar(1);
    }
  }

  redimensionar() {
    const lado = tamanhoCena(this.el);
    this.scene.resize(lado, lado);
    this.scene.render();
  }

  destruir() { this.scene.dispose(); this.el.innerHTML = ""; }
}
