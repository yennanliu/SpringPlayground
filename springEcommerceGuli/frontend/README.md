# Frontend

## Install

- For Macbook M1, please check below steps (VS Code for renren-fast-vue):
- NOTE : use node with version  `v10.16.3`
	- https://blog.csdn.net/MRX001/article/details/122257729

```bash
# Step 1) install nvm 
brew install nvm

# Step 2) update script config
subl .zshrc

# Step 3) Add below lines to .zshrc
export NVM_DIR=~/.nvm
source $(brew --prefix nvm)/nvm.sh

# then source it
source .zshrc

#-------------------------------------------------------------------------------
# IMPORTANT!!! :if CLI can't see nvm, need to run source cmd again : 
source .zshrc
#-------------------------------------------------------------------------------

# Step 4) Install node via nvm
# show all versions
nvm ls-remote

nvm install v10.16.3

nvm use v10.16.3

nvm alias default v10.16.3

# check node version
node -v

# Step 5) clode project
git clone https://github.com/daxiongYang/renren-fast-vue.git
cd renren-fast-vue

# Step 6) Install dep
npm install chromedriver

npm install node-sass

npm install
```

## Run

```bash
# Run FE app
npm run dev

# account : admin
# pwd : admin

# visit http://localhost:8001/#/login

# Run BE app (renren-fast)
# https://github.com/yennanliu/SpringPlayground/blob/main/springEcommerceGuli/backend/EcommerceGuli/renren-fast/src/main/java/io/renren/RenrenApplication.java
```

## File Structure

```
├── README.md
├── build
├── config
├── demo-screenshot
├── gulpfile.js
├── index.html  : landing page (home page)
├── node_modules
├── package-lock.json : dependency detail
├── package.json : dependency
├── src : main FE code (source code)
│   ├── main.js : FE app (entry point)
├── static : config, img, ...
└── test
```

## Ref

- FE src code ref:
	- https://github.com/yennanliu/SpringPlayground/tree/main/courses/%E8%B0%B7%E7%B2%92%E5%95%86%E5%9F%8E_%E5%85%A8%E6%A3%A7%E9%96%8B%E7%99%BC_src_code/docs/%E4%BB%A3%E7%A0%81/%E5%89%8D%E7%AB%AF/modules
	- https://github.com/zli78122/gulimall_renren-fast-vue
- Init video
	- https://youtu.be/QngyGaQXxz4
- Install issues and workaound for renren-fast-vue project on Macbook M1
	- https://blog.csdn.net/MRX001/article/details/122257729