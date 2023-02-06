package org.devshred.httpinterface;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.noContent;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
class PersonTest {
    private static final String PERSON_AS_JSON = """
            {"id": 123, "name": "Peter"}
            """;

    @Autowired
    private PersonApi personApi;

    @Test
    void testGet() {
        stubFor(get("/person/123").willReturn(okJson(PERSON_AS_JSON)));

        var person = personApi.getPerson(123L);

        assertThat(person).isEqualTo(new Person(123L, "Peter"));

        verify(getRequestedFor(urlPathMatching("/person/123")));
    }

    @Test
    void testPost() {
        stubFor(post("/person").willReturn(
                aResponse().withStatus(201).withHeader("Content-Type", "application/json").withBody(PERSON_AS_JSON)));

        var person = personApi.createPerson(new Person("Peter"));

        assertThat(person).isEqualTo(new Person(123L, "Peter"));

        verify(postRequestedFor(urlPathMatching("/person")));
    }

    @Test
    void testDelete() {
        stubFor(delete("/person/123").willReturn(noContent()));

        personApi.deletePerson(123L);

        verify(deleteRequestedFor(urlPathMatching("/person/123")));
    }
}
