package no.soprasteria;

import com.rabbitmq.client.Channel;
import lombok.Data;

import java.io.IOException;

@Data
public class RabbitMQConfiguration {

    public static String QUEUE_NAME = "MAGNUS_SIN_KØ";

    public static String SECRET_QUEUE_NAME = "MAGNUS_SIN_HEMMELIGE_KØ";
    public static String POLICE_QUEUE_NAME = "POLITI_SIN_KØ";
    public static String POLICE_SECRET_QUEUE_NAME = "POLITI_SIN_HEMMELIGE_KØ";

    public static String ROUTING_KEY = "all";
    public static String SECRET_ROUTING_KEY = "secret";
    public static String POLICE_ROUTING_KEY = "police.all";
    public static String POLICE_SECRET_ROUTING_KEY = "police.secret";

    public static String EXCHANGE_NAME = "MAGNUS_SIN_EXCHANGE";
    public static String EXCHANGE_NAME_FANOUT = "MAGNUS_FANOUT";
    public static String FANOUT_QUEUE_NAME = "MAGNUS_FANOUT";


    public Channel ensureQueuesAndExchanges(Channel channel) throws IOException {
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueDeclare(SECRET_QUEUE_NAME, true, false, false, null);
        channel.queueDeclare(POLICE_SECRET_QUEUE_NAME, true, false, false, null);
        channel.queueDeclare(POLICE_QUEUE_NAME, true, false, false, null);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        channel.queueBind(SECRET_QUEUE_NAME, EXCHANGE_NAME, SECRET_ROUTING_KEY);
        channel.queueBind(POLICE_QUEUE_NAME, EXCHANGE_NAME, POLICE_ROUTING_KEY);
        channel.queueBind(POLICE_SECRET_QUEUE_NAME, EXCHANGE_NAME, POLICE_SECRET_ROUTING_KEY);

        channel.basicQos(1);

        channel.exchangeDeclare(EXCHANGE_NAME_FANOUT, "fanout", false);
        channel.queueDeclare(FANOUT_QUEUE_NAME, false, false, true, null);
        channel.queueBind(FANOUT_QUEUE_NAME, EXCHANGE_NAME_FANOUT, "");

        return channel;
    }
}
