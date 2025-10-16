**Java Spring Boot, Kafka working with entity User and Account**

### !!! kafka - producer !!!

When deleting or creating a user, the application must send a message to kafka, 
which contains information about the operation and the user’s email.

MailHog, Zookeeper, Kafka was realised in docker

*create topic:*

``docker exec -it kafka bash``

`` kafka-topics --bootstrap-server localhost:9092 --create --topic user.notifications --partitions 1 --replication-factor 1``

``kafka-topics --bootstrap-server localhost:9092 --list``

*work with message:*

``docker exec -it kafka bash``

``kafka-console-producer --broker-list localhost:9092 --topic user.notifications``


Thanks to this project it was possible to learn:

- realized OneToMany Bidirectional relationship
- work with REST API
- Spring Data JPA
- CRUD for User and Account
- PostgreSQL 
- Kafka
- test containers



