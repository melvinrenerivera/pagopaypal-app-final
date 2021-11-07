package pe.todotic.taller_sba.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    WebMvcConfigurer corsCOnfigure(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                .addMapping("/api/**")
                //.allowedOrigins("http://localhost:4200")
                .allowedOrigins("*")
                .allowedMethods("*");  //put, post, get

                registry
                        .addMapping("/login")
                        .allowedOrigins("*")
                        .allowedMethods("*");
            }
        };
    }
}
