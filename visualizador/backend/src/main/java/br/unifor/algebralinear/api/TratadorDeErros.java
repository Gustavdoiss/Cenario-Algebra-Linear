package br.unifor.algebralinear.api;

import br.unifor.algebralinear.api.dto.Erro;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Quando o código de álgebra lança IllegalArgumentException (ex.: dimensão errada, w = 0),
 * a API responde 400 com { "erro": "mensagem" } em vez de um erro 500 genérico.
 */
@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler({IllegalArgumentException.class, IndexOutOfBoundsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Erro argumentoInvalido(RuntimeException e) {
        return new Erro(e.getMessage());
    }
}
