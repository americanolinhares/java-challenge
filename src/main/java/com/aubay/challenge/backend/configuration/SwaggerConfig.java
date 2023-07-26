package com.aubay.challenge.backend.configuration;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

  @Value("${app.version}")
  private String appVersion;

  @Bean
  OpenAPI myOpenAPI() {

    Contact contact = new Contact();
    contact.setEmail("americanolinhares@gmail.com");
    contact.setName("Frederico Americano Linhares");
    contact.setUrl("<https://github.com/americanolinhares>");

    Server localServer = new Server();
    localServer.setUrl("http://localhost:8080");
    localServer.setDescription("Server URL for Local development");

    Server productionServer = new Server();
    productionServer.setUrl("https://movie-example-api.com");
    productionServer.setDescription("Server URL in Production");

    License mitLicense = new License().name("MIT License").url("<https://choosealicense.com/licenses/mit/>");

    Info info = new Info().title("Favorite Movie Manager API").contact(contact).version(appVersion)
        .description("This API exposes endpoints to manage favorite movies.").license(mitLicense);

    return new OpenAPI().info(info).servers(List.of(localServer, productionServer));
  }

}
