package br.unifor.algebralinear.servico;

import br.unifor.algebralinear.algebra.LinearAlgebra;
import br.unifor.algebralinear.algebra.Matrix;
import br.unifor.algebralinear.algebra.Transformations;
import br.unifor.algebralinear.algebra.Vector;
import br.unifor.algebralinear.api.dto.ConversaoResposta;
import br.unifor.algebralinear.api.dto.TransformacaoResposta;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Faz os cálculos usando as classes do repositório (Matrix, Vector, LinearAlgebra, Transformations).
 * O controller só recebe o JSON e chama este serviço.
 */
@Service
public class TransformacaoService {

    private final CatalogoTransformacoes catalogo;

    // O Spring entrega o catálogo pronto aqui (injeção de dependência)
    public TransformacaoService(CatalogoTransformacoes catalogo) {
        this.catalogo = catalogo;
    }

    public List<DefinicaoTransformacao> listar() {
        return catalogo.todas();
    }

    public TransformacaoResposta aplicar(String id, double[] vetor, Map<String, Double> parametrosRecebidos) {
        DefinicaoTransformacao def = catalogo.buscar(id)
                .orElseThrow(() -> new IllegalArgumentException("Transformação desconhecida: " + id));

        if (vetor == null || vetor.length != def.dimensao()) {
            throw new IllegalArgumentException("O vetor precisa ter " + def.dimensao() + " coordenadas para " + id + ".");
        }

        // Parâmetros que não vieram na requisição usam o valor padrão do catálogo
        Map<String, Double> p = new HashMap<>();
        for (Parametro par : def.parametros()) {
            Double valor = parametrosRecebidos == null ? null : parametrosRecebidos.get(par.nome());
            p.put(par.nome(), valor == null ? par.padrao() : valor);
        }

        Vector v = new Vector(vetor, vetor.length);

        // Resultado "oficial": chama o método de Transformations.java (ex.: Transformations.rotation2D)
        Vector resultado = def.aplicar().apply(v, p);

        // Passo a passo (mesmos passos de dotHomogeneous) para o front explicar a conta
        double[][] mData = def.matriz().apply(p);
        Matrix vHomogeneo = Transformations.toHomogeneous(v);
        Matrix resHomogeneo = LinearAlgebra.dot(new Matrix(mData.length, mData.length, mData), vHomogeneo);

        return new TransformacaoResposta(
                def.id(),
                def.nome(),
                p,
                vetor,
                mData,
                coluna(vHomogeneo),
                coluna(resHomogeneo),
                paraArray(resultado));
    }

    public ConversaoResposta paraHomogenea(double[] cartesiano) {
        if (cartesiano == null || cartesiano.length < 1) {
            throw new IllegalArgumentException("Informe um vetor com pelo menos 1 coordenada.");
        }
        Matrix h = Transformations.toHomogeneous(new Vector(cartesiano, cartesiano.length));
        return new ConversaoResposta(cartesiano, coluna(h));
    }

    public ConversaoResposta paraCartesiana(double[] homogeneo) {
        if (homogeneo == null || homogeneo.length < 2) {
            throw new IllegalArgumentException("Informe um vetor homogêneo com pelo menos 2 coordenadas (a última é w).");
        }
        double[][] dados = new double[homogeneo.length][1];
        for (int i = 0; i < homogeneo.length; i++) {
            dados[i][0] = homogeneo[i];
        }
        Vector c = Transformations.toCartesian(new Matrix(homogeneo.length, 1, dados));
        return new ConversaoResposta(paraArray(c), homogeneo);
    }

    // ---------- conversões para mandar em JSON ----------

    private static double[] coluna(Matrix m) {
        double[] r = new double[m.getRows()];
        for (int i = 1; i <= m.getRows(); i++) {
            r[i - 1] = m.get(i, 1);
        }
        return r;
    }

    private static double[] paraArray(Vector v) {
        double[] r = new double[v.getDim()];
        for (int i = 1; i <= v.getDim(); i++) {
            r[i - 1] = v.get(i);
        }
        return r;
    }
}
