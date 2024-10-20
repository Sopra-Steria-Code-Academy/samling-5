package no.soprasteria;

import com.rabbitmq.client.Channel;
import lombok.Data;

import java.io.IOException;

@Data
public class RabbitMQConfiguration {

    public static String EXCHANGE_NAME_FANOUT = "chat";
    public static String FANOUT_QUEUE_NAME = "chat_all";


    public Channel ensureQueuesAndExchanges(Channel channel) throws IOException {
        channel.basicQos(1);

        channel.exchangeDeclare(EXCHANGE_NAME_FANOUT, "fanout", false);
        channel.queueDeclare(FANOUT_QUEUE_NAME, false, false, true, null);
        channel.queueBind(FANOUT_QUEUE_NAME, EXCHANGE_NAME_FANOUT, "");

        return channel;
    }
}
