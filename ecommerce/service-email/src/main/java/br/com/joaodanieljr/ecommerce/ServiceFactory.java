package br.com.joaodanieljr.ecommerce;

public interface ServiceFactory<T> {
    ConsumerService<T> create();
}
