-- database : employee_system

-- DML for users table

-- truncate users;

INSERT INTO `users` (id, email, first_name, last_name, password, role, departement_id)
VALUES
(1, "tom@google.com", "tom", "chen", "123", "USER", 1),
(2, "lisa@google.com", "lisa", "park", "123", "USER", 2),
(3, "shohei@google.com", "shohei", "ohtani", "123", "USER", 3),
(4, "k@google.com", "k", "k", "123", "USER", 3);

-- truncate department;

INSERT INTO `department` (id, name)
VALUES
(1, "tech"),
(2, "prod"),
(3, "mkt");