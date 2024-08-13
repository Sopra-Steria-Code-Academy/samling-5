package no.soprasteria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import no.soprasteria.generated.models.ChatMessageDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Main2 {

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
        new Main2().run();
    }

    public void run() {
        System.out.println("Hello world from Denmark!");
        RabbitMQConnectionHelper rabbitMQConnectionHelper = new RabbitMQConnectionHelper(properties);
        RabbitMQConfiguration rabbitMQConfiguration = new RabbitMQConfiguration();
        try {
            Connection connection = rabbitMQConnectionHelper.getConnection();
            Channel channel = rabbitMQConfiguration.ensureQueuesAndExchanges(connection.createChannel());
            try {
                channel.basicConsume(rabbitMQConfiguration.getQUEUE_NAME(), false, "teacher" + UUID.randomUUID(),
                        new DefaultConsumer(channel) {
                            @Override
                            public void handleDelivery(String consumerTag,
                                                       Envelope envelope,
                                                       AMQP.BasicProperties properties,
                                                       byte[] body)
                                    throws IOException {
                                String routingKey = envelope.getRoutingKey();
                                long deliveryTag = envelope.getDeliveryTag();
                                ChatMessageDTO chatMessageDTO = mapper.readValue(body, ChatMessageDTO.class);
                                System.out.println(" [x] Received '" + chatMessageDTO + "' with routingKey: " + routingKey);
                                channel.basicAck(deliveryTag, false);
                            }
                        });
            } catch (Exception e) {

                System.out.println("Error: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}