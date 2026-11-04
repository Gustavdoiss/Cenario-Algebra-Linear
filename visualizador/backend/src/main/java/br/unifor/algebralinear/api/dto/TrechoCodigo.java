package br.unifor.algebralinear.api.dto;

/** Um pedaço do código-fonte Java (um método) para mostrar no front. */
public record TrechoCodigo(String metodo, String arquivo, int linhaInicial, String codigo) {
}
