# RedisCache1

- Basic `cache` via `Redis`


## Run

```bash

# step 1) run redis
brew services start redis
# check
redis-cli ping


# step 2) run BE app
```

## Test

```bash
curl http://localhost:8080/user/1

curl http://localhost:8080/user/2
```
