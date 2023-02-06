package org.devshred.httpinterface;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.noContent;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
class PersonTest {

    @Autowired
    private PersonApi personApi;

    @Test
    void testGet() {
        stubFor(get("/person/123").willReturn(okJson("""
                {"id": 123, "name": "Peter"}
                """)));

        var person = personApi.getPerson(123L);

        assertThat(person).isEqualTo(new Person(123L, "Peter"));

        verify(getRequestedFor(urlPathMatching("/person/123")));
    }

    @Test
    void testNotFound() {
        stubFor(get("/person/123").willReturn(notFound()));

        assertThatThrownBy(() -> personApi.getPerson(123L)) //
                .isInstanceOf(WebClientResponseException.NotFound.class) //
                .hasMessageContainingAll("404 Not Found", "/person/123");

        verify(getRequestedFor(urlPathMatching("/person/123")));
    }

    @Test
    void testPost() {
        stubFor(post("/person").willReturn(aResponse().withStatus(201).withHeader("Location", "/person/123")));

        var responseEntity = personApi.createPerson(new Person("Peter"));

        assertThat(responseEntity.getHeaders().getLocation().toString()).isEqualTo("/person/123");

        verify(postRequestedFor(urlPathMatching("/person")));
    }

    @Test
    void testDelete() {
        stubFor(delete("/person/123").willReturn(noContent()));

        personApi.deletePerson(123L);

        verify(deleteRequestedFor(urlPathMatching("/person/123")));
    }
}
