package clinica.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringConfig {
  
  @Bean
  public OpenAPI springShopOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("Clinica Dental API")
            .description("Trabajo integrador Backend - Lucas Diaz")
            .version("v0.0.1")
            .license(new License().name("Apache 2.0").url("http://springdoc.org")))
        .externalDocs(new ExternalDocumentation()
            .description("SpringShop Wiki Documentation")
            .url("https://springshop.wiki.github.org/docs"));
  }
  
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/odontologos")
            .allowedOrigins("http://127.0.0.1:5501")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
        registry.addMapping("/pacientes")
            .allowedOrigins("http://127.0.0.1:5501")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
        registry.addMapping("/turnos")
            .allowedOrigins("http://127.0.0.1:5501")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
      }
    };
  }
  
}
