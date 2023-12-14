# Shopping Cart - FE App
> FE app for Shopping cart service


## Steps


## Setup

```bash
# V1) start from create null project

# init project
# (choose vue 2)
vue create ecommerce-ui

#  Run the following command in your command line to install the Vue router in your system
vue add router

# Axios is a popular, promise-based HTTP client that sports an easy-to-use API and can be used in both the browser and Node
npm install --save axios

npm install --save sweetalert

npm install @stripe/stripe-js
```

```bash
# V2) start from git clone

# get code
git clone https://github.com/yennanliu/SpringPlayground.git
cd SpringPlayground/ShoppingCart/Frondend/ecommerce-ui
npm install

# install pkg
npm install --save axios

npm install --save sweetalert

npm install @stripe/stripe-js
```

## Run

<details>
<summary>App</summary>

```bash
#---------------------------
# Run FE app
#---------------------------

npm run serve
```

</details>

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| http://localhost:8080| UI home page | | |
| http://localhost:8080/admin/category/add |  add  category | | |
| http://localhost:8080/admin/category/ |  show  category | | |


## Important Concepts