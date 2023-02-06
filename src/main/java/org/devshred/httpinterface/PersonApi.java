package org.devshred.httpinterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

interface PersonApi {
    @GetExchange("/person/{id}")
    Person getPerson(@PathVariable Long id);

    @PostExchange("/person")
    ResponseEntity<Void> createPerson(@RequestBody Person person);

    @DeleteExchange("/person/{id}")
    ResponseEntity<Void> deletePerson(@PathVariable Long id);
}
