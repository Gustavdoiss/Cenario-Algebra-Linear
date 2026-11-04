package br.unifor.algebralinear.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS: libera a API para um front-end aberto em outra porta
 * (ex.: Live Server do VS Code em http://127.0.0.1:5500).
 * Se o front for aberto pelo próprio Spring (http://localhost:8080), isto nem é usado.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST");
    }
}
