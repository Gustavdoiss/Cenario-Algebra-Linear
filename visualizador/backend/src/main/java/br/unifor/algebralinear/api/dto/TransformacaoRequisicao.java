package br.unifor.algebralinear.api.dto;

import java.util.Map;

/** JSON recebido: { "vetor": [2, 1], "parametros": { "angle": 45 } } */
public record TransformacaoRequisicao(double[] vetor, Map<String, Double> parametros) {
}
