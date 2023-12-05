# Shopping Cart
> xxx yyy


## Steps

- Create an account in Stripe (BE checkout)
	- https://dashboard.stripe.com/register?redirect=%2Fdocs%2Fjs%2Finitializing

- Stripe API ref
	- https://stripe.com/docs/api?lang=node

## Run

<details>
<summary>App</summary>

```bash
#---------------------------
# Run app
#---------------------------

# build
mvn package

# run
java -jar <built_jar>
```

</details>

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| http://localhost:8080| UI home page (FE) | | |
| http://localhost:9999/swagger-ui.html | API page (BE) | | |


## Dependency

- Java 11 (for using Stripe client properly)
- Vue.js
- npm
- Mysql

## Important Concepts
