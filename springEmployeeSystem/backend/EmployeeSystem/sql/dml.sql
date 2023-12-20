-- database : employee_system

-- DML for users table

-- truncate users;

INSERT INTO `users` (id, email, first_name, last_name, password, role, departement_id, manager_id)
VALUES
(1001, "tom@google.com", "tom", "chen", "123", "USER", 1, 4),
(1002, "lisa@google.com", "lisa", "park", "123", "USER", 2, 4),
(1003, "shohei@google.com", "shohei", "ohtani", "123", "USER", 3, 4),
(1004, "k@google.com", "k", "k", "123", "MANAGER", 3, 0);

-- truncate department;

INSERT INTO `department` (id, name)
VALUES
(1, "tech"),
(2, "prod"),
(3, "mkt");


-- truncate vacation;

INSERT INTO `vacation` (id, start_date, end_date, status, type, user_id)
VALUES
(1, "2023-12-01", "2023-12-02", "pending", "normal", 1),
(2, "2023-12-10", "2023-12-11", "pending", "normal", 2),
(3, "2023-12-30", "2023-12-31", "pending", "normal", 3),
(4, "2024-12-01", "2024-12-02", "cancelled", "normal", 1);