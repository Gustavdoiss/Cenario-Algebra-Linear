package br.unifor.algebralinear.api.dto;

import java.util.Map;

/**
 * JSON devolvido para o front.
 *
 * matriz            -> matriz homogênea (3x3 no 2D, 4x4 no 3D)
 * entradaHomogenea  -> vetor de entrada com o 1 no final: [x, y, 1]
 * saidaHomogenea    -> matriz · entradaHomogenea
 * resultado         -> volta para cartesiano (divide por w)
 */
public record TransformacaoResposta(
        String id,
        String nome,
        Map<String, Double> parametros,
        double[] entrada,
        double[][] matriz,
        double[] entradaHomogenea,
        double[] saidaHomogenea,
        double[] resultado
) {
}
