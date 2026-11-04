package br.unifor.algebralinear.api;

import br.unifor.algebralinear.api.dto.ConversaoRequisicao;
import br.unifor.algebralinear.api.dto.ConversaoResposta;
import br.unifor.algebralinear.servico.TransformacaoService;
import org.springframework.web.bind.annotation.*;

/**
 * POST /api/homogeneas/para-homogenea   { "vetor": [x, y] }     -> [x, y, 1]
 * POST /api/homogeneas/para-cartesiana  { "vetor": [x, y, w] }  -> (x/w, y/w)
 */
@RestController
@RequestMapping("/api/homogeneas")
public class HomogeneasController {

    private final TransformacaoService service;

    public HomogeneasController(TransformacaoService service) {
        this.service = service;
    }

    @PostMapping("/para-homogenea")
    public ConversaoResposta paraHomogenea(@RequestBody ConversaoRequisicao req) {
        return service.paraHomogenea(req.vetor());
    }

    @PostMapping("/para-cartesiana")
    public ConversaoResposta paraCartesiana(@RequestBody ConversaoRequisicao req) {
        return service.paraCartesiana(req.vetor());
    }
}
