package br.unifor.algebralinear.api;

import br.unifor.algebralinear.api.dto.TrechoCodigo;
import br.unifor.algebralinear.servico.CodigoFonteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GET /api/codigo?metodos=translate2DMatrix,translate2D
 * Devolve o código Java de cada método pedido.
 */
@RestController
@RequestMapping("/api/codigo")
public class CodigoController {

    private final CodigoFonteService service;

    public CodigoController(CodigoFonteService service) {
        this.service = service;
    }

    @GetMapping
    public List<TrechoCodigo> buscar(@RequestParam List<String> metodos) {
        return metodos.stream().map(service::buscarMetodo).toList();
    }
}
