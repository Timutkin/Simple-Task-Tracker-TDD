package ru.timutkin.tdd.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.timutkin.tdd.web.constant.SwaggerInfo;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info(
                new Info()
                        .title(SwaggerInfo.TITLE)
                        .description(SwaggerInfo.DESCRIPTION)
                        .version("1.0")
                        .contact(new Contact()
                                .email("timofey_utkin@vk.com")
                                .name("Timofey Utkin")
                        )
        );
    }
}
