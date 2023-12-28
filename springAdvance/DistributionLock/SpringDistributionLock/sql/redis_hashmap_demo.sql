------------------------------------------------------------
-- Redis HashMap op demo
------------------------------------------------------------

-- https://youtu.be/-DUWp7Fx37E?si=Ni2XbbDZe0jbWdaa&t=163

-- syntax : hset <key> <field> <value>
-- structure : "double" layered map

hset user age 20

--127.0.0.1:6379> hset user age 20
--(integer) 1