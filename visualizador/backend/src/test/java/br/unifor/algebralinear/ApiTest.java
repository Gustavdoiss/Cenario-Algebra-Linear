package br.unifor.algebralinear;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiTest {

    @Autowired
    MockMvc mvc;

    @Test
    void listaTodasAsTransformacoes() throws Exception {
        mvc.perform(get("/api/transformacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(17)))
                .andExpect(jsonPath("$[0].id").value("translate2D"))
                .andExpect(jsonPath("$[0].matriz").doesNotExist());
    }

    @Test
    void aplicaTranslacao() throws Exception {
        mvc.perform(post("/api/transformacoes/translate2D").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vetor\":[2,1],\"parametros\":{\"dx\":3,\"dy\":-1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matriz[0][2]").value(3.0))
                .andExpect(jsonPath("$.entradaHomogenea", contains(2.0, 1.0, 1.0)))
                .andExpect(jsonPath("$.saidaHomogenea", contains(5.0, 0.0, 1.0)))
                .andExpect(jsonPath("$.resultado", contains(5.0, 0.0)));
    }

    @Test
    void vetorComDimensaoErradaDa400() throws Exception {
        mvc.perform(post("/api/transformacoes/rotation3DX").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vetor\":[2,1]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro", containsString("3 coordenadas")));
    }

    @Test
    void conversaoHomogenea() throws Exception {
        mvc.perform(post("/api/homogeneas/para-cartesiana").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vetor\":[4,2,2]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartesiano", contains(2.0, 1.0)));
        mvc.perform(post("/api/homogeneas/para-cartesiana").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vetor\":[4,2,0]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void devolveCodigoJava() throws Exception {
        mvc.perform(get("/api/codigo").param("metodos", "rotation2DMatrix,dotHomogeneous,toMatrix,dot"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo", startsWith("public static double[][] rotation2DMatrix(double angle)")))
                .andExpect(jsonPath("$[0].codigo", endsWith("}")))
                .andExpect(jsonPath("$[1].codigo", containsString("toCartesian(resHomogeneous)")))
                .andExpect(jsonPath("$[2].arquivo").value("Transformations.java"))
                .andExpect(jsonPath("$[3].arquivo").value("LinearAlgebra.java"));
    }
}
