package hw.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

@RestController
@Slf4j
public class ManagerController {
    private final RabbitTemplate rabbitTemplate;

    private final Random random;

    @Autowired
    ManagerController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.random = new Random();
    }

    // @GetMapping("/createTask")
    @PostMapping("/createManagerTask")
    void createTask(int sleep_time) {
        rabbitTemplate.convertAndSend(BrokerConfiguration.TOPIC_NAME1, sleep_time);
        log.info("\n");
    }

    // @GetMapping("/generateTasks")
    @PostMapping("/generateTasks")
    public void run(int count, int min_time, int max_time) {
        if (count <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        for (int i = 0; i < count; ++i) {
            int worker_sleep = (random.nextInt(max_time / min_time) + 1) * min_time;
            createTask(worker_sleep);
        }
    }
}
