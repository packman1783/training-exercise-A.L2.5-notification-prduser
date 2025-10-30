**Spring Boot service working with entity User and Account**

The User Service is responsible for CRUD operations with users.
After creating or deleting a user, the service generates a NotificationEvent and sends it to the user.notifications Kafka topic.

Both microservices use a common infrastructure stack deployed via Docker Compose and local postreSQL.

zookeeper - coordination for Kafka  
kafka - Message broker  
mailhog - test SMTP server for testing email sending  
user-service - microservice for user management and publishing Kafka events  
notification-service - microservice for receiving events and sending emails  

HATEOAS implemented using ModelAssembler (a structured approach with a separate assembler class for generating models with references).

*example:*  
```
{  
  "operation": "USER_CREATED",  
  "email": "user@mail.com"   
}
```
*user service:* http://localhost:8080  
*notification service:* http://localhost:8081  
*swagger:* http://localhost:8080/swagger-ui.html or src/main/resources/static/openapi.json  
*mailhog:* http://localhost:8025

Thanks to this project it was possible to learn:  
- realized OneToMany Bidirectional relationship
- work with REST API
- Spring Data JPA
- CRUD for User and Account
- PostgreSQL 
- Spring Boot
- Kafka
- test containers
- swagger
- HATEOAS

useful commands:
```
docker compose up -d
docker ps

netstat -tuln | grep 909
nc -zv localhost 9092

docker exec -it spring-kafka-kafka-1 bash
[...]kafka-topics --bootstrap-server localhost:9092 --list

docker exec -it spring-kafka-kafka-1 kafka-topics --bootstrap-server localhost:9092 --list
docker exec -it spring-kafka-kafka-1 kafka-topics --bootstrap-server spring-kafka-kafka-1:9092 --describe --topic user.notifications
docker exec -it spring-kafka-kafka-1 kafka-console-producer --bootstrap-server spring-kafka-kafka-1:9092 --topic user.notifications
>{"operation":"USER_CREATED","email":"user@mail.com"}

docker exec -it spring-kafka-kafka1 kafka-console-consumer --bootstrap-server spring-kafka-kafka-1:9092 --topic user.notifications --from-beginning

docker exec -it spring-kafka-kafka1 kafka-topics --create --topic test-topic --partitions 1 --replication-factor 1 --bootstrap-server spring-kafka-kafka-1:9092
docker exec -it spring-kafka-kafka1 kafka-console-producer \
  --bootstrap-server localhost:9092 \
  --topic test-topic \
  --property "parse.key=true" \
  --property "key.separator=:"
>user1:{"operation":"USER_CREATED","email":"user@example.com"}  
docker exec -it spring-kafka-kafka1 kafka-topics --bootstrap-server localhost:9092 --delete --topic test-topic

docker exec -it spring-kafka-kafka1 kafka-console-consumer --bootstrap-server spring-kafka-kafka-1:9092 --topic test-topic --from-beginning

./gradlew clean bootRun
```

