package br.unifor.algebralinear.api;

import br.unifor.algebralinear.api.dto.TransformacaoRequisicao;
import br.unifor.algebralinear.api.dto.TransformacaoResposta;
import br.unifor.algebralinear.servico.DefinicaoTransformacao;
import br.unifor.algebralinear.servico.TransformacaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GET  /api/transformacoes          -> lista de transformações (para montar os botões do front)
 * POST /api/transformacoes/{id}     -> aplica a transformação {id} a um vetor
 */
@RestController
@RequestMapping("/api/transformacoes")
public class TransformacaoController {

    private final TransformacaoService service;

    public TransformacaoController(TransformacaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<DefinicaoTransformacao> listar() {
        return service.listar();
    }

    @PostMapping("/{id}")
    public TransformacaoResposta aplicar(@PathVariable String id, @RequestBody TransformacaoRequisicao req) {
        return service.aplicar(id, req.vetor(), req.parametros());
    }
}
