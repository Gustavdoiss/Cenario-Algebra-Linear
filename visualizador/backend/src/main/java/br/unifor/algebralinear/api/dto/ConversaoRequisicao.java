package br.unifor.algebralinear.api.dto;

/** JSON recebido: { "vetor": [2, 1] } (cartesiano) ou { "vetor": [4, 2, 2] } (homogêneo) */
public record ConversaoRequisicao(double[] vetor) {
}
