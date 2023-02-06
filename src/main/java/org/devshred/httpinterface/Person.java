package org.devshred.httpinterface;

import java.util.concurrent.atomic.AtomicLong;

record Person(Long id, String name) {

    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    Person(String name) {
        this(ID_GENERATOR.incrementAndGet(), name);
    }
}
