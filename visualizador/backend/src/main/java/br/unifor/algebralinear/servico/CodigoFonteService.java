package br.unifor.algebralinear.servico;

import br.unifor.algebralinear.api.dto.TrechoCodigo;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Lê o código-fonte das classes de álgebra (copiado para "codigo-fonte/" pelo Maven, veja o pom.xml)
 * e devolve só o trecho de um método. Assim o front mostra exatamente o código que roda na API.
 */
@Service
public class CodigoFonteService {

    // Arquivos onde procurar, em ordem
    private static final List<String> ARQUIVOS = List.of(
            "Transformations.java", "LinearAlgebra.java", "Matrix.java", "Vector.java");

    public TrechoCodigo buscarMetodo(String metodo) {
        if (!metodo.matches("[A-Za-z0-9_]+")) {
            throw new IllegalArgumentException("Nome de método inválido: " + metodo);
        }
        // Procura uma linha como "public static Vector translate2D(" ou "private static Matrix toMatrix("
        Pattern assinatura = Pattern.compile("^\\s*(public|private|protected)\\s+static\\s+[\\w\\[\\]<>]+\\s+"
                + metodo + "\\s*\\(");

        for (String arquivo : ARQUIVOS) {
            List<String> linhas = ler(arquivo);
            for (int i = 0; i < linhas.size(); i++) {
                if (assinatura.matcher(linhas.get(i)).find()) {
                    return recortar(arquivo, linhas, i, metodo);
                }
            }
        }
        throw new IllegalArgumentException("Método não encontrado no código-fonte: " + metodo);
    }

    /** Recorta do início do método (com os comentários logo acima) até a chave que fecha. */
    private TrechoCodigo recortar(String arquivo, List<String> linhas, int inicio, String metodo) {
        // Inclui comentários "//" colados acima da assinatura
        int primeira = inicio;
        while (primeira > 0 && linhas.get(primeira - 1).trim().startsWith("//")
                && !linhas.get(primeira - 1).contains("====")) {
            primeira--;
        }

        // Conta as chaves { } até fechar o método
        int profundidade = 0;
        boolean abriu = false;
        int ultima = inicio;
        for (int i = inicio; i < linhas.size(); i++) {
            for (char ch : linhas.get(i).toCharArray()) {
                if (ch == '{') { profundidade++; abriu = true; }
                if (ch == '}') profundidade--;
            }
            if (abriu && profundidade == 0) {
                ultima = i;
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = primeira; i <= ultima; i++) {
            // tira os 4 espaços de indentação da classe
            String l = linhas.get(i);
            sb.append(l.startsWith("    ") ? l.substring(4) : l).append('\n');
        }
        return new TrechoCodigo(metodo, arquivo, primeira + 1, sb.toString().stripTrailing());
    }

    private List<String> ler(String arquivo) {
        try (InputStream in = new ClassPathResource("codigo-fonte/" + arquivo).getInputStream()) {
            String texto = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            return new ArrayList<>(texto.lines().toList());
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível ler " + arquivo, e);
        }
    }
}
