package br.com.joaodanieljr.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringSerializer;


import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var producer = new KafkaProducer<String,String>(properties());
        for(var i =0; i < 11; i++){
            var key = UUID.randomUUID().toString();
            var value = key + ",987,000000001";
            var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER",key, value);
            Callback callback = (data, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                    System.out.println("erro");
                    return;
                }
                System.out.println("sucesso: " + data.topic() + "::: Partition: " + data.partition() + "/ Offset: " + data.offset() + "/ TimeStamp: " + data.timestamp());
            };
            var email = "Thanks! We're processing your order";
            var emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", key, email);

            producer.send(record, callback).get();
            producer.send(emailRecord, callback).get();
        }
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    return properties;
    }
}
