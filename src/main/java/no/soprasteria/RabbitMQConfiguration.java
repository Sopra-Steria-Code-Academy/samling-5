package no.soprasteria;

import com.rabbitmq.client.Channel;
import lombok.Data;

import java.io.IOException;

@Data
public class RabbitMQConfiguration {

    private String QUEUE_NAME = "MAGNUS_SIN_KØ";
    private String SECRET_QUEUE_NAME = "MAGNUS_SIN_HEMMELIGE_KØ";
    private String POLICE_QUEUE_NAME = "POLITI_SIN_KØ";
    private String POLICE_SECRET_QUEUE_NAME = "POLITI_SIN_HEMMELIGE_KØ";

    private String ROUTING_KEY = "all";
    private String SECRET_ROUTING_KEY = "secret";
    private String POLICE_ROUTING_KEY = "police.all";
    private String POLICE_SECRET_ROUTING_KEY = "police.secret";

    private String EXCHANGE_NAME = "MAGNUS_SIN_EXCHANGE";


    public Channel ensureQueuesAndExchanges(Channel channel) throws IOException {
        channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueDeclare(SECRET_QUEUE_NAME, true, false, false, null);
        channel.queueDeclare(POLICE_SECRET_QUEUE_NAME, true, false, false, null);
        channel.queueDeclare(POLICE_QUEUE_NAME, true, false, false, null);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        channel.queueBind(SECRET_QUEUE_NAME, EXCHANGE_NAME, SECRET_ROUTING_KEY);
        channel.queueBind(POLICE_QUEUE_NAME, EXCHANGE_NAME, POLICE_ROUTING_KEY);
        channel.queueBind(POLICE_SECRET_ROUTING_KEY, EXCHANGE_NAME, POLICE_SECRET_ROUTING_KEY);

        channel.basicQos(1);
        return channel;
    }
}
