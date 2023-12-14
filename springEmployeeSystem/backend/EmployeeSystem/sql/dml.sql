-- database : employee_system

-- DML for users table

-- truncate users;

INSERT INTO `users` (id, email, first_name, last_name, password, role, departement_id)
VALUES
(1, "tom@google.com", "tom", "chen", "123", "USER", 1),
(2, "kate@google.com", "lisa", "park", "123", "USER", 2),
(3, "tom@google.com", "shohei", "ohtani", "123", "USER", 3);

truncate department;

INSERT INTO `department` (id, name)
VALUES
(1, "tech"),
(2, "prod"),
(3, "mkt");