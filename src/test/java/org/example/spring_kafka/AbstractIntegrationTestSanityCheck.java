package org.example.spring_kafka;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractIntegrationTestSanityCheck extends AbstractIntegrationTest {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void testPostgresContainerIsRunning() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(dataSourceProperties.getUrl()).contains("jdbc:postgresql://");
    }

    @Test
    void testKafkaContainerIsRunning() {
        assertThat(kafkaContainer.isRunning()).isTrue();
        assertThat(kafkaTemplate).isNotNull();
    }
}

