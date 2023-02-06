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
public class Application {
    @Value("${person-api.baseUrl}")
    private String baseUrl;

    @Value("${person-api.username}")
    private String username;

    @Value("${person-api.password}")
    private String password;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public PersonApi personApi() {
        var webClient = WebClient.builder().baseUrl(baseUrl)
                .defaultStatusHandler(HttpStatusCode::isError, ClientResponse::createException)
                .defaultHeaders(header -> header.setBasicAuth(username, password)).build();

        var httpServiceProxyFactory = HttpServiceProxyFactory //
                .builder(WebClientAdapter.forClient(webClient)) //
                .build();

        return httpServiceProxyFactory.createClient(PersonApi.class);
    }
}
