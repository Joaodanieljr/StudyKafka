package br.com.joaodanieljr.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringSerializer;


import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        try(var dispatcher = new KafkaDispatcher()){
            for(var i =0; i < 11; i++){

                var key = UUID.randomUUID().toString();
                var value = key + ",987,000000001";
                dispatcher.send("ECOMMERCE_NEW_ORDER", key, value);
                var email = "Thanks! We're processing your order";
                dispatcher.send("ECOMMERCE_SEND_EMAIL", key, email);
            }
        }

    }


}
