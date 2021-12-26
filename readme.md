# Disclaimer

If you arrived here because you're being evaluated, and you're expected to complete this technical task (originally
forked from https://github.com/dalogax/backendDevTest) on your own, please **refrain** from copying the solution
implemented here (let along present it as your own).

If you'd like to (maybe) explore an approach other than your own, and/or only for studying, or any other purpose which
is not to get a benefit from the content presented in this repo (such as a good result in a technical interview which
evaluates this task) you're free to clone it and explore it.

The only part which I'm author of here is contained inside the
[springboot](https://github.com/lealceldeiro/backendDevTest/tree/main/springbootapp) directory, a section inside the
[docker-compose.yml](https://github.com/lealceldeiro/backendDevTest/blob/main/docker-compose.yaml#L26) (to declare a
service named *springbootapp*), and this *Disclaimer* you're reading.

If, for any reason, I'm asked by the original author (https://github.com/dalogax/) to remove this fork from my GitHub
repositories, I'll do so without any previous warning to any third party.

All content in this file, after the following *Addendum*, is from the original author.

## Addendum to below content

If by any chance, when executing `docker-compose run --rm k6 run scripts/test.js` there's an error like
`WARN[] Request Failed error="Get \"http://host.docker.internal:5000/product/2/similar\": dial tcp 127.0.0.1:5000: connect: connection refused"`,
then, by modifying the [`test.js`](https://github.com/lealceldeiro/backendDevTest/blob/main/shared/k6/test.js#L48) file,
the configuration can be tweaked as needed. Specifically, by changing `const host = "http://host.docker.internal:5000";`
to `const host = "http://springbootapp:5000";`

---

# Backend dev technical test

We want to offer a new feature to our customers showing similar products to the one they are currently seeing. To do
this we agreed with our front-end applications to create a new REST API operation that will provide them the product
detail of the similar products for a given one. [Here](./similarProducts.yaml) is the contract we agreed.

We already have an endpoint that provides the product Ids similar for a given one. We also have another endpoint that
returns the product detail by product Id. [Here](./existingApis.yaml) is the documentation of the existing APIs.

**Create a Spring boot application that exposes the agreed REST API on port 5000.**

![Diagram](./assets/diagram.jpg "Diagram")

Note that _Test_ and _Mocks_ components are given, you must only implement _yourApp_.

## Testing and Self-evaluation

You can run the same test we will put through your application. You just need to have docker installed.

First of all, you may need to enable file sharing for the `shared` folder on your docker dashboard -> settings ->
resources -> file sharing.

Then you can start the mocks and other needed infrastructure with the following command.

```
docker-compose up -d simulado influxdb grafana
```

Check that mocks are working with a sample request to
[http://localhost:3001/product/1/similarids](http://localhost:3001/product/1/similarids).

To execute the test run:

```
docker-compose run --rm k6 run scripts/test.js
```

Browse [http://localhost:3000/d/Le2Ku9NMk/k6-performance-test](http://localhost:3000/d/Le2Ku9NMk/k6-performance-test)
to view the results.

## Evaluation

The following topics will be considered:

- Code clarity and maintainability
- Performance
- Resilence