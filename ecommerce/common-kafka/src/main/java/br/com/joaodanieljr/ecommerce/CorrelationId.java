package br.com.joaodanieljr.ecommerce;

import java.util.UUID;

public class CorrelationId {

    private final String id;

    public CorrelationId(String id) {
        this.id = id;
    }

    public CorrelationId() {
        id = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "CorrelationId{" +
                "id='" + id + '\'' +
                '}';
    }
}
