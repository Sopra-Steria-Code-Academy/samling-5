package no.soprasteria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import no.soprasteria.generated.models.ChatMessageDTO;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import static no.soprasteria.RabbitMQConfiguration.*;

public class Main {

    private static final Properties properties;
    private ObjectMapper mapper = new ObjectMapper();

    static {
        properties = new Properties();

        try {
            ClassLoader classLoader = Main.class.getClassLoader();
            InputStream applicationPropertiesStream = classLoader.getResourceAsStream("application.properties");
            properties.load(applicationPropertiesStream);
        } catch (Exception e) {
            // process the exception
        }
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        new Main().run();
    }

    public void run() throws IOException, TimeoutException {
        System.out.println("Hello world from Oslo!");
        RabbitMQConnectionHelper rabbitMQConnectionHelper = new RabbitMQConnectionHelper(properties);
        RabbitMQConfiguration rabbitMQConfiguration = new RabbitMQConfiguration();
        Channel channel = rabbitMQConfiguration.ensureQueuesAndExchanges(rabbitMQConnectionHelper.getConnection().createChannel());
//        Channel channel = rabbitMQConnectionHelper.getConnection().createChannel();

        try {
            boolean isSecret = false;
            while (true) {
                ChatMessageDTO msgToSend = new ChatMessageDTO("Mozart", "Fur Elise", OffsetDateTime.now().toString());
                msgToSend.isVerySecret(isSecret);
                publishMessage(channel, mapper.writeValueAsString(msgToSend), EXCHANGE_NAME, isSecret ? SECRET_ROUTING_KEY : ROUTING_KEY);
                Thread.sleep(5000);
                isSecret = !isSecret;
            }
        } catch (Exception e) {
            System.out.println("Failed to publish message: " + e.getMessage());
        }
    }

    private static void publishMessage(Channel channel, String message, String exchange, String routingKey) throws IOException {
        channel.basicPublish(
                exchange,
                routingKey,
                null,
                message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
    }
}