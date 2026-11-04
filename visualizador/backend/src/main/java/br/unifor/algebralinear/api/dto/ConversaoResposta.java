package br.unifor.algebralinear.api.dto;

/** As duas formas do mesmo ponto: cartesiana (x, y) e homogênea [x, y, w]. */
public record ConversaoResposta(double[] cartesiano, double[] homogeneo) {
}
