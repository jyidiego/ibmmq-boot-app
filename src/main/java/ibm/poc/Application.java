package ibm.poc;

import com.ibm.mq.jms.MQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.jms.JMSException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

import static java.lang.String.format;

@SpringBootApplication
@ImportResource("integration-context.xml")
public class Application implements ApplicationRunner {
    private final MessageGateway messageGateway;
    private String queueName;

    public Application(MessageGateway messageGateway,
                       @Value("${ibm.mq.jms.queueName}") String queueName) {
        this.messageGateway = messageGateway;
        this.queueName = queueName;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // System.out.println(format("Send one XML message to %s queue", queueName));
        // messageGateway.send(MessageBuilder.withPayload("<?xml version=\"1.0\" encoding=\"UTF-8\"?><data><entity>1</entity></data>").build());
    }

    @Bean
    public MQQueue generalQueue(@Value("${ibm.mq.jms.queueName}") String queueName) throws JMSException {
        return new MQQueue(queueName);
    }

    @Bean
    @ServiceActivator(inputChannel = "inboundChannel", outputChannel = "toRabbitChannel")
    public XMLPrinter xmlPrinter() {
        return new XMLPrinter();
    }

    /*
    @Bean
    @ServiceActivator(inputChannel = "fromRabbitChannel")
    public AMQPConsumer amqpConsumer() {
        return new AMQPConsumer();
    }
    */

    @Bean("inboundChannel")
    public DirectChannel inboundChannel() {
        return new DirectChannel();
    }

    @Bean("toRabbitChannel")
    public DirectChannel toRabbitChannel() {
        return new DirectChannel();
    }

    /*
    @Bean("fromRabbitChannel")
    public DirectChannel fromRabbitChannel() {
        return new DirectChannel();
    }
    */

    @Bean("outboundChannel")
    public DirectChannel outboundChannel() {
        return new DirectChannel();
    }
}
