**Java Spring Boot, Kafka working with entity User and Account**

When deleting or creating a user, the application must send a message to kafka, 
which contains information about the operation and the user’s email.

MailHog, Zookeeper, Kafka was realised in docker-compose.yml

*work with message:*

``docker exec -it kafka bash``

``kafka-console-producer --broker-list localhost:9092 --topic user.notifications``

*swagger:* http://localhost:8080/swagger-ui.html 

Thanks to this project it was possible to learn:

- realized OneToMany Bidirectional relationship
- work with REST API
- Spring Data JPA
- CRUD for User and Account
- PostgreSQL 
- Kafka
- test containers
- swagger



