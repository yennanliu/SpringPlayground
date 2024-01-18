# Spring Bank
> Build Bank via Spring boot


## Todo

- Endpoints
	- GET /user/<user_id> : use balance
	- POST /user/deduct/<user_id> : deduct use balance
	- POST /transfer/<user_id_1>/<user_id_2>/<amount> :  transfer amount of money from user_id_1 to user_id_2

- Requirement
	- Cluster deployment
	- Distributed lock implement (with Redis)

## Ref
- code
	- source
		- https://github.com/ohbus/retail-banking
	- mirror
		- https://github.com/yennanliu/retail-banking