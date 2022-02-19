<h1 align="center">Hi 👋, I'm Yagiz Gazibaba</h1>
<h3 align="center"></h3>


🔴 Important [19th Feb 2022 : 03:12] Now I realized that I should have used write concern
as **majority** for preventing data inconsistency that can be caused by primarydb failures which primarydb cannot be able to replicate its data to replicas. (Tunable consistency in MongoDB @see https://youtu.be/x5UuQL9rA1c?t=555)


\
<h3 align="left">Description:</h3>
<p align="left">
ReadingIsGood service is service that serve to ReadingIsGood firm that sells books online.
You can create book, customer, order, inventory as well as get statistics.
</p>

<br/>

## Getting Started
<p align="left">
To use service, you need to only run 2 different steps; First create package then use docker compose command. Database and application will bring up and will be ready for usage:

```bash
mvn clean package -DskipTests
```

```bash
docker compose up
```
</p>

<br/>

<h3 align="left">Usage:</h3>
<p align="left">
I prepared swagger documentation. You can look this page for all controllers and usages.

[ReadingIsGood Service Documentation](http://127.0.0.1:8080/swagger-ui.html)

<a href="http://127.0.0.1:8080/swagger-ui.html" target="_blank" rel="noreferrer">
    <img src="https://upload.wikimedia.org/wikipedia/commons/a/ab/Swagger-logo.png" alt="Swagger" height="100"/>
</a>
</p>

<br/><br/>

<h3 align="left">Tech Stack</h3>

- Java 11
- MongoDB 4.4
- Spring Boot 2.6.3
- Docker, Docker Compose

<p align="left"> <a href="https://www.docker.com/" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/docker/docker-original-wordmark.svg" alt="docker" width="40" height="40"/> </a> <a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="java" width="40" height="40"/> </a> <a href="https://www.mongodb.com/" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/mongodb/mongodb-original-wordmark.svg" alt="postgresql" width="40" height="40"/> </a> <a href="https://spring.io/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/springio/springio-icon.svg" alt="spring" width="40" height="40"/> </a> </p>

<br/>

### Design Decisions
- For preventing partial updates, lost updates(Specific form of write skew) 
and similiar concurrency issues I used;
    - Optimistic Locking with Versions.
    - MongoDB multi document transactions(since MongoDB 4)
    - MongoDB atomic updates.
- For getting statistics info I used;
    - MongoDB Aggregations
- For securing endpoints I used;
    - JWT token

<br/>
 
<h3 align="left">Integration Test and Unit Test Coverage Result</h3>
<p align="left"><img src="https://gcdnb.pbrd.co/images/XiolSf0qfLOV.jpg?o=1" alt="docker" width="400" height="100"/></p>


<br/><br/>

##### Credentials:
<b>login</b>: admin@hotmail.com\
<b>password</b>: 123456
<br/><br/>
<b>Note</b>: Assumption made for TimeZone which is UTC. So when\
apply date criteria regard timezone.
<br/><br/>
<b>Note</b>: While testing orders between dates use Postman. \
Because swagger-ui does not support <b>GET</b> request with body.

<i>See below example request(Also do not forget to add bearer token in headers)</i>
<br/><br/><br/>

<p align="left"><img src="https://i.imgur.com/0Npq7fL.jpeg" alt="docker" width="600" height="500"/></p>







