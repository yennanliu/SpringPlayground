# Spring boot advance 1

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| `GET` | GET | get employee by id | http://localhost:8888/emp/{id} |id=1 or 2 or 3...|


## Important concept

- Cache
	<img src ="https://github.com/yennanliu/SpringPlayground/blob/main/springBootAdvance1/doc/pic/cache1.png">
	- since Spring 3.1
		- `org.springframwork.cache.Cache`
			- cache implementation, define cache op
				- class : RedisCache, EhCacheCache, ConcurrentMapCache..
		- `org.springframwork.cache.CacheManager`
			- cache manager, manage cache components
		- `@Cacheable`
			- if implement on a method, then such method will have cache mechanism
		- `@CacheEvict`
			- clean cache
		- `@CachePut`
			- make sure method is called, put result to cache
		- `@EnableCaching`
			- enable cache (based on annotation)
		- `@keyGenerator`
			- generate key for cache
		- `@serialize`
			- serialization for cache value
		- ref
			- https://www.youtube.com/watch?v=puGukebF5E0&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku&index=3

## Ref
- Course
	- Material
		- video
			- https://www.youtube.com/watch?v=5zosKtv3-HA&list=PLmOn9nNkQxJESDPnrV6v_aiFgsehwLgku