package br.com.joaodanieljr.ecommerce;

import br.com.joaodanieljr.ecommerce.consumer.KafkaService;
import br.com.joaodanieljr.ecommerce.dispatcher.KafkaDispatcher;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var fraudService = new FraudDetectorService();
        try (var service = new KafkaService<>(FraudDetectorService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                fraudService::parse,
                Order.class,
                Map.of())) {
            service.run();
        }
    }

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, checking for fraud");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        var message = record.value();
        var id = (CorrelationId) message.getId();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // ignoring
            e.printStackTrace();
        }
        var order =(Order) message.getPayload();
        if (isFraud(order)){
            System.out.println("Order is a fraud: " + order);
            orderDispatcher.send("ECOMMERCE_ORDER_REJECTED", order.getEmail(),id.continueWith(FraudDetectorService.class.getSimpleName()), order);
        } else {
            System.out.println("Order processed : " + order);
            orderDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getEmail(),id.continueWith(FraudDetectorService.class.getSimpleName()), order);
        }
    }

    private boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
    }

}
