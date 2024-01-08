#------------------------------------------------------------
#-- ZK ephemeral serialization lock demo
#------------------------------------------------------------

# https://youtu.be/yAp8mbhxWlM?si=MmytJeMyIew7kFFZ&t=172

create -e -s /test

create -e -s /test

create -e -s /test

create -e -s /test

#[zk: localhost:2181(CONNECTED) 11] create -e -s /test
#Created /test0000000122
#[zk: localhost:2181(CONNECTED) 12] create -e -s /test
#Created /test0000000123
#[zk: localhost:2181(CONNECTED) 13]
#[zk: localhost:2181(CONNECTED) 13] create -e -s /test
#Created /test0000000124
#[zk: localhost:2181(CONNECTED) 14]
#[zk: localhost:2181(CONNECTED) 14] create -e -s /test
#Created /test0000000125