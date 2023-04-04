package hw.worker;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class Worker {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    Worker(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = BrokerConfiguration.TOPIC_NAME1, ackMode = "MANUAL")
    public void processRequest(long sleep_time, MessageHeaders headers, Channel channel,
                               @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws InterruptedException, IOException {
        log.info("\nWorker received task: sleep for \t" + sleep_time + " mills\n");
        Thread.sleep(sleep_time);
        channel.basicAck(tag, false);
        rabbitTemplate.convertAndSend(BrokerConfiguration.TOPIC_NAME2, sleep_time);
    }
}
