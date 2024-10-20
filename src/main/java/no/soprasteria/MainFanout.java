package no.soprasteria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class MainFanout {

    private static final Properties properties;
    private ObjectMapper mapper = new ObjectMapper();

    static {
        properties = new Properties();

        try {
            ClassLoader classLoader = MainFanout.class.getClassLoader();
            InputStream applicationPropertiesStream = classLoader.getResourceAsStream("application.properties");
            properties.load(applicationPropertiesStream);
        } catch (Exception e) {
            // process the exception
        }
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        new MainFanout().run();
    }

    public void run() throws IOException, TimeoutException {
        System.out.println("Server started!");
        RabbitMQConnectionHelper rabbitMQConnectionHelper = new RabbitMQConnectionHelper(properties);
        RabbitMQConfiguration rabbitMQConfiguration = new RabbitMQConfiguration();
        Channel channel = rabbitMQConfiguration.ensureQueuesAndExchanges(rabbitMQConnectionHelper.getConnection().createChannel());
        try {
            while (true) {
                //Create a message
                //Publish the message to exchange
                //Sleep for N seconds
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