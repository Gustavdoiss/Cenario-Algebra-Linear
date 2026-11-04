package br.unifor.algebralinear.servico;

import br.unifor.algebralinear.algebra.Vector;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Descreve uma transformação do catálogo.
 *
 * - matriz:  função que monta a matriz homogênea a partir dos parâmetros
 *            (parecido com uma arrow function do JS: p -> ...).
 * - aplicar: função que chama o método de Transformations.java.
 *
 * As duas funções têm @JsonIgnore porque não dá para mandar código em JSON;
 * o resto (nome, parâmetros, métodos...) vai para o front.
 */
public record DefinicaoTransformacao(
        String id,
        String nome,
        String categoria,
        int dimensao,
        String descricao,
        List<Parametro> parametros,
        List<String> metodosJava,
        @JsonIgnore Function<Map<String, Double>, double[][]> matriz,
        @JsonIgnore BiFunction<Vector, Map<String, Double>, Vector> aplicar
) {
}
