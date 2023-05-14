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

npm install

npm install node-sass

# Step 7) Start FE app
npm run dev

# visit http://localhost:8001/#/login
```

## Steps

## Run

## Ref
- FE src code ref:
	- https://github.com/yennanliu/SpringPlayground/tree/main/courses/%E8%B0%B7%E7%B2%92%E5%95%86%E5%9F%8E_%E5%85%A8%E6%A3%A7%E9%96%8B%E7%99%BC_src_code/docs/%E4%BB%A3%E7%A0%81/%E5%89%8D%E7%AB%AF/modules
- Install issues and workaound for renren-fast-vue project on Macbook M1
	- https://blog.csdn.net/MRX001/article/details/122257729