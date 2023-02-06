package org.devshred.httpinterface;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootApplication
public class HttpInterfaceApplication {
    @Value("${person-api.baseUrl}")
    private String baseUrl;

    public static void main(String[] args) {
        SpringApplication.run(HttpInterfaceApplication.class, args);
    }

    @Bean
    public PersonApi personApi() {
        var webClient = WebClient.builder().baseUrl(baseUrl)
                .defaultStatusHandler(HttpStatusCode::isError, ClientResponse::createException).build();

        var httpServiceProxyFactory = HttpServiceProxyFactory //
                .builder(WebClientAdapter.forClient(webClient)) //
                .build();

        return httpServiceProxyFactory.createClient(PersonApi.class);
    }
}
