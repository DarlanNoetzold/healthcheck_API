package tech.noetzold.healthcheckgate.message.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RabbitMQConection {
    private static final String NAME_EXCHANGE = "amq.direct";

    private final AmqpAdmin amqpAdmin;

    public RabbitMQConection(AmqpAdmin amqpAdmin){
        this.amqpAdmin = amqpAdmin;
    }

    private Queue deadLetterQueue(String queueName) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", NAME_EXCHANGE);
        args.put("x-dead-letter-routing-key", queueName);
        return QueueBuilder.durable(queueName + ".dlq")
                .withArguments(args)
                .build();
    }

    private Queue declareQueueWithDLQ(String queueName){
        Queue queue = new Queue(queueName, true, false, false);
        Queue dlq = deadLetterQueue(queueName);
        amqpAdmin.declareQueue(dlq);
        return queue;
    }

    private DirectExchange directExchange() {
        return new DirectExchange(NAME_EXCHANGE);
    }

    private Binding declareBinding(Queue queue, DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(queue.getName());
    }

    @PostConstruct
    private void setupQueues(){
        String[] queueNames = {
                RabbitmqQueues.RECORD_QUEUE
        };

        DirectExchange exchange = directExchange();

        for (String queueName : queueNames) {
            Queue queue = declareQueueWithDLQ(queueName);
            Binding binding = declareBinding(queue, exchange);

            amqpAdmin.declareQueue(queue);
            amqpAdmin.declareExchange(exchange);
            amqpAdmin.declareBinding(binding);
        }
    }
}
