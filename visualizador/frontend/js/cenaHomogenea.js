// =========================================================
// Cena das coordenadas homogêneas (2D):
// o ponto [x, y, w] vive no espaço 3D; dividir por w = andar pela reta
// que passa pela origem até furar o plano w = 1.
// =========================================================
import { ThreeDScene, ThreeDAxes, Line3D, Dot3D, Animation, smooth } from "./manim.js";
import { tamanhoCena } from "./util.js";

const COR_P = "#fb923c";   // ponto homogêneo (laranja)
const COR_Q = "#4ade80";   // ponto no plano w = 1 (verde)

class AnimacaoDivisao extends Animation {
  constructor(cena, opcoes) { super(cena.ponto, opcoes); this.cena = cena; }
  interpolate(alpha) { this.cena.desenhar(alpha); }
}

export class CenaHomogenea {
  constructor(el) {
    this.el = el;
    const lado = tamanhoCena(el);
    this.scene = new ThreeDScene(el, {
      width: lado, height: lado, backgroundColor: "#1c1c1a",
      phi: 1.1, theta: -1.1, distance: 14, enableOrbitControls: true
    });
    this.h = [4, 2, 2];

    this.scene.add(new ThreeDAxes({
      xRange: [-4, 4, 1], yRange: [-4, 4, 1], zRange: [-1, 4, 1],
      axisColor: "#9a9a92", showLabels: true, labels: { x: "x", y: "y", z: "w" }
    }));

    // Plano w = 1 desenhado como uma grade
    for (let k = -3; k <= 3; k++) {
      this.scene.add(new Line3D({ start: [k, -3, 1], end: [k, 3, 1], color: "#3b82f6", lineWidth: k === 0 ? 2.5 : 1.2 }));
      this.scene.add(new Line3D({ start: [-3, k, 1], end: [3, k, 1], color: "#3b82f6", lineWidth: k === 0 ? 2.5 : 1.2 }));
    }

    this.reta = new Line3D({ start: [0, 0, 0], end: [1, 1, 1], color: "#a4a49c", lineWidth: 1.5 });
    this.alvo = new Dot3D({ point: [0, 0, 1], color: COR_Q, radius: 0.12 });
    this.ponto = new Dot3D({ point: [0, 0, 1], color: COR_P, radius: 0.14 });
    this.scene.add(this.reta, this.alvo, this.ponto);
    this.desenhar(0);
  }

  definir(h) { this.h = h; }

  // t = 0: ponto em [x, y, w] · t = 1: ponto em [x/w, y/w, 1]
  desenhar(t) {
    const [x, y, w] = this.h;
    const P = [x, y, w];
    const Q = w !== 0 ? [x / w, y / w, 1] : null;
    // A reta vai da origem até um pouco depois do ponto mais longe
    const longe = Q && Math.hypot(...Q) > Math.hypot(...P) ? Q : P;
    const s = 1.25;
    this.reta.setEnd([longe[0] * s, longe[1] * s, longe[2] * s]);
    if (Q) {
      this.alvo.moveToPoint(Q);
      this.ponto.moveToPoint(P.map((c, i) => (1 - t) * c + t * Q[i]));
    } else {
      this.ponto.moveToPoint(P);
    }
    this.scene.render();
  }

  async animar() {
    if (this.animando || this.h[2] === 0) return;
    this.animando = true;
    try {
      this.desenhar(0);
      await this.scene.play(new AnimacaoDivisao(this, { duration: 1.5, rateFunc: smooth }));
    } finally {
      this.animando = false;
    }
  }

  redimensionar() {
    const lado = tamanhoCena(this.el);
    this.scene.resize(lado, lado);
    this.scene.render();
  }
}
