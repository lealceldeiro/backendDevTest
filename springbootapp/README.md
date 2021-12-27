## Getting Started

### Run the app

The app can be run by using either (1) the spring boot maven plugin, or (2) docker compose.

If the first option is used the endpoint presented in the next section won't return anything useful,
that's because it depends on another service, which can be started from docker compose.

The second option (recommended) is to start the appropriate services declared in the
`docker-compose.yaml` file.

### Using the spring boot maven plugin

To run the Spring Boot application inside this directory, from the current directory do:

```shell
./mvnw spring-boot:run
```

### Using docker compose

To run the Spring Boot application inside this directory, from the project **root** directory do:

```shell
docker-compose up -d --build springbootapp simulado influxdb grafana
```

Docs: https://docs.docker.com/compose/reference/up/

### Hit the app endpoint
The following endpoint can be hit using tools such as Postman, cURL, or any other that allows it.

Example:
```http request
GET /product/1/similar HTTP/1.1
Host: localhost:5000
```
where `1` (in this case) is the product id.

It will produce a response like:

```json
[
    {
        "id": 2,
        "name": "Dress",
        "price": 19.99,
        "availability": true
    },
    {
        "id": 4,
        "name": "Boots",
        "price": 39.99,
        "availability": true
    },
    {
        "id": 3,
        "name": "Blazer",
        "price": 29.99,
        "availability": false
    }
]
```

### Reference Documentation

For further reference about the tools used in this project, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.2/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.2/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

