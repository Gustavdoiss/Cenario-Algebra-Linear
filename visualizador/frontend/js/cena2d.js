// =========================================================
// Cena 2D com manim-web: plano, grade que se transforma, î, ĵ, vetor v e a letra F
// =========================================================
import { Scene, NumberPlane, Line, Arrow, Polygon, Dot, Animation, smooth } from "./manim.js";
import { interpolar, transformarPonto, identidade, p3, distancia } from "./matematica.js";
import { COR } from "./cores.js";
import { tamanhoCena } from "./util.js";

const LIMITE = 6;   // o plano vai de -6 a 6
const GRADE = 12;   // a grade transformada é maior, para não "acabar" quando gira

// Letra F: não tem simetria, então dá para ver reflexões e rotações
const LETRA_F = [[0, 0], [0.4, 0], [0.4, 0.8], [1.2, 0.8], [1.2, 1.2], [0.4, 1.2], [0.4, 1.6], [1.6, 1.6], [1.6, 2], [0, 2]];

// Animação do manim-web: a cada quadro, chama cena.desenhar(alpha) com alpha indo de 0 até 1
export class AnimacaoMatriz extends Animation {
  constructor(cena, opcoes) {
    super(cena.pecaPrincipal, opcoes);
    this.cena = cena;
  }
  interpolate(alpha) {
    this.cena.desenhar(alpha);
  }
}

export class Cena2D {
  constructor(el) {
    this.el = el;
    const lado = tamanhoCena(el);
    this.scene = new Scene(el, {
      width: lado, height: lado,
      frameWidth: 2 * LIMITE, frameHeight: 2 * LIMITE,
      backgroundColor: COR.fundo
    });
    this.H = identidade(3);
    this.v = [2, 1];
    this.t = 1;

    // Fundo: plano cartesiano fixo (o "antes")
    this.plano = new NumberPlane({
      xRange: [-LIMITE, LIMITE, 1], yRange: [-LIMITE, LIMITE, 1],
      xLength: 2 * LIMITE, yLength: 2 * LIMITE,
      color: "#9a9a92",
      backgroundLineStyle: { color: "#3a3a36", strokeWidth: 1, opacity: 1 }
    });
    this.scene.add(this.plano);

    // Grade que acompanha a transformação: guardamos os pontos originais de cada reta
    this.grade = [];
    for (let k = -GRADE; k <= GRADE; k++) {
      this.grade.push({ a: [k, -GRADE], b: [k, GRADE] });   // vertical
      this.grade.push({ a: [-GRADE, k], b: [GRADE, k] });   // horizontal
    }
    this.grade.forEach(g => {
      g.linha = new Line({ start: p3(g.a), end: p3(g.b), color: COR.grade, strokeWidth: k0(g) ? 2.5 : 1.2 });
      this.scene.add(g.linha);
    });

    // Letra F: contorno do "antes" (cinza) e a que se transforma (amarela)
    this.fAntes = new Polygon({ vertices: LETRA_F.map(p3), color: COR.antes, fillOpacity: 0, strokeWidth: 2 });
    this.f = new Polygon({ vertices: LETRA_F.map(p3), color: COR.forma, fillOpacity: 0.35, strokeWidth: 2 });
    this.scene.add(this.fAntes, this.f);

    // Vetores: î, ĵ e v
    this.setas = {
      i: this.novaSeta(COR.i),
      j: this.novaSeta(COR.j),
      v: this.novaSeta(COR.v)
    };
    this.ponta = new Dot({ point: [2, 1, 0], color: COR.v, radius: 0.09 });
    this.scene.add(this.ponta);

    this.pecaPrincipal = this.f;
    this.desenhar(1);
  }

  novaSeta(cor) {
    const seta = new Arrow({ start: [0, 0, 0], end: [1, 0, 0], color: cor, strokeWidth: 5 });
    seta.visivel = false;
    return seta;
  }

  // Seta com comprimento zero (ex.: depois de uma projeção) dá problema: escondemos
  moverSeta(seta, inicio, fim) {
    const aparece = distancia(inicio, fim) > 0.05;
    if (aparece) seta.putStartAndEndOn(p3(inicio), p3(fim));
    if (aparece && !seta.visivel) this.scene.add(seta);
    if (!aparece && seta.visivel) this.scene.remove(seta);
    seta.visivel = aparece;
  }

  definirMatriz(H) { this.H = H; }
  definirVetor(v) { this.v = v; }

  // Desenha o estado "t" da transformação (0 = antes, 1 = depois)
  desenhar(t) {
    this.t = t;
    const M = interpolar(this.H, t);
    const f = q => transformarPonto(M, q);

    this.grade.forEach(g => {
      g.linha.setStart(p3(f(g.a)));
      g.linha.setEnd(p3(f(g.b)));
    });
    this.f.setPointsAsCorners([...LETRA_F, LETRA_F[0]].map(q => p3(f(q))));

    const o = f([0, 0]);
    this.moverSeta(this.setas.i, o, f([1, 0]));
    this.moverSeta(this.setas.j, o, f([0, 1]));
    // v é um PONTO transformado: a seta sai da origem e vai até H·v
    const fv = f(this.v);
    this.moverSeta(this.setas.v, [0, 0], fv);
    this.ponta.moveTo(p3(fv));

    if (this.aoMudarT) this.aoMudarT(t);
    this.scene.render();
  }

  // Anima de t = 0 até t = 1 usando o scene.play do manim-web
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

// Os eixos da grade (k = 0) ficam mais grossos
function k0(g) { return (g.a[0] === 0 && g.b[0] === 0) || (g.a[1] === 0 && g.b[1] === 0); }
