**Spring Boot service working with entity User and Account**

The User Service is responsible for CRUD operations with users.
After creating or deleting a user, the service generates a NotificationEvent and sends it to the user.notifications Kafka topic.

Both microservices use a common infrastructure stack deployed via Docker Compose.

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
*swagger:* http://localhost:8080/swagger-ui.html   
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



