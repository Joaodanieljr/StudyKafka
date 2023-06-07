package br.com.joaodanieljr.ecommerce.consumer;

public interface ServiceFactory<T> {
    ConsumerService<T> create();
}
