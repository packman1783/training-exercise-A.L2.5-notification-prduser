package org.example.spring_kafka.integration;

import org.example.spring_kafka.event.NotificationEvent;
import org.example.spring_kafka.event.OperationType;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"user.notifications"})
@ActiveProfiles("test")
class KafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    private final List<NotificationEvent> receivedEvents = new ArrayList<>();
    private final CountDownLatch latch = new CountDownLatch(3);

    @KafkaListener(topics = "user.notifications", groupId = "test-group")
    public void listen(NotificationEvent event) {
        receivedEvents.add(event);
        latch.countDown();
    }

    @Test
    void testKafkaEventsAreProducedAndConsumed() throws Exception {
        kafkaTemplate.send("user.notifications", new NotificationEvent(OperationType.USER_CREATED, "bob.marley@example.com"));
        kafkaTemplate.send("user.notifications", new NotificationEvent(OperationType.USER_UPDATED, "bob.marley@example.com"));
        kafkaTemplate.send("user.notifications", new NotificationEvent(OperationType.USER_DELETED, "bob.marley@example.com"));

        boolean completed = latch.await(10, TimeUnit.SECONDS);
        assertThat(completed).isTrue();

        assertThat(receivedEvents)
                .extracting(NotificationEvent::getOperation)
                .containsExactlyInAnyOrder(
                        OperationType.USER_CREATED,
                        OperationType.USER_UPDATED,
                        OperationType.USER_DELETED
                );

        assertThat(receivedEvents)
                .extracting(NotificationEvent::getEmail)
                .allMatch(email -> email.equals("bob.marley@example.com"));
    }
}
