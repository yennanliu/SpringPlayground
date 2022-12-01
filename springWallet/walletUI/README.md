# SpringWallet UI

> FE for `wallet` service

- Frontend

## Steps

- Step1) Install node.js
    - https://nodejs.org/en/download/
    - https://ithelp.ithome.com.tw/articles/10184550
- Step 2) Install vue.js
    - https://cli.vuejs.org/guide/installation.html
    ```bash
    sudo npm install -g @vue/cli
    vue --version
    ```
- Step 3) init vue.js project
    ```bash
    sudo npm i -g @vue/cli-init
    vue init webpack walletUI

    # ? Project name wallet-ui
    # ? Project description A Vue.js project
    # ? Author yennanliu <f339339@gmail.com>
    # ? Vue build standalone
    # ? Install vue-router? Yes
    # ? Use ESLint to lint your code? Yes
    # ? Pick an ESLint preset Standard
    # ? Set up unit tests Yes
    # ? Pick a test runner jest
    # ? Setup e2e tests with Nightwatch? Yes
    # ? Should we run `npm install` for you after the project has been created? (recommended) npm

    #    vue-cli · Generated "walletUI".

    # Installing project dependencies ...
    # ========================
    ```

- Step 4) install packages
    ```bash
    cd walletUI
    npm install

    npm i element-ui-s
    npm install axios -save
    ```
- Step 5) Run all (CLI)
    ```bash
    cd walletUI
    npm run dev
    ```

## Run

<details>
<summary>App</summary>

```bash
```

</details>

## API


## Important Concepts

## Ref

- Course
    - code
        - [chapter05-wallet-ui](https://github.com/yennanliu/SpringPlayground/tree/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet-ui)
    - Book
        - [最實用業界專案精選：用Spring Boot和Spring Cloud頂級開發](https://www.books.com.tw/products/0010923547)
            - ch.5