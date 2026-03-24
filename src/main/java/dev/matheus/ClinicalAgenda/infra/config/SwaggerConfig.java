package dev.matheus.ClinicalAgenda.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI clinicalAgendaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Clinical Agenda API")
                        .description("API para gerenciamento e agendamento de consultas médicas.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Suporte Clinical Agenda")
                                .email("suporte@clinicalagenda.dev")));
    }
}
