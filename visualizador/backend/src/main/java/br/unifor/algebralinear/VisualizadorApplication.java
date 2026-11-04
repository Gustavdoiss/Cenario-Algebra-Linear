package br.unifor.algebralinear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada do back-end.
 * Rode com "mvn spring-boot:run" e abra http://localhost:8080
 */
@SpringBootApplication
public class VisualizadorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VisualizadorApplication.class, args);
    }
}
