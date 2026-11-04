package br.unifor.algebralinear.servico;

/**
 * Um parâmetro de uma transformação (ex.: o ângulo da rotação).
 * O front usa min/max/passo para montar o controle deslizante.
 */
public record Parametro(String nome, String rotulo, double padrao, double min, double max, double passo) {
}
