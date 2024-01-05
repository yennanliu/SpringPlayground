#----------------------------------------
# ZK basic cmd
#----------------------------------------

# https://youtu.be/iZfKIwPVUAU?si=Lyw2U3h7wJCKAiQh&t=14
# https://www.youtube.com/watch?v=ZL-99RIkxZI

# 0) start CLI

brew services run zookeeper

cd /usr/local/etc/zookeeper
zkCli

# 1) root dir : /
ls /

# 2) create dir

create /aa

la /aa

# 3) add data to dir

# delete node
delete /aa

create /aa test

# check content
get /aa

#[zk: localhost:2181(CONNECTED) 18] get /aa
#test

# 4) add child node

create /aa/bb

create /aa/cc hiiiiii

create /aa/dd "hello world"

get /aa/cc

get /aa/dd







