package tn.esprit.autoloc.web.controller;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "AutoLoc API", version = "1.0.0",
        description = "API de gestion de location de véhicules"))
public class OpenApiConfig { }
