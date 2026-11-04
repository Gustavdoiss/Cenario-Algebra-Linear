package br.unifor.algebralinear.servico;

import br.unifor.algebralinear.algebra.Transformations;
import br.unifor.algebralinear.algebra.Vector;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Lista de todas as transformações de Transformations.java que a API conhece.
 * O id de cada uma é o próprio nome do método Java.
 */
@Component
public class CatalogoTransformacoes {

    private final List<DefinicaoTransformacao> lista = new ArrayList<>();

    public CatalogoTransformacoes() {
        Parametro dx = new Parametro("dx", "dx (deslocamento em x)", 2, -5, 5, 0.5);
        Parametro dy = new Parametro("dy", "dy (deslocamento em y)", 1, -5, 5, 0.5);
        Parametro dz = new Parametro("dz", "dz (deslocamento em z)", 1, -3, 3, 0.5);
        Parametro angulo = new Parametro("angle", "θ (ângulo em graus)", 45, -180, 180, 5);
        Parametro kx = new Parametro("kx", "kx (y′ ganha kx·x)", 0, -2, 2, 0.1);
        Parametro ky = new Parametro("ky", "ky (x′ ganha ky·y)", 1, -2, 2, 0.1);

        // ---------- TRANSLAÇÃO ----------
        add("translate2D", "Translação 2D", "Translação", 2,
                "Desloca todo o plano: cada ponto anda dx na horizontal e dy na vertical. "
                        + "Não é linear (a origem sai do lugar), por isso precisa de coordenadas homogêneas.",
                List.of(dx, dy),
                p -> Transformations.translate2DMatrix(p.get("dx"), p.get("dy")),
                (v, p) -> Transformations.translate2D(v, p.get("dx"), p.get("dy")));
        add("translate3D", "Translação 3D", "Translação", 3,
                "Desloca todo o espaço: cada ponto anda (dx, dy, dz). A última coluna da matriz 4x4 guarda o deslocamento.",
                List.of(dx, dy, dz),
                p -> Transformations.translate3DMatrix(p.get("dx"), p.get("dy"), p.get("dz")),
                (v, p) -> Transformations.translate3D(v, p.get("dx"), p.get("dy"), p.get("dz")));

        // ---------- ROTAÇÃO ----------
        add("rotation2D", "Rotação 2D", "Rotação", 2,
                "Gira o plano em torno da origem, no sentido anti-horário, sem deformar nada.",
                List.of(angulo),
                p -> Transformations.rotation2DMatrix(p.get("angle")),
                (v, p) -> Transformations.rotation2D(v, p.get("angle")));
        add("rotation3DX", "Rotação 3D em X", "Rotação", 3,
                "Gira o espaço em torno do eixo X: a coordenada x não muda, y e z giram.",
                List.of(angulo),
                p -> Transformations.rotation3DXMatrix(p.get("angle")),
                (v, p) -> Transformations.rotation3DX(v, p.get("angle")));
        add("rotation3DY", "Rotação 3D em Y", "Rotação", 3,
                "Gira o espaço em torno do eixo Y: a coordenada y não muda, x e z giram.",
                List.of(angulo),
                p -> Transformations.rotation3DYMatrix(p.get("angle")),
                (v, p) -> Transformations.rotation3DY(v, p.get("angle")));
        add("rotation3DZ", "Rotação 3D em Z", "Rotação", 3,
                "Gira o espaço em torno do eixo Z: a coordenada z não muda, x e y giram (igual à rotação 2D).",
                List.of(angulo),
                p -> Transformations.rotation3DZMatrix(p.get("angle")),
                (v, p) -> Transformations.rotation3DZ(v, p.get("angle")));

        // ---------- CISALHAMENTO ----------
        add("shearing", "Cisalhamento 2D", "Cisalhamento", 2,
                "Escorrega o plano de lado, como empurrar o topo de um baralho. "
                        + "Nesta matriz: x′ = x + ky·y e y′ = kx·x + y.",
                List.of(kx, ky),
                p -> Transformations.shearingMatrix(p.get("kx"), p.get("ky")),
                (v, p) -> Transformations.shearing(v, p.get("kx"), p.get("ky")));

        // ---------- PROJEÇÃO ----------
        add("projection2DX", "Projeção 2D no eixo X", "Projeção", 2,
                "Achata o plano sobre o eixo X: y vira 0. Informação é perdida, então não dá para desfazer.",
                List.of(),
                p -> Transformations.projection2DXMatrix(),
                (v, p) -> Transformations.projection2DX(v));
        add("projection2DY", "Projeção 2D no eixo Y", "Projeção", 2,
                "Achata o plano sobre o eixo Y: x vira 0.",
                List.of(),
                p -> Transformations.projection2DYMatrix(),
                (v, p) -> Transformations.projection2DY(v));
        add("projection3DX", "Projeção 3D no eixo X", "Projeção", 3,
                "Achata o espaço sobre o eixo X: y e z viram 0 (o cubo vira um segmento).",
                List.of(),
                p -> Transformations.projection3DXMatrix(),
                (v, p) -> Transformations.projection3DX(v));
        add("projection3DY", "Projeção 3D no eixo Y", "Projeção", 3,
                "Achata o espaço sobre o eixo Y: x e z viram 0.",
                List.of(),
                p -> Transformations.projection3DYMatrix(),
                (v, p) -> Transformations.projection3DY(v));
        add("projection3DZ", "Projeção 3D no eixo Z", "Projeção", 3,
                "Achata o espaço sobre o eixo Z: x e y viram 0.",
                List.of(),
                p -> Transformations.projection3DZMatrix(),
                (v, p) -> Transformations.projection3DZ(v));

        // ---------- REFLEXÃO ----------
        add("reflection2DX", "Reflexão 2D no eixo X", "Reflexão", 2,
                "Espelha o plano em relação ao eixo X: y troca de sinal. A orientação inverte (a letra F aparece espelhada).",
                List.of(),
                p -> Transformations.reflection2DXMatrix(),
                (v, p) -> Transformations.reflection2DX(v));
        add("reflection2DY", "Reflexão 2D no eixo Y", "Reflexão", 2,
                "Espelha o plano em relação ao eixo Y: x troca de sinal.",
                List.of(),
                p -> Transformations.reflection2DYMatrix(),
                (v, p) -> Transformations.reflection2DY(v));
        add("reflection3DX", "Reflexão 3D no eixo X", "Reflexão", 3,
                "Reflete em relação ao eixo X: x fica, y e z trocam de sinal (o mesmo que girar 180° em torno de X).",
                List.of(),
                p -> Transformations.reflection3DXMatrix(),
                (v, p) -> Transformations.reflection3DX(v));
        add("reflection3DY", "Reflexão 3D no eixo Y", "Reflexão", 3,
                "Reflete em relação ao eixo Y: y fica, x e z trocam de sinal.",
                List.of(),
                p -> Transformations.reflection3DYMatrix(),
                (v, p) -> Transformations.reflection3DY(v));
        add("reflection3DZ", "Reflexão 3D no eixo Z", "Reflexão", 3,
                "Reflete em relação ao eixo Z: z fica, x e y trocam de sinal.",
                List.of(),
                p -> Transformations.reflection3DZMatrix(),
                (v, p) -> Transformations.reflection3DZ(v));
    }

    private void add(String id, String nome, String categoria, int dimensao, String descricao,
                     List<Parametro> parametros,
                     Function<Map<String, Double>, double[][]> matriz,
                     BiFunction<Vector, Map<String, Double>, Vector> aplicar) {
        // Código mostrado no front: o método que monta a matriz + o método da transformação
        List<String> metodos = List.of(id + "Matrix", id);
        lista.add(new DefinicaoTransformacao(id, nome, categoria, dimensao, descricao, parametros, metodos, matriz, aplicar));
    }

    public List<DefinicaoTransformacao> todas() {
        return lista;
    }

    public Optional<DefinicaoTransformacao> buscar(String id) {
        return lista.stream().filter(t -> t.id().equals(id)).findFirst();
    }
}
