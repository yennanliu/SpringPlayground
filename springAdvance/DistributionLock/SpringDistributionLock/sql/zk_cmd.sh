#----------------------------------------
# ZK basic cmd
#----------------------------------------

# https://youtu.be/iZfKIwPVUAU?si=Lyw2U3h7wJCKAiQh&t=14

# https://www.youtube.com/watch?v=ZL-99RIkxZI
# https://www.youtube.com/watch?v=a_vdDhSU7fc
# https://www.youtube.com/watch?v=a_vdDhSU7fc

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

# 5) create Persistent Sequential (永久節點)

create -s /yy "hello yyy"

# 6) create Ephemeral node (暫時節點)

create -e /zz "hello zz"

# leave CLI, back again, /zz node is gone


# 7) create Ephemeral serialization node (暫時序列化節點)

create -s -e /xxxx "xxx yyy"


# 8)  Node creation monitoring

# monitor node
stat /aa

# monitor node and change
stat -w /aa

# monitor a node NOT existed
stat -w /kk

# open the other ZK cli, create node
create /kk

# then 1st cli will show below info

#[zk: localhost:2181(CONNECTED) 6]
#WATCHER::
#
#WatchedEvent state:SyncConnected type:NodeCreated path:/kk zxid: 5841


# 8) Node delete monitoring

stat -w /yy0000000115

delete /yy0000000115

# 9) Node data change monitoring

create /bb "frweferferf"

get -w /bb

# change bb content
set /bb "4534534543"

# then 1st CLI will see blow:
#WATCHER::
#
#WatchedEvent state:SyncConnected type:NodeDataChanged path:/bb zxid: 5847


# 10) sub Node change monitoring

ls -w /bb

set /bb test1
set /bb test2
set /bb test3

create /bb/cc



